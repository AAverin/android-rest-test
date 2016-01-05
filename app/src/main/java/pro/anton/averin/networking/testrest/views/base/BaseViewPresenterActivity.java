package pro.anton.averin.networking.testrest.views.base;


import pro.anton.averin.networking.testrest.presenters.BasePresenterImpl;
import pro.anton.averin.networking.testrest.presenters.BaseView;

public class BaseViewPresenterActivity<P extends BasePresenterImpl> extends BaseActivity implements BaseView {

    PresenterViewLinker<P> linker;

    protected void initializePresenter(P presenter, BaseView baseView) {
        linker = new PresenterViewLinker<>(presenter, baseView);
        linker.onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        linker.onResume();
        linker.onVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        linker.onPause();
        linker.onHidden();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (linker != null) {
            linker.onDestroy();
        }
    }
}
