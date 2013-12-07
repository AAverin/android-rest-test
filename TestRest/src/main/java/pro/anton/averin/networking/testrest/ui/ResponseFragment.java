package pro.anton.averin.networking.testrest.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 09.11.13.
 */
public class ResponseFragment extends ViewPagerFragment {
    private View mGroupRoot;
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_response, null);
        mTabHost = (FragmentTabHost) mGroupRoot.findViewById(R.id.tabhost);
        mTabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(mTabHost.newTabSpec("rawResponse").setIndicator(getString(R.string.raw_response)), RawResponseFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("jsonResponse").setIndicator(getString(R.string.json_response)), JsonResponseFragment.class, null);

        return mGroupRoot;
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.responseViewTitle);
    }
}
