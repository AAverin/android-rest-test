package pro.anton.averin.networking.testrest.di;

import dagger.Module;
import pro.anton.averin.networking.testrest.views.base.BaseActivity;

@Module
public class ActivityModule {

    private final BaseActivity baseActivity;

    public ActivityModule(BaseActivity activity) {
        this.baseActivity = activity;
    }
}

