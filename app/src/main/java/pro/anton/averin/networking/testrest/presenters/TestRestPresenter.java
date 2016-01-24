package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.rx.RxBus;
import pro.anton.averin.networking.testrest.rx.events.FabClickedEvent;
import pro.anton.averin.networking.testrest.rx.events.NaviResponseScreenEvent;

@Singleton
public class TestRestPresenter extends RxBusPresenter<TestRestView> {

    @Inject
    RxBus rxBus;

    @Inject
    public TestRestPresenter(BaseContext baseContext) {
        super(baseContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rxBus.subscribe(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxBus.unsubscribe(this);
    }

    public void undim() {
        view.undim();
    }

    @Override
    public void onEvent(RxBusEvent event) {
        if (event instanceof NaviResponseScreenEvent) {
            view.naviResponseScreen();
        }
    }

    public void onFabClicked() {
        rxBus.send(new FabClickedEvent());
    }
}
