package pro.anton.averin.networking.testrest.views.activities;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.presenters.TestRestPresenter;
import pro.anton.averin.networking.testrest.presenters.TestRestView;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterActivity;

public class TestRestActivity extends BaseViewPresenterActivity<TestRestPresenter> implements TestRestView {

    @Inject
    TestRestPresenter presenter;

}
