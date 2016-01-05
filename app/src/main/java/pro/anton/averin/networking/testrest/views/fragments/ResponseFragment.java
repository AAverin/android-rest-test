package pro.anton.averin.networking.testrest.views.fragments;

import android.os.Bundle;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.ResponseView;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class ResponseFragment extends BaseViewPresenterViewpagerFragment<ResponsePresenter> implements ResponseView {

    @Inject
    ResponsePresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);
    }
}
