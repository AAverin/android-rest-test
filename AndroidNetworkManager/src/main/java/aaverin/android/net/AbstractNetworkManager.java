/**
 * @author Anton Averin <a.a.averin@gmail.com>
 * 
 * Basic Network Manager abstraction - interface for possible implementations
 */

package aaverin.android.net;

public abstract class AbstractNetworkManager {
	
	public abstract void subscribe(NetworkListener listener);
	public abstract void unsubscribe(NetworkListener listener);
	public abstract boolean isSubscribed(NetworkListener listener);
	public abstract void clearListeners();
	
	public abstract void putMessage(NetworkMessage message);
		
	public abstract void releaseQueue();
	
	protected abstract void sendMessage(final NetworkMessage message);
	
	protected abstract void sendNextMessage();
	
	public abstract void close();
	
}
