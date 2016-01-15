package pro.anton.averin.networking.testrest.resolution;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.utils.LLogger;

@Singleton
public class LogResolution implements Resolution {

    @Inject
    LLogger llogger;

    @Inject
    public LogResolution() {

    }

    @Override
    public void handle(Throwable t) {
        llogger.log_e(this, t);
    }

    @Override
    public void success() {

    }
}
