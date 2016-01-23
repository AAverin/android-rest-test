package pro.anton.averin.networking.testrest.views.androidviews.toolbar;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.di.ActivityScope;

@ActivityScope
public class DefaultToolbarImpl extends BaseToolbar {

    @Inject
    public DefaultToolbarImpl(ActionBar actionBar) {
        super(actionBar);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        super.setToolbar(toolbar);

        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
    }
}
