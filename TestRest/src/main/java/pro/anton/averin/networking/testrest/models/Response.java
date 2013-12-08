package pro.anton.averin.networking.testrest.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AAverin on 07.12.13.
 */
public class Response {
    public int status;
    public String method;
    public String url;
    public Map<String, List<String>> headers;
    public String body;
}
