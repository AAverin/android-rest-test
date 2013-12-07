/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * A cached network manager implementation
 * Basicall it's a wrapper over NetworkManager, that can enable HttpCache and wraps sendMessage method implementation
 * HttpCache is implemented using HttpResponseCache Android port (https://github.com/candrews/HttpResponseCache)
 */

package aaverin.android.net;

import java.io.File;
import java.util.ArrayList;

public class CachedNetworkManager extends AbstractNetworkManager implements NetworkListener {

	private static CachedNetworkManager instance = null;

	private NetworkManager networkManagerInstance = null;

	private CachedNetworkManager() {
		networkManagerInstance = NetworkManager.getInstance();
		networkManagerInstance.subscribe(this);
	}

	public static CachedNetworkManager getInstance() {
		if (instance == null) {
			instance = new CachedNetworkManager();
		}
		return instance;
	}
	
	public void enableHttpCache(File httpCacheDir, long httpCacheSize) {
		networkManagerInstance.enableHttpCache(httpCacheDir, httpCacheSize);
	}

	private ArrayList<NetworkListener> listeners = new ArrayList<NetworkListener>();

	public void subscribe(NetworkListener listener) {
		listeners.add(listener);
	}

	public void unsubscribe(NetworkListener listener) {
		listeners.remove(listener);
	}

	public boolean isSubscribed(NetworkListener listener) {
		return listeners.contains(listener);
	}

	public void clearListeners() {
		listeners.clear();
	}

	@Override
	public void putMessage(NetworkMessage message) {
		networkManagerInstance.putMessage(message);
	}

	@Override
	public void releaseQueue() {
		networkManagerInstance.releaseQueue();
	}
	
	public void releaseQueue(boolean sendInBatch) {
		networkManagerInstance.releaseQueue();
	}

	@Override
	protected void sendMessage(NetworkMessage message) {
		/**
		 * Determine what network type is used and what is the message
		 * If message has high priority - send it through via networkManager
		 * If message is forced - send it through
		 * If we are using WiFi and policy is to download all on WiFi - send it through
		 * If we are not on WiFi - try to fetch data from DB and if it's there - don't use network
		 */
		
		networkManagerInstance.sendMessage(message);
	}

	@Override
	protected void sendNextMessage() {
		networkManagerInstance.sendNextMessage();
	}

	@Override
	public void close() {
		networkManagerInstance.close();
	}

	@Override
	public void queueStart() {
		for (NetworkListener networkListsner : listeners) {
			networkListsner.queueStart();
		}
	}

	@Override
	public void queueFinish() {
		for (NetworkListener networkListsner : listeners) {
			networkListsner.queueFinish();
		}
	}

	@Override
	public void queueFailed() {
		for (NetworkListener networkListsner : listeners) {
			networkListsner.queueFailed();
		}
	}

	@Override
	public void requestStart(NetworkMessage message) {
		for (NetworkListener networkListsner : listeners) {
			networkListsner.requestStart(message);
		}
	}

	@Override
	public void requestSuccess(NetworkMessage message, NetworkResponse response) {
		for (NetworkListener networkListener : listeners) {
			networkListener.requestSuccess(message, response);
		}
	}

	@Override
	public void requestFail(NetworkMessage message, NetworkResponse response) {
		for (NetworkListener networkListener : listeners) {
			networkListener.requestFail(message, response);
		}
	}

	@Override
	public void requestProgress(NetworkMessage message) {
		for (NetworkListener networkListsner : listeners) {
			networkListsner.requestProgress(message);
		}
	}
}
