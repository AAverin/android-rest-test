package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;

@Singleton
public class AddQueryPopupPresenter extends BasePresenterImpl<AddQueryPopupView> {

    @Inject
    public AddQueryPopupPresenter(BaseContext baseContext) {
        super(baseContext);
    }
}
