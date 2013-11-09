package pro.anton.averin.networking.testrest;

import android.support.v4.app.Fragment;

public class TestRestActivity extends BaseSinglePaneActivity {

    @Override
    protected Fragment onCreatePane() {
        return new TestRestFragment();
    }

    @Override
    protected void onDestroyPane() {

    }
}
