/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * <p/>
 * Apache Http Client implementation for NetworkResponse
 */

package aaverin.android.net;

import org.apache.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ApacheHttpClientResponse extends NetworkResponse {

    //for ApacheHttpClient implementation
    private HttpResponse httpResponse = null;
    private ByteArrayOutputStream outputStream = null;

    public ApacheHttpClientResponse() {

    }

    public void setResponseArguments(HttpResponse httpResponse) {
        this.setHttpResponse(httpResponse);
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
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
