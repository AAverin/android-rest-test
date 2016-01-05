package pro.anton.averin.networking.testrest.legacy.phone;

import android.support.v4.app.Fragment;
import android.view.Menu;

import pro.anton.averin.networking.testrest.legacy.BaseSinglePaneActivity;
import pro.anton.averin.networking.testrest.legacy.TestRestFragment;

public class TestRestActivity extends BaseSinglePaneActivity {

    @Override
    protected Fragment onCreatePane() {
        return new TestRestFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroyPane() {

    }
}
