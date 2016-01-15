package pro.anton.averin.networking.testrest;

import android.view.ViewConfiguration;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;

import io.fabric.sdk.android.Fabric;

public class BaseContext extends ApplicationContext {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Config.isCrashlyticsEnabled)
            Fabric.with(this, new Crashlytics());

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
