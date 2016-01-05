package pro.anton.averin.networking.testrest.views.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.presenters.TestRestPresenter;
import pro.anton.averin.networking.testrest.presenters.TestRestView;
import pro.anton.averin.networking.testrest.views.adapters.RestPagerAdapter;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterActivity;

public class TestRestActivity extends BaseViewPresenterActivity<TestRestPresenter> implements TestRestView {

    @Inject
    TestRestPresenter presenter;

    @Bind(R.id.pager)
    ViewPager viewPager;

    RestPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().injectTo(this);
        initializePresenter(presenter, this);

        setContentView(R.layout.fragment_main);
        ButterKnife.bind(this);

        pagerAdapter = new RestPagerAdapter(getSupportFragmentManager(), new String[]{
                getString(R.string.requestViewTitle),
                getString(R.string.responseViewTitle)
        });

        viewPager.setAdapter(pagerAdapter);
    }
}
