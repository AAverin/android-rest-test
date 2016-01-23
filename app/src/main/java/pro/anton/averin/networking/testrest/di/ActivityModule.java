package pro.anton.averin.networking.testrest.di;

import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;
import pro.anton.averin.networking.testrest.views.base.BaseActivity;

@Module
public class ActivityModule {

    private final BaseActivity baseActivity;

    public ActivityModule(BaseActivity activity) {
        this.baseActivity = activity;
    }

    @Provides
    @ActivityScope
    BaseActivity baseActivity() {
        return baseActivity;
    }

    @Provides
    @ActivityScope
    LayoutInflater layoutInflater() {
        return baseActivity.getLayoutInflater();
    }

    @Provides
    @ActivityScope
    ActionBar actionBar() {
        return baseActivity.getSupportActionBar();
    }
}

