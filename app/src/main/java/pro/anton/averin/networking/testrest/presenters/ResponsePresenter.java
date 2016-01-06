package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;

@Singleton
public class ResponsePresenter extends BasePresenterImpl<ResponseView> {

    @Inject
    public ResponsePresenter(BaseContext baseContext) {
        super(baseContext);
    }
}
