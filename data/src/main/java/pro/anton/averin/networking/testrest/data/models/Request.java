package pro.anton.averin.networking.testrest.data.models;

import java.util.ArrayList;

import pro.anton.averin.networking.testrest.data.ProtocolType;

/**
 * Created by AAverin on 12.11.13.
 */
public class Request {

    public String name;
    public String protocol;
    public String baseUrl;
    public String method;
    public String queryString;
    public String body;
    public ArrayList<RequestHeader> headers;

    public Request() {
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        toString.append("name:");
        toString.append(name);
        toString.append("protocol");
        toString.append(protocol);
        toString.append("baseUrl:");
        toString.append(baseUrl);
        toString.append("method:");
        toString.append(method);
        toString.append("query:");
        toString.append(queryString);

        return toString.toString();
    }

    public int getProtocolType() {
        if (protocol.toLowerCase().contains("https")) {
            return ProtocolType.HTTPS;
        }
        return ProtocolType.HTTP;
    }

    public String asURI() {
        StringBuilder uri = new StringBuilder();
        uri.append(protocol);
        uri.append(baseUrl.trim());
        uri.append(queryString.trim());
        return uri.toString();
    }

    public boolean isValid() {
        return baseUrl != null && baseUrl.length() > 0;
    }
}
