package pro.anton.averin.networking.testrest;

import android.app.Application;
import android.content.Context;
import android.view.ViewConfiguration;

import com.bugsense.trace.BugSenseHandler;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        if (Config.isBugsenseEnabled)
            BugSenseHandler.initAndStartSession(this, Config.BUGSENSE_APIKEY);
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

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }
}
