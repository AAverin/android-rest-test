package pro.anton.averin.networking.testrest.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import pro.anton.averin.networking.testrest.ApplicationContext;
import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Repository;
import pro.anton.averin.networking.testrest.data.RepositoryImpl;

@Module
public class ApplicationModule {

    private final BaseContext baseContext;

    public ApplicationModule(ApplicationContext baseContext) {
        this.baseContext = (BaseContext) baseContext;
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

    @Provides
    @Singleton
    OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
