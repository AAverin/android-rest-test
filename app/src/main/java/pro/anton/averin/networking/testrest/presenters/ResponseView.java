package pro.anton.averin.networking.testrest.presenters;

import pro.anton.averin.networking.testrest.data.models.Request;
import pro.anton.averin.networking.testrest.data.models.Response;

public interface ResponseView extends BaseView {
    void updateShareIntent(Response currentResponse, Request currentRequest);

    void hideNoDataLayout();

    void hideProgressBar();

    void showResponseLayout();

    void showProgressBar();

    void hideResponseLayout();
}
