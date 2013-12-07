package pro.anton.averin.networking.testrest.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 07.12.13.
 */
public class RawResponseFragment extends Fragment {

    private View mGroupRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGroupRoot = inflater.inflate(R.layout.fragment_raw_response, container, false);
        return mGroupRoot;
    }
}
