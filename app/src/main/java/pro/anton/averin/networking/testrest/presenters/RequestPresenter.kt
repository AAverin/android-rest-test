package pro.anton.averin.networking.testrest.presenters

import okhttp3.Response
import pro.anton.averin.networking.testrest.data.Repository
import pro.anton.averin.networking.testrest.data.models.Request
import pro.anton.averin.networking.testrest.rx.ResolvedObserver
import pro.anton.averin.networking.testrest.rx.RxSchedulers
import pro.anton.averin.networking.testrest.rx.events.DimBackgroundEvent
import pro.anton.averin.networking.testrest.rx.events.FabClickedEvent
import pro.anton.averin.networking.testrest.rx.events.UndimBackgroundEvent
import javax.inject.Inject

class RequestPresenter
@Inject
constructor() : RxBusPresenter<RequestView>() {

    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var schedulers: RxSchedulers

    fun onUseFileCheckboxChecked(isChecked: Boolean) {
        view.cleanPostBody()
        if (isChecked) {
            view.showPickFileButton()
            view.setPostBodyFileHint()
        } else {
            view.hidePickFileButton()
            view.setPostBodyDefaultHint()
        }
    }

    fun onPickFileButtonClicked() {
        view.showFileChooser()
    }

    fun onAddQueryButtonClicked() {
        view.showAddQueryPopup()
    }

    fun onAddHeadersButtonClicked() {
        view.showAddHeaderPopup()
    }

    fun onSendClicked(request: Request) {
        if (request.isValid) {
            repository.sendRequest(request).observeOn(schedulers.androidMainThread()).subscribe(object : ResolvedObserver<Response>(
                    view.uiResolution) {
                override fun onCompleted() {
                    super.onCompleted()
                    view.navigateToResponseScreen()
                }

                override fun onNext(s: Response) {
                    llogger.log(this, s.message())
                }
            })
        } else {
            view.focusBaseUrl()
        }
    }

    fun onSaveItemClicked() {
        view.navigateToManagerScreenForSave()
    }

    fun onManagerItemClicked() {
        view.navigateToManagerScreen()
    }

    fun onClearItemClicked() {
        view.clearFields()
    }

    fun onPostMethodSelected() {
        view.showPostLayout()
    }

    fun onPostMethodUnselected() {
        view.hidePostLayout()
    }

    fun addQueryPopupDismissed() {
        requestUnDimBackground()
    }

    fun requestUnDimBackground() {
        globalBus.post(UndimBackgroundEvent())
    }

    fun requestDimBackground() {
        globalBus.post(DimBackgroundEvent())
    }

    override fun onEvent(event: Any) {
        if (event is FabClickedEvent && visible) {
            onSendClicked(view.request)
        }
    }
}
