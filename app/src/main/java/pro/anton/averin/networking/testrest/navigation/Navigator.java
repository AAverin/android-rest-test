package pro.anton.averin.networking.testrest.navigation;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.rx.RxBus;
import pro.anton.averin.networking.testrest.rx.events.NaviManagerScreenEvent;
import pro.anton.averin.networking.testrest.rx.events.NaviResponseScreenEvent;

@Singleton
public class Navigator {

    @Inject
    RxBus rxBus;

    @Inject
    public Navigator() {

    }

    public void navigateToResponseScreen() {
        rxBus.send(new NaviResponseScreenEvent());
    }

    public void navigateToManagerScreenForSave() {
        rxBus.send(NaviManagerScreenEvent.withSave());
    }

    public void navigateToManagerScreen() {
        rxBus.send(NaviManagerScreenEvent.withoutSave());
    }
}
