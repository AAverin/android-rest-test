package pro.anton.averin.networking.testrest.views.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.di.ActivityComponent;
import pro.anton.averin.networking.testrest.di.ActivityModule;
import pro.anton.averin.networking.testrest.di.DaggerActivityComponent;
import pro.anton.averin.networking.testrest.di.HasComponent;


public class BaseActivity extends AppCompatActivity implements HasComponent<ActivityComponent> {

    protected BaseContext baseContext;
    private ActivityComponent activityComponent;

    @Override
    public ActivityComponent getComponent() {
        return activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseContext = (BaseContext) getApplicationContext();

        baseContext.getApplicationComponent().injectTo(this);


        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(baseContext.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }
}
