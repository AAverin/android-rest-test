package pro.anton.averin.networking.testrest.views.base;

import android.os.Bundle;

import pro.anton.averin.networking.testrest.presenters.BasePresenter;

public abstract class PFragment<P extends BasePresenter> extends BaseFragment {

    protected P presenter;

    public void initPresenter(P presenter, final Bundle savedInstanceState) {
        this.presenter = presenter;
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
