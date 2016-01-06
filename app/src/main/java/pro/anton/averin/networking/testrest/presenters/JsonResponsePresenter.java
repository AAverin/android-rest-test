package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Storage;

@Singleton
public class JsonResponsePresenter extends BasePresenterImpl<JsonResponseView> {

    @Inject
    Storage storage;

    @Inject
    public JsonResponsePresenter(BaseContext baseContext) {
        super(baseContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        view.update(storage.getCurrentResponse());
    }
}
