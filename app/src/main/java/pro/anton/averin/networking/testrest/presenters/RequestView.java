package pro.anton.averin.networking.testrest.presenters;

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
}
