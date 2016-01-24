package pro.anton.averin.networking.testrest.presenters

import pro.anton.averin.networking.testrest.rx.events.FabClickedEvent
import pro.anton.averin.networking.testrest.rx.events.NaviResponseScreenEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRestPresenter
@Inject
constructor() : RxBusPresenter<TestRestView>() {

    fun undim() {
        view!!.undim()
    }

    override fun onEvent(event: Any) {
        if (event is NaviResponseScreenEvent) {
            view!!.naviResponseScreen()
        }
    }

    fun onFabClicked() {
        globalBus.post(FabClickedEvent())
    }
}
