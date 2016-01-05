package pro.anton.averin.networking.testrest.di;

import javax.inject.Singleton;

import dagger.Component;
import pro.anton.averin.networking.testrest.utils.LLogger;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent extends ApplicationInjectsTo {
    LLogger llogger();
}
