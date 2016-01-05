package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.BaseContext;

public class ResponsePresenter extends BasePresenterImpl<ResponseView> {

    @Inject
    public ResponsePresenter(BaseContext baseContext) {
        super(baseContext);
    }
}
