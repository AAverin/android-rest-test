package pro.anton.averin.networking.testrest;

import android.app.Application;
import android.content.Context;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import aaverin.android.net.CachedNetworkManager;
import pro.anton.averin.networking.testrest.db.RestTestDb;
import pro.anton.averin.networking.testrest.models.Request;
import pro.anton.averin.networking.testrest.models.Response;

/**
 * Created by AAverin on 13.11.13.
 */
public class TestRestApp extends Application {
    private static TestRestApp instance;

    public CachedNetworkManager networkManager;
    public RestTestDb testRestDb = null;

    public Request currentRequest;
    public Response currentResponse;

    public TestRestApp() {
        instance = this;
        networkManager = CachedNetworkManager.getInstance();
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        testRestDb = new RestTestDb(getContext());

        //attempt to force overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
}
