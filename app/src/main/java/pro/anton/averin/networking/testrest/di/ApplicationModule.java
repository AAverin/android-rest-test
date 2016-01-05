package pro.anton.averin.networking.testrest.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Repository;
import pro.anton.averin.networking.testrest.data.RepositoryImpl;

@Module
public class ApplicationModule {

    private final BaseContext baseContext;

    public ApplicationModule(BaseContext baseContext) {
        this.baseContext = baseContext;
    }

    @Provides
    @Singleton
    BaseContext baseContext() {
        return this.baseContext;
    }

    @Provides
    @Singleton
    Repository repository(RepositoryImpl repository) {
        return repository;
    }
}
