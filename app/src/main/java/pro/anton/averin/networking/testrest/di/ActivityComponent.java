package pro.anton.averin.networking.testrest.di;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,
        modules = ActivityModule.class)
public interface ActivityComponent extends ActivityInjectsTo {

}

