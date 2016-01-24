package pro.anton.averin.networking.testrest.views.base;

import android.os.Bundle;

import pro.anton.averin.networking.testrest.presenters.BasePresenter;
import pro.anton.averin.networking.testrest.views.activities.ToolbarActivity;

public abstract class ToolbarPActivity<P extends BasePresenter> extends ToolbarActivity {

    private P presenter;

    public void initPresenter(final P presenter, final Bundle savedInstanceState) {
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
