package pro.anton.averin.networking.testrest.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.ui.activities.BaseActivity;

/**
 * Created by AAverin on 30-9-2014.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    public final static String BASE_DIALOG_FRAGMENT_TAG = "BaseDialogFragmentTag";

    protected BaseContext baseContext = null;

    protected ViewGroup contentView = null;

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public abstract String getFragmentName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialogTheme);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baseContext = (BaseContext) getActivity().getApplicationContext();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean handleBackPress() {
        return false;
    }

    public void displayDialog(BaseActivity activity, String tag) {
        if (activity == null || !activity.isActive()) {
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        if (fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment oldDialog = fragmentManager.findFragmentByTag(tag);
        if (oldDialog != null) {
            transaction.remove(oldDialog);
        }
        transaction.addToBackStack(null);

        //fix for IllegalStateException: Can not perform this action after onSaveInstanceState
        //http://stackoverflow.com/questions/15729138/on-showing-dialog-i-get-can-not-perform-this-action-after-onsaveinstancestate
        if (!activity.isActive()) { //final safeguard in case activity was closed in the middle
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        this.show(ft, tag);
    }

}
