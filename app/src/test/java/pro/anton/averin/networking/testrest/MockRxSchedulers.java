package pro.anton.averin.networking.testrest;

import pro.anton.averin.networking.testrest.rx.RxSchedulers;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class MockRxSchedulers extends RxSchedulers {

    @Override
    public Scheduler androidMainThread() {
        return Schedulers.immediate();
    }
}
