package pro.anton.averin.networking.testrest.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 09.11.13.
 */
public abstract class BaseSinglePaneActivity extends BaseActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_singlepane_empty);

        if (savedInstanceState == null) {
            //init custom fragment content
            mFragment = onCreatePane();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.root_container, mFragment, "MAIN")
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected abstract Fragment onCreatePane();

    protected abstract void onDestroyPane();
}
