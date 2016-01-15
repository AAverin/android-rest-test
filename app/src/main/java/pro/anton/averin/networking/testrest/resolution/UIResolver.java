package pro.anton.averin.networking.testrest.resolution;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.di.PerActivity;
import pro.anton.averin.networking.testrest.views.base.BaseActivity;

@PerActivity
public class UIResolver {

    private final BaseActivity baseActivity;
    private final ViewGroup root;

    @Inject
    public UIResolver(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        root = (ViewGroup) baseActivity.findViewById(R.id.rootView);
    }

    public void snackBar(@StringRes int messageResource) {
        Snackbar.make(root, messageResource, Snackbar.LENGTH_LONG).show();
    }
}
