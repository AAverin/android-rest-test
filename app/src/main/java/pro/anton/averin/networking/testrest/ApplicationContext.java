package pro.anton.averin.networking.testrest;

import android.app.Application;

import pro.anton.averin.networking.testrest.di.ApplicationComponent;
import pro.anton.averin.networking.testrest.di.ApplicationModule;
import pro.anton.averin.networking.testrest.di.DaggerApplicationComponent;

public class ApplicationContext extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
