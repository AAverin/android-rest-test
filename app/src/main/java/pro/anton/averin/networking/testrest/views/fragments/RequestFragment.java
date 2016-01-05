package pro.anton.averin.networking.testrest.views.fragments;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.presenters.RequestPresenter;
import pro.anton.averin.networking.testrest.presenters.RequestView;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class RequestFragment extends BaseViewPresenterViewpagerFragment<RequestPresenter> implements RequestView {

    @Inject
    RequestPresenter presenter;
}
