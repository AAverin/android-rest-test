package pro.anton.averin.networking.testrest.views.androidviews.toolbar;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import javax.inject.Inject;

public class BaseToolbar {

    protected final ActionBar actionBar;

    private Toolbar toolbar;

    @Inject
    public BaseToolbar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        resetToolbar();
    }

    protected void resetToolbar() {
        toolbar.setContentInsetsAbsolute(0, 0);

        TypedValue typedValue = new TypedValue();
        actionBar.getThemedContext().getTheme().resolveAttribute(android.R.attr.homeAsUpIndicator, typedValue, true);
        actionBar.setHomeAsUpIndicator(typedValue.resourceId);
        actionBar.setDisplayShowCustomEnabled(false);
    }
}
