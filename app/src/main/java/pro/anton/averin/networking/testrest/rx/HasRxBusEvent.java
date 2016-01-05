package pro.anton.averin.networking.testrest.rx;

import pro.anton.averin.networking.testrest.rx.events.RxBusEvent;

public interface HasRxBusEvent {
    void onEvent(RxBusEvent event);
}

