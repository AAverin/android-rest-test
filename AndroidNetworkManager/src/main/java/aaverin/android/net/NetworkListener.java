/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * A generic network listener
 * Methods basically do what they read
 */

package aaverin.android.net;

public interface NetworkListener {
    public void queueStart();
    public void queueFinish();
    public void queueFailed();
    public void requestStart(NetworkMessage message);
    public void requestSuccess(NetworkMessage message, NetworkResponse response);
    public void requestFail(NetworkMessage message, NetworkResponse response);
    public void requestProgress(NetworkMessage message);
}
