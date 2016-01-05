package pro.anton.averin.networking.testrest.views.base;

import pro.anton.averin.networking.testrest.presenters.BasePresenter;
import pro.anton.averin.networking.testrest.presenters.BaseView;

public class PresenterViewLinker<P extends BasePresenter> {

    private final P presenter;

    public PresenterViewLinker(P presenter, BaseView view) {
        this.presenter = presenter;

        presenter.setView(view);
    }

    public void onCreate() {
        presenter.onCreate();
    }

    public void onResume() {
        presenter.onResume();
    }

    public void onPause() {
        presenter.onPause();
    }

    public void onDestroy() {
        presenter.onDestroy();
    }

    public void onVisible() {
        presenter.onVisible();
    }

    public void onHidden() {
        presenter.onHidden();
    }
}
