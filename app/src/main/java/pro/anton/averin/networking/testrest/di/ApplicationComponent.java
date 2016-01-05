package pro.anton.averin.networking.testrest.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent extends ApplicationInjectsTo {

}
