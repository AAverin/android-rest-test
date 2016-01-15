package pro.anton.averin.networking.testrest.resolution;

import java.net.ConnectException;

public abstract class BaseResolution extends FusedResolution {

    @Override
    public void handle(Throwable t) {
        if (t instanceof ConnectException) {
            handle((ConnectException) t);
        }
    }

}
