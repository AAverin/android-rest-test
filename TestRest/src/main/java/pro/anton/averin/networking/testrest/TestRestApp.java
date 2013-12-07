package pro.anton.averin.networking.testrest;

import android.app.Application;
import android.content.Context;

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
    public RestTestDb restTestDb = null;

    public Request currentRequest;
    public Response currentResponse;

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
