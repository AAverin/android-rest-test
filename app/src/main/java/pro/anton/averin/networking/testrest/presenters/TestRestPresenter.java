package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;

@Singleton
public class TestRestPresenter extends BasePresenterImpl<TestRestView> {

    @Inject
    public TestRestPresenter(BaseContext baseContext) {
        super(baseContext);
    }

    public void undim() {
        view.undim();
    }
}
