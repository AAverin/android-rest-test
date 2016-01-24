package pro.anton.averin.networking.testrest.presenters;

import pro.anton.averin.networking.testrest.data.models.Request;
import pro.anton.averin.networking.testrest.resolution.Resolution;

public interface RequestView extends BaseView {
    void cleanPostBody();

    void showPickFileButton();

    void setPostBodyFileHint();

    void hidePickFileButton();

    void setPostBodyDefaultHint();

    void showFileChooser();

    void showAddQueryPopup();

    void showAddHeaderPopup();

    void showPostLayout();

    void hidePostLayout();

    void clearFields();

    void focusBaseUrl();

    Request getRequest();

    Resolution getUiResolution();

    void navigateToManagerScreenForSave();

    void navigateToManagerScreen();

    void navigateToResponseScreen();
}
