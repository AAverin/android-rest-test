package pro.anton.averin.networking.testrest.ui;

import pro.anton.averin.networking.testrest.ui.fragments.BaseFragment;

/**
 * Created by AAverin on 09.11.13.
 */
public abstract class ViewPagerFragment extends BaseFragment {

    protected abstract String getPageTitle();
    protected abstract void onPageSelected();
}
