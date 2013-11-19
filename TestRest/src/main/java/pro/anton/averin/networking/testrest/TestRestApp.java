package pro.anton.averin.networking.testrest;

import android.app.Application;
import android.content.Context;

import pro.anton.averin.networking.testrest.db.RestTestDb;

/**
 * Created by AAverin on 13.11.13.
 */
public class TestRestApp extends Application {
    private static TestRestApp instance;

    public RestTestDb restTestDb = null;

    public TestRestApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        restTestDb = new RestTestDb(getContext());
    }
}
