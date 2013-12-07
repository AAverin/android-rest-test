package aaverin.android.net;

import aaverin.android.net.NetworkManager.NetworkManagerCore;

public class NetworkResponseFactory {
    
    public NetworkResponseFactory() {
        // TODO Auto-generated constructor stub
    }
    
    public static NetworkResponse getNetworkResponse() {
        if (NetworkManager.NETWORK_MANAGER_CORE == NetworkManagerCore.APACHE) {
            return new ApacheHttpClientResponse();
        }
        
        if (NetworkManager.NETWORK_MANAGER_CORE == NetworkManagerCore.HTTPURLCONNECTION) {
            return new HttpUrlConnectionResponse();
        }
        return null;
    }
    
}
