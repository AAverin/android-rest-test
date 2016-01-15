package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.rx.HasRxBusEvent;
import pro.anton.averin.networking.testrest.rx.RxBus;
import pro.anton.averin.networking.testrest.rx.events.RxBusEvent;
import pro.anton.averin.networking.testrest.utils.LLogger;

public class RxBusPresenter<B extends BaseView> extends BasePresenterImpl<B> implements HasRxBusEvent {

    @Inject
    RxBus rxBus;
    @Inject
    LLogger llogger;

    public RxBusPresenter(BaseContext baseContext) {
        super(baseContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            rxBus.subscribe(this);
        }
    }

    @Override
    public void onVisible() {
        super.onVisible();
        rxBus.subscribe(this);
    }

    @Override
    public void onHidden() {
        super.onHidden();
        rxBus.unsubscribe(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        rxBus.unsubscribe(this);
    }

    @Override
    public void onEvent(RxBusEvent event) {

    }
}
