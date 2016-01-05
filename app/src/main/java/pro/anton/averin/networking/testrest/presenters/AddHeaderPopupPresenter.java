package pro.anton.averin.networking.testrest.presenters;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.data.Repository;
import pro.anton.averin.networking.testrest.data.models.Headers;

@Singleton
public class AddHeaderPopupPresenter extends BasePresenterImpl<AddHeaderPopupView> {

    @Inject
    Repository repository;

    @Inject
    public AddHeaderPopupPresenter(BaseContext baseContext) {
        super(baseContext);
    }

    public List<Headers.Header> getSupportedHeaders() {
        return repository.getSupportedHeaders();
    }

    public boolean updateHeader(Headers.Header selectedHeader) {
        return repository.updateHeader(selectedHeader);
    }
}
