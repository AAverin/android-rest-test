package pro.anton.averin.networking.testrest.legacy.activities.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import pro.anton.averin.networking.testrest.legacy.activities.BaseSinglePaneActivity;
import pro.anton.averin.networking.testrest.legacy.fragments.TestRestFragment;

public class TestRestActivity extends BaseSinglePaneActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
