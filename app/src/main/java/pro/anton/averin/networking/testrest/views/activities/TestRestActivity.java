package pro.anton.averin.networking.testrest.views.activities;

import android.os.Bundle;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.presenters.TestRestPresenter;
import pro.anton.averin.networking.testrest.presenters.TestRestView;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterActivity;

public class TestRestActivity extends BaseViewPresenterActivity<TestRestPresenter> implements TestRestView {

    @Inject
    TestRestPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().injectTo(this);
        initializePresenter(presenter, this);

        setContentView(R.layout.fragment_main);
        ButterKnife.bind(this);
    }
}
