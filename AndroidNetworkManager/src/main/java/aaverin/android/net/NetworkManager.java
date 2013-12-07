/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * Core NetworkManager implementation
 * 
 * NetworkManager operates with NetworkMessages.
 * NetworkMessages are added to the manager by a call to putMessage method.
 * 
 * All messages are stored in the internal queue until releaseQueue method is called.
 * After that messages are sent one by one, each in a separate thread.
 * During the process of message sending several callbacks are called
 * 
 * onQueueStart() - called when manager starts to go through a new queue. Queue becomes new when it's finished
 * onQueueFinished() - called when manager finished processing a queue
 * onQueueFailed() - called in case we have failed to process the queue. It happens when we exceed MAX_FAILURES
 * 
 * onNetworkSendStart(NetworkMessage message) - called for each particular message when manager starts the process of sending
 * 
 * onNetworkSendProgress(NetworkMessage message) - called while background AsyncTask goes through doInBackground method
 * 
 * onNetworkSendSuccess(NetworkMessage message, NetworkResponse response) - called when manager succeeds to send a message and got a response
 * 
 * onNetworkSendFailure(NetworkMessage message, NetworkResponse response) - called when manager fails to send a message
 * onNetworkSendFailure(NetworkMessage message, NetworkResponse response, Exception e)
 * 
 * Success is 200 OK in server response.
 * Everything else is a failure.
 */

package aaverin.android.net;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.integralblue.httpresponsecache.HttpResponseCache;

