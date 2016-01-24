package pro.anton.averin.networking.testrest.views.base;

import android.os.Bundle;

import pro.anton.averin.networking.testrest.presenters.BasePresenter;

public abstract class PActivity<P extends BasePresenter> extends BaseActivity {

    private P presenter;

    public void initPresenter(P presenter, final Bundle savedInstanceState) {
        this.presenter = presenter;
        presenter.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        presenter.onVisible();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
        presenter.onHidden();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
