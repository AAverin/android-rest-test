package pro.anton.averin.networking.testrest.views.base;

import pro.anton.averin.networking.testrest.presenters.BasePresenterImpl;
import pro.anton.averin.networking.testrest.presenters.BaseView;

public abstract class BaseViewPresenterViewpagerFragment<P extends BasePresenterImpl> extends BaseFragment implements BaseView {

    PresenterViewLinker<P> linker;
    private boolean deferredSetVisibleHintValue;

    protected void initializePresenter(P presenter, BaseView baseView) {
        linker = new PresenterViewLinker<>(presenter, baseView);
        linker.onCreate();
        if (deferredSetVisibleHintValue) {
            linker.onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (linker == null) {
            deferredSetVisibleHintValue = isVisibleToUser;
            return;
        }
        deferSetUserVisibleHint(isVisibleToUser);
    }

    private void deferSetUserVisibleHint(final boolean isVisibleToUser) {
        if (isVisibleToUser) {
            linker.onVisible();
        } else {
            linker.onHidden();
        }
        deferredSetVisibleHintValue = isVisibleToUser;
    }

    @Override
    public void onResume() {
        super.onResume();
        linker.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        linker.onPause();
    }

    @Override
    public void onDestroy() {
        if (linker != null) {
            linker.onDestroy();
        }
        super.onDestroy();
    }
}
