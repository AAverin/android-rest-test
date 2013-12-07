package aaverin.android.net;

public interface OnNetworkSendListener {
    public void onQueueStart();
    public void onQueueFinished();
    public void onQueueFailed();
    public void onNetworkSendStart(NetworkMessage message);
    public void onNetworkSendSuccess(NetworkMessage message, NetworkResponse response);
    public void onNetworkSendProgress(NetworkMessage message);
    public void onNetworkSendFailure(NetworkMessage message, NetworkResponse response);
    public void onNetworkSendFailure(NetworkMessage message, NetworkResponse response, Exception e);
}