package pro.anton.averin.networking.testrest.presenters;

import android.content.Intent;

import org.json.JSONObject;

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

    void hideRawResponse();

    void showJson();

    void showRawResponse();

    void hideJson();

    void enableJson();

    void setJson(JSONObject jsonObject);

    void disableJson();
}
