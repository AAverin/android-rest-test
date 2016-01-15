package pro.anton.averin.networking.testrest.rx;

import pro.anton.averin.networking.testrest.resolution.Resolution;
import rx.Observer;

public abstract class ResolvedObserver<T> implements Observer<T> {

    private final Resolution resolution;

    public ResolvedObserver(Resolution resolution) {
        this.resolution = resolution;
    }

    @Override
    public void onCompleted() {
        resolution.success();
    }

    @Override
    public void onError(Throwable e) {
        resolution.handle(e);
    }
}
