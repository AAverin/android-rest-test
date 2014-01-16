package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.ui.adapters.RestPagerAdapter;
import pro.anton.averin.networking.testrest.ui.phone.EntriesManagerActivity;

/**
 * Created by AAverin on 09.11.13.
 */
public class TestRestFragment extends Fragment implements ViewPager.OnPageChangeListener, MenuItem.OnMenuItemClickListener {

    private Activity activity;
    private View mGroupRoot;

    private TestRestApp testRestApp;

    private ViewPager mViewPager;
    private RestPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGroupRoot = inflater.inflate(R.layout.fragment_main, null);

        mViewPager = (ViewPager) mGroupRoot.findViewById(R.id.pager);
        pagerAdapter = new RestPagerAdapter(getChildFragmentManager(), new String[] {
                getString(R.string.requestViewTitle),
                getString(R.string.responseViewTitle)
        });
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(this);

        setHasOptionsMenu(true);

        return mGroupRoot;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.testRestApp = (TestRestApp)activity.getApplicationContext();
    }

    public void showResponsePage() {
        mViewPager.setCurrentItem(1);
    }

    public void showRequestPage() {
        mViewPager.setCurrentItem(0);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.test_rest, menu);

        menu.findItem(R.id.action_save).setOnMenuItemClickListener(this);
        menu.findItem(R.id.action_manager).setOnMenuItemClickListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        ((ViewPagerFragment)pagerAdapter.getRegisteredFragment(i)).onPageSelected();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((ViewPagerFragment)pagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem())).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.action_save:
                openManagerActivity(true);
                break;

            case R.id.action_manager:
                openManagerActivity(false);
                break;
        }
        return true;
    }

    private void openManagerActivity(boolean save) {
        boolean invokeActivity = true;
        if (save) {
            invokeActivity = ((RequestFragment)pagerAdapter.getRegisteredFragment(0)).prepareRequest();
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
