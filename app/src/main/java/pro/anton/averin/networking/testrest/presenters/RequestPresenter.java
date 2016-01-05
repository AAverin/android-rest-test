package pro.anton.averin.networking.testrest.presenters;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.BaseContext;

public class RequestPresenter extends BasePresenterImpl<RequestView> {

    @Inject
    public RequestPresenter(BaseContext baseContext) {
        super(baseContext);
    }

    public void onUseFileCheckboxChecked(boolean isChecked) {
        view.cleanPostBody();
        if (isChecked) {
            view.showPickFileButton();
            view.setPostBodyFileHint();
        } else {
            view.hidePickFileButton();
            view.setPostBodyDefaultHint();
        }
    }

    public void onPickFileButtonClicked() {
        view.showFileChooser();
    }

    public void onAddQueryButtonClicked() {
        view.showAddQueryPopup();
    }

    public void onAddHeadersButtonClicked() {
        view.showAddHeaderPopup();
    }

    public void onSendClicked() {
        if (prepareRequest()) {
            view.showResponseScreen();
        }
    }

    public void onSaveItemClicked() {
        view.showManagerScreenForSave();
    }

    public void onManagerItemClicked() {
        view.showManagerScreen();
    }

    public void onClearItemClicked() {
        view.clearFields();
    }

    public void onPostMethodSelected() {
        view.showPostLayout();
    }

    public void onPostMethodUnselected() {
        view.hidePostLayout();
    }


    private boolean prepareRequest() {
//        if (!validate()) {
//            Toast.makeText(activity, getString(R.string.error_emptyFields), 3000).show();
//            baseUrlEditText.requestFocus();
//            return false;
//        }
//        testRestApp.currentResponse = null;
//        testRestApp.currentRequest = buildRequest();
        return false;
    }

    public void addQueryPopupDismissed() {
        view.unDimBackground();
    }
}
