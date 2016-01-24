package pro.anton.averin.networking.testrest.views.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.views.base.BaseActivity;

public abstract class ToolbarActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getComponent().injectTo(this);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
