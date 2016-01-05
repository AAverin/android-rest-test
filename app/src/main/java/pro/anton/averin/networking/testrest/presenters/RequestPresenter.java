package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.BaseContext;

public class RequestPresenter extends BasePresenterImpl<RequestView> {

    @Inject
    public RequestPresenter(BaseContext baseContext) {
        super(baseContext);
    }
}
