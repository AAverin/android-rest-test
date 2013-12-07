/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * The main NetworkMessage implementation.
 * NetworkMessage is the object that envelops the logics of POST and GET HTTP methods 
 */


package aaverin.android.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

public class NetworkMessage {
	
	private String method;
	private URI uri;
	private List<NameValuePair> parametersList = null;
	private String rawPostBody = null;
	private boolean isCacheable = true;
	
	private HttpRequestBase request = null;
	
	private boolean deleteOnFailure = false;
	
	/**
	 * 
	 * @param deleteOnFailure delete this message from the queue if it failed
	 */
	public NetworkMessage(boolean deleteOnFailure) {
		this.deleteOnFailure = deleteOnFailure;
	}
	
	public boolean shouldDeleteOnFailure() {
	    return deleteOnFailure;
	}
	
	/**
	 * HTTP method for this NetworkMessage
	 * For now it's POST or GET
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * Remote URI for this NetworkMessage
	 * @param uri
	 */
	public void setURI(URI uri) {
		this.uri = uri;
	}
	
	/**
	 * List of parameters that will be either set as query parameters for GET method, or put into the body of POST method
	 * @param parameters
	 */
	public void setParametersList(List<NameValuePair> parameters) {
		this.parametersList = parameters;
	}
	
	public List<NameValuePair> getParametersList() {
		return this.parametersList;
	}
	
	/**
	 * Raw post body that will be set for this message. Should be used with POST.
	 * Replaces parametersList
	 * @param rawString
	 */
	public void setRawPostBody(String rawString) {
		this.rawPostBody = rawString;
	}
	
	public String getRawPostBody() {
		return this.rawPostBody;
	}
	
	public String getMethod() {
	    return this.method;
	}
	
	public URI getURI() {
	    return this.uri;
	}
	
	/**
	 * Sets if this particular NetworkMessage should be cached by HttpCache
	 * @param cacheable
	 */
	public void setCacheable(boolean cacheable) {
		isCacheable = cacheable;
	}
	
	public boolean isCacheable() {
		return isCacheable;
	}

	/**
	 * Returns an HttpRequest for Apache implementation
	 * WARNING: HttpCaching is not yet implemented for Apache. If you want to use caching please switch to HttpUrlConnection
	 * 
	 * For GET parametersList that was set previously is added to the uri as query parameters.
	 * All parameters are URLEncoded and in UTF-8
	 * 
	 * For POST parameetersList are also added as query parameters, because POST's do actually support that
	 * Also, if there was rawPostBody set it is appended to the body in UTF-8 format
	 * 
	 * @return a Apache request that will be then used by NetworkManager internally
	 */
	public HttpRequestBase getHttpRequest() {
        try {
            if (request == null) {
            	if (this.parametersList == null) {
                	this.parametersList = new ArrayList<NameValuePair>();
                }
            
                if (method.equals("PUT")) {
                    request = new HttpPut(uri);
                } else if (method.equals("POST")) {
                	String url = uri.toString() + "/?" + URLEncodedUtils.format(this.parametersList, "UTF-8").replace("%3A", ":");
                    request = new HttpPost(url);
                    if (this.rawPostBody != null) {
                    	((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(this.rawPostBody, HTTP.UTF_8));
                    }

                } else if (method.equals("GET")) {
                	String url = uri.toString() + "/?" + URLEncodedUtils.format(this.parametersList, "UTF-8").replace("%3A", ":");
                    request = new HttpGet(url);
                }
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return request;
	}
	
	/**
	 * Returns an URLConnection for HttpUrlConnection implementation
	 * 
	 * If NetworkMessage is not cacheable - adds Cache-Control: no-cache HTTP flag
	 * 
	 * For GET parametersList that was set previously is added to the uri as query parameters.
	 * All parameters are URLEncoded and in UTF-8
	 * 
	 * For POST parameetersList are also added as query parameters, because POST's do actually support that
	 * Also, if there was rawPostBody set it is appended to the body in UTF-8 format
	 * 
	 * @return URLConnection - HttpUrlConnection built request for internal use by NetworkManager
	 */
	public URLConnection getHttpURLConnection() {
	    URLConnection connection = null;
	    try {
	    	
	    	if (this.parametersList == null) {
            	this.parametersList = new ArrayList<NameValuePair>();
            }
             
	        String query = URLEncodedUtils.format(this.parametersList, "UTF-8").replace("%3A", ":");
	        
            if (method.equals("GET")) {
                String url = uri.toString() + "/?" + query;
                connection = new URL(url).openConnection();;
                if (!isCacheable()) {
                	connection.addRequestProperty("Cache-Control", "no-cache");
                }
            } else if (method.equals("POST")) {
            	String url = uri.toString() + "/?" + query;
                connection = new URL(url.toString()).openConnection();
                if (!isCacheable()) {
                	connection.addRequestProperty("Cache-Control", "no-cache");
                }
                connection.setDoOutput(true); // Triggers POST.
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");
                OutputStream output = null;
                try {
                     output = connection.getOutputStream();
                     if (this.rawPostBody != null) {
                    	 output.write(this.rawPostBody.getBytes("UTF-8"));
                     } else {
                    	 output.write(query.getBytes("UTF-8"));                    	 
                     }
                } finally {
                     if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
                }
            }
	    } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    return connection;
    }
}
