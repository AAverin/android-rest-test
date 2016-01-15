package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.BaseContext;

public class BasePresenterImpl<B extends BaseView> implements BasePresenter<B> {

    protected final BaseContext baseContext;
    protected B view = null;
    protected boolean isVisible;

    @Inject
    public BasePresenterImpl(BaseContext baseContext) {
        this.baseContext = baseContext;
    }

    @Override
    public void setView(B view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        isVisible = false;
        //to add behaviour on onCreate, override this method
    }

    @Override
    public void onResume() {
        //to add behaviour on onResume, override this method
    }

    @Override
    public void onPause() {
        //to add behaviour on onPause, override this method
    }

    @Override
    public void onDestroy() {
        //to add behaviour on onDestroy, override this method
    }

    @Override
    public void onVisible() {
        isVisible = true;
        //to add behaviour on onVisible, override this method
    }

    @Override
    public void onHidden() {
        isVisible = false;
        //to add behaviour on onHidden, override this method
    }
}
