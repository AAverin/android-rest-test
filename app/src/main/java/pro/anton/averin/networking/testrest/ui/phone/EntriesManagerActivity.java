package pro.anton.averin.networking.testrest.ui.phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import pro.anton.averin.networking.testrest.ui.BaseSinglePaneActivity;
import pro.anton.averin.networking.testrest.ui.EntriesManagerFragment;

/**
 * Created by AAverin on 17.12.13.
 */
public class EntriesManagerActivity extends BaseSinglePaneActivity {

    public final static int ENTRIESMANAGER_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected Fragment onCreatePane() {
        return new EntriesManagerFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                Intent homeIntent = new Intent();
                homeIntent.setClass(this, TestRestActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, homeIntent);
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroyPane() {

    }
}
