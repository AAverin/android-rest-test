package pro.anton.averin.networking.testrest.views.base;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
