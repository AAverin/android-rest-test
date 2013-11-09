package pro.anton.averin.networking.testrest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by AAverin on 09.11.13.
 */
public class RequestFragment extends ViewPagerFragment {

    private View mGroupRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_request, null);

        return mGroupRoot;
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.requestViewTitle);
    }
}
