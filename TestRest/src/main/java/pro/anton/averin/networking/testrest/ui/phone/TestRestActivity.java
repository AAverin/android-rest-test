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

public class TestRestActivity extends BaseSinglePaneActivity implements MenuItem.OnMenuItemClickListener {

    @Override
    protected Fragment onCreatePane() {
        return new TestRestFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_rest, menu);

        menu.findItem(R.id.action_save).setOnMenuItemClickListener(this);
        menu.findItem(R.id.action_manager).setOnMenuItemClickListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroyPane() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.action_save:
                openManagerActivity(true);
                break;

            case R.id.action_manager:
                openManagerActivity(false);
                break;
        }
        return true;
    }

    private void openManagerActivity(boolean save) {
        Intent managerActivityIntent = new Intent();
        managerActivityIntent.setClass(this, TestRestActivity.class);
        if (save) {
            managerActivityIntent.putExtra("save", true);
        }
        startActivity(managerActivityIntent);
    }
}
