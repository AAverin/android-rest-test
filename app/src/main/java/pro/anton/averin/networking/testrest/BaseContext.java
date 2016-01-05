package pro.anton.averin.networking.testrest;

import android.app.Application;
import android.content.Context;
import android.view.ViewConfiguration;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;

import io.fabric.sdk.android.Fabric;
import pro.anton.averin.networking.testrest.data.db.RestTestDb;
import pro.anton.averin.networking.testrest.data.models.Request;
import pro.anton.averin.networking.testrest.data.models.Response;

/**
 * Created by AAverin on 13.11.13.
 */
public class BaseContext extends Application {
    private static BaseContext instance;

    public RestTestDb testRestDb = null;

    public Request currentRequest;
    public Response currentResponse;

    public BaseContext() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.isCrashlyticsEnabled)
            Fabric.with(this, new Crashlytics());
        testRestDb = new RestTestDb(getContext());

        //attempt to force overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
}
