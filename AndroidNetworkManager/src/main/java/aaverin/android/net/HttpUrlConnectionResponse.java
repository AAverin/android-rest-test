/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * HttpUrlConnection imlpementation of NetworkResponse
 */

package aaverin.android.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

public class HttpUrlConnectionResponse extends NetworkResponse {
   
    //for HTTPUrlConneciton implementation
    private int status;
    private Map<String, List<String>> headers;
    private byte[] body;
    
    public HttpUrlConnectionResponse() {
        // TODO Auto-generated constructor stub
    }
    
    public void setResponseArguments(int status, Map<String, List<String>> headers, byte[] body) {
        this.setStatus(status);
        this.setHeaders(headers);
        this.setBody(body);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public String getResponseBody() throws NetworkResponseProcessException {
    	if (body == null) {
    		throw new NetworkResponseProcessException();
    	}
        return new String(body);
    }

    //TODO: check if this part is needed for HttpUrlConnection
	@Override
	public void obtainResponseStream() {
	}
}
