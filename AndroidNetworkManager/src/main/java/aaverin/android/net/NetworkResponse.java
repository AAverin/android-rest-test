/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * Abstract respone.
 * See for implementation in ApacheHttpClientResponse and in HttpUrlConnectionResponse
 */


package aaverin.android.net;

public abstract class NetworkResponse {

	/**
	 * @return HTTP status
	 */
    public abstract int getStatus();
    
    /**
     * implementation-specifics for obtaining response streams
     */
    public abstract void obtainResponseStream();
    
    /**
     * 
     * @return response body as a String
     * @throws NetworkResponseProcessException thrown if body is null for response
     */
    public abstract String getResponseBody() throws NetworkResponseProcessException;
}
