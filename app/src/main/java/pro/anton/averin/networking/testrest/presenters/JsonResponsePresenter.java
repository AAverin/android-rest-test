package pro.anton.averin.networking.testrest.presenters;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import pro.anton.averin.networking.testrest.data.Storage;

@Singleton
public class JsonResponsePresenter extends BasePresenter<JsonResponseView> {

    @Inject
    Storage storage;

    @Inject
    public JsonResponsePresenter() {
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView().update(storage.getCurrentResponse());
    }
}
