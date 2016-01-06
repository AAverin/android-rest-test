package pro.anton.averin.networking.testrest.rx;

import pro.anton.averin.networking.testrest.utils.LLogger;
import rx.Subscriber;

public abstract class LogSubscriber<T> extends Subscriber<T> {

    private final LLogger llogger;

    public LogSubscriber(LLogger llogger) {
        this.llogger = llogger;
    }

    @Override
    public void onError(Throwable e) {
        llogger.log_e(this, e);
    }

}
