package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;

import okhttp3.Response;
import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Repository;
import pro.anton.averin.networking.testrest.data.models.Request;
import pro.anton.averin.networking.testrest.navigation.Navigator;
import pro.anton.averin.networking.testrest.rx.RxBus;
import pro.anton.averin.networking.testrest.rx.RxSchedulers;
import pro.anton.averin.networking.testrest.rx.events.DimBackgroundEvent;
import pro.anton.averin.networking.testrest.rx.events.UndimBackgroundEvent;
import rx.Subscriber;

public class RequestPresenter extends BasePresenterImpl<RequestView> {

    @Inject
    RxBus rxBus;
    @Inject
    Navigator navigator;
    @Inject
    Repository repository;
    @Inject
    RxSchedulers schedulers;

    @Inject
    public RequestPresenter(BaseContext baseContext) {
        super(baseContext);
    }

    public void onUseFileCheckboxChecked(boolean isChecked) {
        view.cleanPostBody();
        if (isChecked) {
            view.showPickFileButton();
            view.setPostBodyFileHint();
        } else {
            view.hidePickFileButton();
            view.setPostBodyDefaultHint();
        }
    }

    public void onPickFileButtonClicked() {
        view.showFileChooser();
    }

    public void onAddQueryButtonClicked() {
        view.showAddQueryPopup();
    }

    public void onAddHeadersButtonClicked() {
        view.showAddHeaderPopup();
    }

    public void onSendClicked(Request request) {
        if (request.isValid()) {
            repository.sendRequest(request).subscribeOn(schedulers.androidMainThread()).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {
                    navigator.navigateToResponseScreen();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Response s) {

                }
            });
        } else {
            view.focusBaseUrl();
        }
    }

    public void onSaveItemClicked() {
        navigator.navigateToManagerScreenForSave();
    }

    public void onManagerItemClicked() {
        navigator.navigateToManagerScreen();
    }

    public void onClearItemClicked() {
        view.clearFields();
    }

    public void onPostMethodSelected() {
        view.showPostLayout();
    }

    public void onPostMethodUnselected() {
        view.hidePostLayout();
    }

    public void addQueryPopupDismissed() {
        requestUnDimBackground();
    }

    public void requestUnDimBackground() {
        rxBus.send(new UndimBackgroundEvent());
    }

    public void requestDimBackground() {
        rxBus.send(new DimBackgroundEvent());
    }
}
