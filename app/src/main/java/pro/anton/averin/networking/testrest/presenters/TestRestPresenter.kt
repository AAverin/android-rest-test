package pro.anton.averin.networking.testrest.presenters

import pro.anton.averin.networking.testrest.rx.events.FabClickedEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRestPresenter
@Inject
constructor() : RxBusPresenter<TestRestView>() {

    fun onNavigateToManagerScreenWithSave() {
        view.navigateToManagerScreenWithSave();
    }

    fun onNavigateToManagerScreenWithoutSave() {
        view.navigateToManagerScreenWithoutSave();
    }

    fun onNavigateToResponseScreen() {
        view.navigateToResponseScreen();
    }

    fun undim() {
        view.undim()
    }

    override fun onEvent(event: Any) {

    }

    fun onFabClicked() {
        globalBus.post(FabClickedEvent())
    }
}
