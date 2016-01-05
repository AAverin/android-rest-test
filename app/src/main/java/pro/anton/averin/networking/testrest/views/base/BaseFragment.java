package pro.anton.averin.networking.testrest.views.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import pro.anton.averin.networking.testrest.BaseContext;

public class BaseFragment extends Fragment {

    protected View contentView;
    protected BaseContext baseContext;

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baseContext = (BaseContext) getActivity().getApplicationContext();
    }
}
