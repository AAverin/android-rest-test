package pro.anton.averin.networking.testrest.legacy.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.legacy.activities.phone.EntriesManagerActivity;
import pro.anton.averin.networking.testrest.views.adapters.RestPagerAdapter;

/**
 * Created by AAverin on 09.11.13.
 */
public class TestRestFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private Activity activity;
    private BaseContext baseContext;


    private ViewPager mViewPager;
    private RestPagerAdapter pagerAdapter;

    private boolean isDimmed = false;

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        return contentView;
    }

    private void init() {
        ((FrameLayout) contentView).getForeground().setAlpha(0);

        initToolbar();

        mViewPager = (ViewPager) contentView.findViewById(R.id.pager);
        pagerAdapter = new RestPagerAdapter(getChildFragmentManager(), new String[]{
                getString(R.string.requestViewTitle),
                getString(R.string.responseViewTitle)
        });
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.baseContext = (BaseContext) activity.getApplicationContext();
        init();
    }

    public void showResponsePage() {
        mViewPager.setCurrentItem(1);
    }

    public void showRequestPage() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        ViewPagerFragment pagerFragment = (ViewPagerFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(mViewPager.getId(), i));
        pagerFragment.onPageSelected();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int position = mViewPager.getCurrentItem();
        ViewPagerFragment pagerFragment = (ViewPagerFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(mViewPager.getId(), position));
        pagerFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void openManagerActivity(boolean save) {
        boolean invokeActivity = true;
        if (save) {
            ViewPagerFragment pagerFragment = (ViewPagerFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(mViewPager.getId(), 0));

            invokeActivity = ((RequestFragment) pagerFragment).prepareRequest();
        }
        if (invokeActivity) {
            Intent managerActivityIntent = new Intent();
            managerActivityIntent.setClass(activity, EntriesManagerActivity.class);
            if (save) {
                managerActivityIntent.putExtra("save", true);
            }
            startActivityForResult(managerActivityIntent, EntriesManagerActivity.ENTRIESMANAGER_REQUEST_CODE);
        }
    }
}
