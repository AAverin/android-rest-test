package pro.anton.averin.networking.testrest.ui.phone;

import android.support.v4.app.Fragment;

import pro.anton.averin.networking.testrest.ui.BaseSinglePaneActivity;
import pro.anton.averin.networking.testrest.ui.EntriesManagerFragment;

/**
 * Created by AAverin on 17.12.13.
 */
public class EntriesManagerActivity extends BaseSinglePaneActivity {
    @Override
    protected Fragment onCreatePane() {
        return new EntriesManagerFragment();
    }

    @Override
    protected void onDestroyPane() {

    }
}
