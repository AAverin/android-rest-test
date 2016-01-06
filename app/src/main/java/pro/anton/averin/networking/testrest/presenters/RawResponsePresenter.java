package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Storage;

@Singleton
public class RawResponsePresenter extends BasePresenterImpl<RawResponseView> {

    @Inject
    Storage storage;

    @Inject
    public RawResponsePresenter(BaseContext baseContext) {
        super(baseContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        view.update(storage.getCurrentResponse());
    }
}
