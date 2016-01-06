package pro.anton.averin.networking.testrest.di;

import javax.inject.Singleton;

import dagger.Component;
import pro.anton.averin.networking.testrest.presenters.AddHeaderPopupPresenter;
import pro.anton.averin.networking.testrest.presenters.AddQueryPopupPresenter;
import pro.anton.averin.networking.testrest.presenters.JsonResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.RawResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.RequestPresenter;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.TestRestPresenter;
import pro.anton.averin.networking.testrest.utils.LLogger;

@Singleton
@Component(modules = ApplicationModule.class)

public interface ApplicationComponent extends ApplicationInjectsTo {

    TestRestPresenter testRestPresenter();

    RequestPresenter requestPresenter();

    ResponsePresenter responsePresenter();

    AddHeaderPopupPresenter addHeaderPopupPresenter();

    AddQueryPopupPresenter addQueryPopupPresenter();

    RawResponsePresenter rawResponsePresenter();

    JsonResponsePresenter jsonResponsePresenter();

    LLogger llogger();
}
