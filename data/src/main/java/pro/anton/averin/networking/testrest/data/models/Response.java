package pro.anton.averin.networking.testrest.data.models;

import java.util.List;
import java.util.Map;

public class Response {
    public int status;
    public String method;
    public String url;
    public Map<String, List<String>> headers;
    public String body;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (status != response.status) return false;
        if (method != null ? !method.equals(response.method) : response.method != null)
            return false;
        if (url != null ? !url.equals(response.url) : response.url != null) return false;
        if (headers != null ? !headers.equals(response.headers) : response.headers != null)
            return false;
        return !(body != null ? !body.equals(response.body) : response.body != null);

    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
