package pro.anton.averin.networking.testrest.views.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.navigation.UINavigator;
import pro.anton.averin.networking.testrest.presenters.TestRestPresenter;
import pro.anton.averin.networking.testrest.presenters.TestRestView;
import pro.anton.averin.networking.testrest.rx.UiBus;
import pro.anton.averin.networking.testrest.rx.events.NaviManagerScreenEvent;
import pro.anton.averin.networking.testrest.rx.events.NaviResponseScreenEvent;
import pro.anton.averin.networking.testrest.utils.LLogger;
import pro.anton.averin.networking.testrest.views.adapters.RestPagerAdapter;
import pro.anton.averin.networking.testrest.views.androidviews.toolbar.DefaultToolbarImpl;
import pro.anton.averin.networking.testrest.views.base.ToolbarPActivity;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class TestRestActivity extends ToolbarPActivity<TestRestPresenter> implements TestRestView {

    @Inject
    TestRestPresenter presenter;
    @Inject
    DefaultToolbarImpl defaultToolbar;

    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.rootView)
    FrameLayout root;
    @Bind(R.id.btn_fab)
    FloatingActionButton sendButton;
    RestPagerAdapter pagerAdapter;

    @Inject
    UiBus uiBus;
    @Inject
    UINavigator uiNavigator;
    @Inject
    LLogger llogger;

    CompositeSubscription events = new CompositeSubscription();

    @OnClick(R.id.btn_fab)
    public void onFabClicked() {
        presenter.onFabClicked();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().injectTo(this);
        initPresenter(presenter, savedInstanceState);
        presenter.setView(this);

        ButterKnife.bind(this);

        presenter.undim();

        pagerAdapter = new RestPagerAdapter(
                getSupportFragmentManager(), new String[]{
                getString(R.string.requestViewTitle),
                getString(R.string.responseViewTitle)
        });

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (defaultToolbar != null && !isFinishing()) {
            defaultToolbar.setToolbar(getToolbar());
        }

        llogger.log_e(this, "onResume");

        events.add(
                uiBus.events(NaviManagerScreenEvent.class).subscribe(
                        new Action1<NaviManagerScreenEvent>() {
                            @Override
                            public void call(final NaviManagerScreenEvent naviManagerScreenEvent) {
                                if (naviManagerScreenEvent.save) {
                                    presenter.onNavigateToManagerScreenWithSave();
                                } else {
                                    presenter.onNavigateToManagerScreenWithoutSave();
                                }
                            }
                        }));

        events.add(
                uiBus.events(NaviResponseScreenEvent.class).subscribe(
                        new Action1<NaviResponseScreenEvent>() {
                            @Override
                            public void call(final NaviResponseScreenEvent naviResponseScreenEvent) {
                                presenter.onNavigateToResponseScreen();
                            }
                        }));
    }

    @Override
    protected void onPause() {
        super.onPause();
        llogger.log_e(this, "onPause");
        events.unsubscribe();
    }

    @Override
    public void undim() {
        root.getForeground().setAlpha(0);
    }

    @Override
    public void navigateToManagerScreenWithSave() {
        uiNavigator.navigateToManagerScreenForSave();
    }

    @Override
    public void navigateToManagerScreenWithoutSave() {
        uiNavigator.navigateToManagerScreen();
    }

    @Override
    public void navigateToResponseScreen() {
        viewPager.setCurrentItem(1);
    }
}
