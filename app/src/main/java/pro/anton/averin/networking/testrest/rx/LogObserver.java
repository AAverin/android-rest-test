package pro.anton.averin.networking.testrest.rx;

import pro.anton.averin.networking.testrest.utils.LLogger;
import rx.Observer;

public abstract class LogObserver<T> implements Observer<T> {

    private final LLogger llogger;

    public LogObserver(LLogger llogger) {
        this.llogger = llogger;
    }

    @Override
    public void onError(Throwable e) {
        llogger.log_e(this, e);
    }

}
