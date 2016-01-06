package pro.anton.averin.networking.testrest.rx;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@Singleton
public class RxSchedulers {

    @Inject
    public RxSchedulers() {

    }

    public Scheduler androidMainThread() {
        return AndroidSchedulers.mainThread();
    }
}
