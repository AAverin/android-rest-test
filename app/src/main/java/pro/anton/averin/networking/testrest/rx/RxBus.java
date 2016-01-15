package pro.anton.averin.networking.testrest.rx;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.rx.events.RxBusEvent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Singleton
public class RxBus {

    private final Subject<RxBusEvent, RxBusEvent> _bus = new SerializedSubject<>(PublishSubject.<RxBusEvent>create());
    private List<HasRxBusEvent> handlers = new CopyOnWriteArrayList<>();

    @Inject
    public RxBus() {
        toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RxBusEvent>() {
            @Override
            public void call(RxBusEvent rxBusEvent) {
                notifyHandlers(rxBusEvent);
            }
        });
    }

    public void send(RxBusEvent o) {
        _bus.onNext(o);
    }

    public Observable<RxBusEvent> toObservable() {
        return _bus;
    }

    public void unsubscribe(final HasRxBusEvent handler) {
        handlers.remove(handler);
    }

    public void subscribe(final HasRxBusEvent handler) {
        if (handlers.contains(handler)) {
            return;
        }

        handlers.add(handler);
    }

    private void notifyHandlers(RxBusEvent event) {
        if (handlers.size() != 0) {
            for (HasRxBusEvent handler : handlers) {
                handler.onEvent(event);
            }
        }
    }

}