import aaverin.android.pDebug;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkManager extends AbstractNetworkManager implements OnNetworkSendListener {

	// [NOT YET IMPLEMENTED] Allows sending all messages from queue in one batch
	public final static boolean IS_BATCH_SEND_SUPPORTED = false;

	// Timeout of network connection
	private final static int NETWORK_TIMEOUT = 5000;

	// Maximum amount of attempts that can fail
	private final static int MAX_FAILURES = 3;

	// Currently used NetworkManager implementation
	public final static String NETWORK_MANAGER_CORE = NetworkManagerCore.HTTPURLCONNECTION;

	/**
	 * interface allowing to pick between Apache and URLConnection implementations
	 * 
	 * @author Anton Averin
	 * 
	 */
	public static interface NetworkManagerCore {
		public final static String APACHE = "APACHE_HTTP_CLIENT";
		public final static String HTTPURLCONNECTION = "HTTP_URL_CONNECTION";
	}

	protected static NetworkManager instance = null;
	private ArrayList<NetworkMessage> messageQueue = null;
	private NetworkThread mainNetworkThread = null;

	private int failuresCount = 0;

	private boolean queueProcessingStarted = false;

	private ArrayList<NetworkListener> listeners = new ArrayList<NetworkListener>();

	/**
	 * Subscribes listener to NetworkManager events
	 */
	public void subscribe(NetworkListener listener) {
		listeners.add(listener);
	}

	/**
	 * Unsubscribes listener from NetworkManager events
	 */
	public void unsubscribe(NetworkListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Checks if current listener is already subscribed
	 */
	public boolean isSubscribed(NetworkListener listener) {
		return listeners.contains(listener);
	}

	/**
	 * Clear all listener subscriptions
	 */
	public void clearListeners() {
		listeners.clear();
	}

	private NetworkManager() {
		messageQueue = new ArrayList<NetworkMessage>();
	}

	public static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}
		return instance;
	}

	/**
	 * Turns on httpCache. Please refer to HttpResponseCache documentation.
	 * 
	 * @param httpCacheDir
	 * @param httpCacheSize
	 */
	public void enableHttpCache(File httpCacheDir, long httpCacheSize) {
		try {
			Class.forName("android.net.http.HttpResponseCache").getMethod("install", File.class, long.class)
					.invoke(null, httpCacheDir, httpCacheSize);
		} catch (Exception httpResponseCacheNotAvailable) {
			// Ln.d(httpResponseCacheNotAvailable,
			// "android.net.http.HttpResponseCache not available, probably because we're running on a pre-ICS version of Android. Using com.integralblue.httpresponsecache.HttpHttpResponseCache.");
			try {
				HttpResponseCache.install(httpCacheDir, httpCacheSize);
			} catch (Exception e) {
				// Ln.e(e, "Failed to set up com.integralblue.httpresponsecache.HttpResponseCache");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds a message to the network queue
	 */
	public void putMessage(NetworkMessage message) {
		int messageIndex = messageQueue.indexOf(message);
		if (messageIndex > -1) {
			messageQueue.remove(messageIndex);
		}
		messageQueue.add(message);
	}

	/**
	 * Releases the queue and sends all messages one by one
	 */
	public void releaseQueue() {
		releaseQueue(IS_BATCH_SEND_SUPPORTED);
	}

	protected void releaseQueue(boolean sendInBatch) {
		if (queueProcessingStarted) {
			return; // don't re-start the queue if it's already in process
		}

		if (messageQueue.size() > 0) {
			if (sendInBatch) {
				// TODO: implement batch sending support
				// ArrayList<String> batch = new ArrayList<String>();
				// for (NetworkMessage message : messageQueue) {
				// batch.add(message.asJson());
				// }
				// sendMessage(prepareBatchMessage(batch));
			} else {
				onQueueStart();
				sendNextMessage();
			}
		}
	}

	private class NetworkThread extends AsyncTask<Void, Void, NetworkResponse> {

		private NetworkMessage request;

		public NetworkThread(NetworkMessage request) {
			this.request = request;
		}

		@Override
		protected void onPreExecute() {
			onNetworkSendStart(request);
			super.onPreExecute();
		}

		@Override
		protected NetworkResponse doInBackground(Void... params) {
			NetworkResponse response = null;
			if (NETWORK_MANAGER_CORE == NetworkManagerCore.APACHE) {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, NETWORK_TIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParameters, NETWORK_TIMEOUT);
				HttpProtocolParams.setUseExpectContinue(httpParameters, false);
				HttpClient client = new DefaultHttpClient(httpParameters);
				HttpResponse httpResponse = null;
				boolean isRunning = true;
				try {
					HttpRequestBase httpRequest = request.getHttpRequest();
					httpResponse = client.execute(httpRequest);
					if (pDebug.logging) {
						Log.d(getClass().getName(),
								String.format("NetworkRequest: %s %s", request.getMethod(), httpRequest.getURI()));
					}
					response = NetworkResponseFactory.getNetworkResponse();
					((ApacheHttpClientResponse) response).setResponseArguments(httpResponse);
					((ApacheHttpClientResponse) response).obtainResponseStream();
				} catch (Exception e) {
					e.printStackTrace();
					isRunning = false;
				}

			} else if (NETWORK_MANAGER_CORE == NetworkManagerCore.HTTPURLCONNECTION) {
				final HttpURLConnection conn = (HttpURLConnection) request.getHttpURLConnection();
				;
				final Timer timer = new Timer();
				try {
					if (pDebug.logging) {
						Log.d(getClass().getName(),
								String.format("NetworkRequest: %s %s", request.getMethod(), conn.getURL()));
					}
					conn.setReadTimeout(NETWORK_TIMEOUT);
					conn.setConnectTimeout(NETWORK_TIMEOUT);
					onNetworkSendProgress(request);
					response = NetworkResponseFactory.getNetworkResponse();
					// Apparently it seems like some servers may stuck:
					// http://thushw.blogspot.hu/2010/10/java-urlconnection-provides-no-fail.html
					// To stop connection we have to run a timer for NETWORK_TIMEOUT and stop connection manually
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							if (timer != null) {
								timer.cancel();
							}
							if (conn != null) {
								conn.disconnect();
							}
						}
					}, NETWORK_TIMEOUT);
					BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
					byte[] body = readStream(in);
					((HttpUrlConnectionResponse) response).setResponseArguments(conn.getResponseCode(),
							conn.getHeaderFields(), body);
				} catch (Exception e) {
					try {
						((HttpUrlConnectionResponse) response).setResponseArguments(conn.getResponseCode(),
								conn.getHeaderFields(), null);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				} finally {
					if (timer != null) {
						timer.cancel();
					}
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(NetworkResponse result) {
			if (result != null) {

				if (pDebug.logging) {
					HttpResponseCache cache = HttpResponseCache.getInstalled();
					if (cache != null) {
						int total = cache.getRequestCount();
						int network = cache.getNetworkCount();
						int hit = cache.getHitCount();
						Log.d(getClass().getName(), String.format(
								"NetworkManager::HttpResponseCache Total/Network/Cache %d/%d/%d", total, network, hit));
					}
				}

				if (result.getStatus() == HttpStatus.SC_OK) {
					onNetworkSendSuccess(request, result);
				} else {
					onNetworkSendFailure(request, result);
				}
			}
			super.onPostExecute(result);
		}

	}

	protected void sendMessage(final NetworkMessage message) {
		mainNetworkThread = new NetworkThread(message);
		mainNetworkThread.execute();
	}

	protected void sendNextMessage() {
		if (messageQueue.size() > 0 && failuresCount < MAX_FAILURES) {
			NetworkMessage message = messageQueue.get(0);
			messageQueue.remove(0);
			sendMessage(message);
		} else if (failuresCount >= MAX_FAILURES) {
			failuresCount = 0;
			onQueueFailed();
		} else {
			failuresCount = 0;
			onQueueFinished();
		}
	}

	public void onNetworkSendSuccess(NetworkMessage message, NetworkResponse response) {
		// if message succeeded just remove it from queue
		messageQueue.remove(message);

		for (NetworkListener networkListener : listeners) {
			networkListener.requestSuccess(message, response);
		}
		mainNetworkThread = null;

		sendNextMessage();
	}

	public void onNetworkSendFailure(NetworkMessage message, NetworkResponse response) {
		onNetworkSendFailure(message, response, null);
	}

	public void onNetworkSendFailure(NetworkMessage message, NetworkResponse response, Exception e) {
		for (NetworkListener networkListener : listeners) {
			networkListener.requestFail(message, response);
		}

		if (!message.shouldDeleteOnFailure()) {
			// add failed message back to queue
			messageQueue.add(message);
			failuresCount++;
		}

		mainNetworkThread = null;
		sendNextMessage();
	}

	public void onNetworkSendStart(NetworkMessage message) {
		for (NetworkListener networkListsner : listeners) {
			networkListsner.requestStart(message);
		}
	}

	public void onNetworkSendProgress(NetworkMessage message) {
		for (NetworkListener networkListsner : listeners) {
			networkListsner.requestProgress(message);
		}
	}

	public void onQueueStart() {
		queueProcessingStarted = true;
		for (NetworkListener networkListsner : listeners) {
			networkListsner.queueStart();
		}
	}

	public void onQueueFinished() {
		queueProcessingStarted = false;
		for (NetworkListener networkListsner : listeners) {
			networkListsner.queueFinish();
		}
	}

	public void onQueueFailed() {
		queueProcessingStarted = false;
		for (NetworkListener networkListsner : listeners) {
			networkListsner.queueFailed();
		}
	}

	public void close() {
		clearListeners();
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	// utilities
	private static byte[] readStream(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int count = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		while ((count = in.read(buf)) != -1)
			out.write(buf, 0, count);
		return out.toByteArray();
	}
}
