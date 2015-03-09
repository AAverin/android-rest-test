package pro.anton.averin.networking.testrest.ui.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import java.util.HashMap;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.ui.fragments.BaseDialogFragment;
import pro.anton.averin.networking.testrest.ui.fragments.BaseFragment;
import pro.anton.averin.networking.testrest.utils.Logger;

/**
 * Created by AAverin on 09.11.13.
 */
public abstract class BaseActivity extends FragmentActivity {

    public final static String MAIN_FRAGMENT_TAG = "MAIN";

    public enum ActivityState {
        created,
        paused,
        stopped,
        resumed
    }
    public ActivityState activityState;

    protected BaseContext baseContext;
    public Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityState = ActivityState.created;

        uiHandler = new Handler();

        baseContext = (BaseContext) getApplicationContext();

//        Mint.initAndStartSession(this, Config.BUGSENSE_API_KEY);
//        Mint.addExtraData("activity", this.getClass().getName());

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
    }
//    public abstract String getScreenName();


    public boolean isActive() {
        return activityState != BaseActivity.ActivityState.paused && activityState != BaseActivity.ActivityState.stopped && !isFinishing();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.log_e(this, "onStart");
        activityState = ActivityState.created;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.log_e(this, "onPause");
        activityState = ActivityState.paused;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.log_e(this, "onResume");
        activityState = ActivityState.resumed;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.log_e(this, "onStop");
        activityState = ActivityState.stopped;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (fragment != null && !fragment.handleBackPress()) {
                super.onBackPressed();
            }
        } catch (ClassCastException e) {
            BaseDialogFragment fragment = (BaseDialogFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (fragment != null && !fragment.handleBackPress()) {
                super.onBackPressed();
            }
        }
    }

    public static void restart(BaseContext baseContext) {
        restart(baseContext, 0);
    }
    public static void restart(BaseContext baseContext, int delay) {
        if (delay == 0) {
            delay = 1;
        }

        Logger.log_e("", "restarting app");
        Intent restartIntent = baseContext.getPackageManager()
                .getLaunchIntentForPackage(baseContext.getPackageName());
        PendingIntent intent = PendingIntent.getActivity(
                baseContext, 0,
                restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlarmManager manager = (AlarmManager) baseContext.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

    public void handleGenericErrorRestart(int delay) {
        handleGenericError();
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                restart(baseContext, 1);
            }
        }, delay);
    }

    public void handleGenericError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.label_critical_error)
                .setMessage(R.string.label_critical_error_message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean haveCached = false;
    boolean isTabletCache = false;
    public boolean isTablet() {
        if (!haveCached) {
            isTabletCache = getResources().getBoolean(R.bool.isTablet);
        }
        return isTabletCache;
    }


    private void debugLogEvent(String type, String eventName, HashMap<String, String> parameters) {
        StringBuilder debugLogMessage = new StringBuilder();

        debugLogMessage.append("__debugLogEvent__");
        debugLogMessage.append(type);
        debugLogMessage.append(" :: ");
        debugLogMessage.append(eventName);
        if (parameters != null) {
            debugLogMessage.append(" :: ");
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                debugLogMessage.append("(");
                debugLogMessage.append(key);
                debugLogMessage.append(",");
                debugLogMessage.append(value);
                debugLogMessage.append("), ");
            }
        }

        Logger.log("BaseActivity", debugLogMessage.toString());
    }

}