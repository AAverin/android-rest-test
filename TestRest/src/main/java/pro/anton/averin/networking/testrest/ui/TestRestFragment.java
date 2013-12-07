package pro.anton.averin.networking.testrest.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.ui.adapters.RestPagerAdapter;

/**
 * Created by AAverin on 09.11.13.
 */
public class TestRestFragment extends Fragment {

    private View mGroupRoot;

    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGroupRoot = inflater.inflate(R.layout.fragment_main, null);

        mViewPager = (ViewPager) mGroupRoot.findViewById(R.id.pager);
        mViewPager.setAdapter(new RestPagerAdapter(getChildFragmentManager(), new String[] {
                getString(R.string.requestViewTitle),
                getString(R.string.responseViewTitle)
        }));

        return mGroupRoot;
    }

    public void showResponsePage() {
        mViewPager.setCurrentItem(1);
    }

    public void showRequestPage() {
        mViewPager.setCurrentItem(0);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
