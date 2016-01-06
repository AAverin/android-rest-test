package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Storage;

@Singleton
public class ResponsePresenter extends BasePresenterImpl<ResponseView> {

    @Inject
    Storage storage;
    @Inject
    public ResponsePresenter(BaseContext baseContext) {
        super(baseContext);
    }

    @Override
    public void onVisible() {
        super.onVisible();

        if (storage.getCurrentRequest() != null && storage.getCurrentResponse() == null) {
            //send request again
            view.hideNoDataLayout();
            view.showProgressBar();
            view.hideResponseLayout();
        } else if (storage.getCurrentResponse() != null) {
            //show response
            view.updateShareIntent(storage.getCurrentResponse(), storage.getCurrentRequest());
            view.hideNoDataLayout();
            view.hideProgressBar();
            view.showResponseLayout();
        }
    }
}
