package pro.anton.averin.networking.testrest.views.fragments;

import android.os.Bundle;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.presenters.RequestPresenter;
import pro.anton.averin.networking.testrest.presenters.RequestView;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class RequestFragment extends BaseViewPresenterViewpagerFragment<RequestPresenter> implements RequestView {

    @Inject
    RequestPresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);
    }
}
