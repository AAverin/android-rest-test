package pro.anton.averin.networking.testrest.presenters;

import android.content.Intent;

public interface ResponseView extends BaseView {
    void setShareIntent(Intent shareIntent);

    void hideNoDataLayout();

    void hideProgressBar();

    void showResponseLayout();

    void showProgressBar();

    void hideResponseLayout();

    void displayMediaNotMountedMessage();

    void setHeaders(String s);

    void setEmptyBody();

    void setResponseBody(String body);
}
