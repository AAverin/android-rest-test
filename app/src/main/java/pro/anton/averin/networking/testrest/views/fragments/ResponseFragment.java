package pro.anton.averin.networking.testrest.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.Bind;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.ResponseView;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class ResponseFragment extends BaseViewPresenterViewpagerFragment<ResponsePresenter> implements ResponseView {

    @Inject
    ResponsePresenter presenter;

    @Bind(R.id.response_layout)
    LinearLayout responseLayout;
    @Bind(R.id.progressbar_layout)
    LinearLayout progressbarLayout;
    @Bind(R.id.nodata_layout)
    LinearLayout noDataLayout;
    @Bind(R.id.tabhost)
    FragmentTabHost tabHost;

    private ShareActionProvider shareActionProvider;
    private Intent shareIntent = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);

        tabHost.setup(baseContext, getBaseActivity().getSupportFragmentManager(), R.id.tabFrameLayout);
//        tabHost.addTab(tabHost.newTabSpec("rawResponse")
//                .setIndicator(getString(R.string.raw_response)), RawResponseFragment.class, null);
//        tabHost.addTab(tabHost.newTabSpec("jsonResponse")
//                .setIndicator(getString(R.string.json_response)), JsonResponseFragment.class, null);
//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                JsonResponseFragment jsonResponseFragment = ((JsonResponseFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("jsonResponse"));
//                if (jsonResponseFragment != null) {
//                    jsonResponseFragment.cancel();
//                }
//            }
//        });

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_response, container, false);
        return contentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.response_screen_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (shareIntent == null) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/html");
        }
        shareActionProvider.setShareIntent(shareIntent);
    }
}
