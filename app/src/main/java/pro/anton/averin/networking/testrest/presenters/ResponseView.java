package pro.anton.averin.networking.testrest.presenters;

import android.content.Intent;

import pro.anton.averin.networking.testrest.data.models.Response;

public interface ResponseView extends BaseView {
    void updateShareIntent(Intent shareIntent);

    void hideNoDataLayout();

    void hideProgressBar();

    void showResponseLayout();

    void showProgressBar();

    void hideResponseLayout();

    void displayMediaNotMountedMessage();

    void update(Response currentResponse);
}
