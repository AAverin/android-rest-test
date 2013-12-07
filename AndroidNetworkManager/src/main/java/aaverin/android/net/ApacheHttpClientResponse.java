/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * Apache Http Client implementation for NetworkResponse
 */

package aaverin.android.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;

public class ApacheHttpClientResponse extends NetworkResponse {
	
    //for ApacheHttpClient implementation
    private HttpResponse httpResponse = null;
    private ByteArrayOutputStream outputStream = null;
    
    public ApacheHttpClientResponse() {
        
    }
    
    public void setResponseArguments(HttpResponse httpResponse) {
        this.setHttpResponse(httpResponse);
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
    
    @Override
    public int getStatus() {
        return httpResponse.getStatusLine().getStatusCode();
    }
    
    public void obtainResponseStream() {
    	outputStream = new ByteArrayOutputStream();
        try {
            httpResponse.getEntity().writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream getResponseStream() {
        return outputStream;
    }

    @Override
    public String getResponseBody() {
        ByteArrayOutputStream os = getResponseStream();
        if (os == null) {
        	return null;
        }
        return new String(os.toByteArray());
    }
}
