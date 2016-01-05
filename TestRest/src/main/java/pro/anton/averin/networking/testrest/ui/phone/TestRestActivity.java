package pro.anton.averin.networking.testrest.ui.phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.ui.BaseSinglePaneActivity;
import pro.anton.averin.networking.testrest.ui.TestRestFragment;

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
