package pro.anton.averin.networking.testrest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

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
        mViewPager.setAdapter(new RestPagerAdapter(getFragmentManager()));

        return mGroupRoot;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
