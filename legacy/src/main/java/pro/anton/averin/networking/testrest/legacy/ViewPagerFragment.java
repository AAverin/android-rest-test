package pro.anton.averin.networking.testrest.legacy;

import android.support.v4.app.Fragment;

/**
 * Created by AAverin on 09.11.13.
 */
public abstract class ViewPagerFragment extends Fragment {

    protected abstract String getPageTitle();
    protected abstract void onPageSelected();
}