package pro.anton.averin.networking.testrest.resolution;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.ViewGroup;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.di.ActivityScope;
import pro.anton.averin.networking.testrest.views.base.BaseActivity;

@ActivityScope
public class UIResolver {

    private final BaseActivity baseActivity;
    private final ViewGroup snackbarRoot;

    @Inject
    public UIResolver(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        snackbarRoot = (ViewGroup) baseActivity.findViewById(R.id.toolbar_content);
    }

    public void snackBar(@StringRes int messageResource) {
        Snackbar.make(snackbarRoot, messageResource, Snackbar.LENGTH_LONG).show();
    }
}
