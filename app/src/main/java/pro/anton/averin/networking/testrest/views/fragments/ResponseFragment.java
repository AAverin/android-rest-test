package pro.anton.averin.networking.testrest.views.fragments;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.presenters.RequestPresenter;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.ResponseView;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class ResponseFragment extends BaseViewPresenterViewpagerFragment<ResponsePresenter> implements ResponseView {

    @Inject
    RequestPresenter presenter;
}
