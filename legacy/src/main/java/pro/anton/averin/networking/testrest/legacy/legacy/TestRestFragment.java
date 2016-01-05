package pro.anton.averin.networking.testrest.legacy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.legacy.adapters.RestPagerAdapter;
import pro.anton.averin.networking.testrest.legacy.phone.EntriesManagerActivity;

/**
 * Created by AAverin on 09.11.13.
 */
public class TestRestFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private Activity activity;
    private TestRestApp testRestApp;



    private ViewPager mViewPager;
    private RestPagerAdapter pagerAdapter;

    private boolean isDimmed = false;

    private View mGroupRoot;

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    @Override
    public View getView() {
        return mGroupRoot;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGroupRoot = inflater.inflate(R.layout.fragment_main, null);
        return mGroupRoot;
    }

    private void init() {
        ((FrameLayout)getView()).getForeground().setAlpha(0);

        mViewPager = (ViewPager) getView().findViewById(R.id.pager);
        pagerAdapter = new RestPagerAdapter(getChildFragmentManager(), new String[] {
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
        this.testRestApp = (TestRestApp)activity.getApplicationContext();
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

            invokeActivity = ((RequestFragment)pagerFragment).prepareRequest();
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

    public void dim() {
        ((FrameLayout)getView()).getForeground().setAlpha(100);
    }

    public void unDim() {
        ((FrameLayout)getView()).getForeground().setAlpha(0);
    }

    public void toggleDim() {
        if (isDimmed) {
            unDim();
        } else {
            dim();
        }
    }
}
