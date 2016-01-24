package pro.anton.averin.networking.testrest.views.base;

import android.os.Bundle;

import pro.anton.averin.networking.testrest.presenters.BasePresenter;
import pro.anton.averin.networking.testrest.presenters.BaseView;

public abstract class ViewpagerPFragment<P extends BasePresenter> extends PFragment<P> implements BaseView {

    private boolean deferredSetVisibleHintValue;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (deferredSetVisibleHintValue) {
            presenter.onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (presenter == null) {
            deferredSetVisibleHintValue = isVisibleToUser;
            return;
        }
        deferSetUserVisibleHint(isVisibleToUser);
    }

    private void deferSetUserVisibleHint(final boolean isVisibleToUser) {
        if (isVisibleToUser) {
            presenter.onVisible();
        } else {
            presenter.onHidden();
        }
        deferredSetVisibleHintValue = isVisibleToUser;
    }
}
