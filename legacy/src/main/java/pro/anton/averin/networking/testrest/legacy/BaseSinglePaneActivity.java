package pro.anton.averin.networking.testrest.legacy;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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
