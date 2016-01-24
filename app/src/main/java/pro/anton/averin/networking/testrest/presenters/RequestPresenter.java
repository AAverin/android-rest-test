package pro.anton.averin.networking.testrest.presenters;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import okhttp3.Response;
import pro.anton.averin.networking.testrest.data.Repository;
import pro.anton.averin.networking.testrest.data.models.Request;
import pro.anton.averin.networking.testrest.rx.ResolvedObserver;
import pro.anton.averin.networking.testrest.rx.RxSchedulers;
import pro.anton.averin.networking.testrest.rx.events.DimBackgroundEvent;
import pro.anton.averin.networking.testrest.rx.events.FabClickedEvent;
import pro.anton.averin.networking.testrest.rx.events.UndimBackgroundEvent;
import pro.anton.averin.networking.testrest.utils.LLogger;

public class RequestPresenter extends RxBusPresenter<RequestView> {

    @Inject
    Repository repository;
    @Inject
    RxSchedulers schedulers;
    @Inject
    LLogger llogger;

    @Inject
    public RequestPresenter() {
    }

    public void onUseFileCheckboxChecked(boolean isChecked) {
        getView().cleanPostBody();
        if (isChecked) {
            getView().showPickFileButton();
            getView().setPostBodyFileHint();
        } else {
            getView().hidePickFileButton();
            getView().setPostBodyDefaultHint();
        }
    }

    public void onPickFileButtonClicked() {
        getView().showFileChooser();
    }

    public void onAddQueryButtonClicked() {
        getView().showAddQueryPopup();
    }

    public void onAddHeadersButtonClicked() {
        getView().showAddHeaderPopup();
    }

    public void onSendClicked(Request request) {
        if (request.isValid()) {
            repository.sendRequest(request).observeOn(schedulers.androidMainThread()).subscribe(new ResolvedObserver<Response>(
                                                                                                        getView().getUiResolution()) {
                @Override
                public void onCompleted() {
                    super.onCompleted();
                    view.navigateToResponseScreen();
                }

                @Override
                public void onNext(Response s) {
                    llogger.log(this, s.message());
                }
            });
        } else {
            getView().focusBaseUrl();
        }
    }

    public void onSaveItemClicked() {
        view.navigateToManagerScreenForSave();
    }

    public void onManagerItemClicked() {
        view.navigateToManagerScreen();
    }

    public void onClearItemClicked() {
        getView().clearFields();
    }

    public void onPostMethodSelected() {
        getView().showPostLayout();
    }

    public void onPostMethodUnselected() {
        getView().hidePostLayout();
    }

    public void addQueryPopupDismissed() {
        requestUnDimBackground();
    }

    public void requestUnDimBackground() {
        getGlobalBus().post(new UndimBackgroundEvent());
    }

    public void requestDimBackground() {
        getGlobalBus().post(new DimBackgroundEvent());
    }

    @Override
    protected void onEvent(@NotNull final Object event) {
        if (event instanceof FabClickedEvent && getVisible()) {
            onSendClicked(getView().getRequest());
        }
    }
}
