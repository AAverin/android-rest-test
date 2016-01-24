package pro.anton.averin.networking.testrest.di;

import javax.inject.Singleton;

import dagger.Component;
import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.presenters.AddHeaderPopupPresenter;
import pro.anton.averin.networking.testrest.presenters.AddQueryPopupPresenter;
import pro.anton.averin.networking.testrest.presenters.RequestPresenter;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.TestRestPresenter;
import pro.anton.averin.networking.testrest.rx.UiBus;
import pro.anton.averin.networking.testrest.utils.LLogger;

@Singleton
@Component(modules = ApplicationModule.class)

public interface ApplicationComponent extends ApplicationInjectsTo {

    UiBus uiBus();

    BaseContext baseContext();

    TestRestPresenter testRestPresenter();

    RequestPresenter requestPresenter();

    ResponsePresenter responsePresenter();

    AddHeaderPopupPresenter addHeaderPopupPresenter();

    AddQueryPopupPresenter addQueryPopupPresenter();

    LLogger llogger();
}
