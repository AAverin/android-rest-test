package pro.anton.averin.networking.testrest.legacy.fragments;

/**
 * Created by AAverin on 09.11.13.
 */
public abstract class ViewPagerFragment extends BaseFragment {

    protected abstract String getPageTitle();

    protected abstract void onPageSelected();
}
