package pro.anton.averin.networking.testrest.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 09.11.13.
 */
public class ResponseFragment extends ViewPagerFragment {
    private View mGroupRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_response, null);

        return mGroupRoot;
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.responseViewTitle);
    }
}
