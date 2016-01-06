package pro.anton.averin.networking.testrest.presenters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.MockRxSchedulers;
import pro.anton.averin.networking.testrest.data.Repository;
import pro.anton.averin.networking.testrest.navigation.Navigator;
import pro.anton.averin.networking.testrest.rx.RxBus;
import pro.anton.averin.networking.testrest.rx.events.DimBackgroundEvent;
import pro.anton.averin.networking.testrest.rx.events.UndimBackgroundEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RequestPresenterTest {

    @Mock
    BaseContext baseContext;
    @Mock
    RequestView view;
    @Mock
    RxBus rxBus;
    @Mock
    Navigator navigator;
    @Mock
    Repository repository;

    MockRxSchedulers mockRxSchedulers = new MockRxSchedulers();

    RequestPresenter presenter;

    @Before
    public void setup() {
        presenter = new RequestPresenter(baseContext);
        presenter.setView(view);

        presenter.rxBus = rxBus;
        presenter.navigator = navigator;
        presenter.repository = repository;
        presenter.schedulers = mockRxSchedulers;
    }

    @Test
    public void checkingFileBoxCleansPostBody() {
        presenter.onUseFileCheckboxChecked(true);
        verify(view).cleanPostBody();
    }

    @Test
    public void uncheckingFileBoxCleansPostBody() {
        presenter.onUseFileCheckboxChecked(false);
        verify(view).cleanPostBody();
    }

    @Test
    public void checkingFileBoxShowsPickFileButton() {
        presenter.onUseFileCheckboxChecked(true);
        verify(view).showPickFileButton();
    }

    @Test
    public void checkingFileBoxSetsPostBodyFileHint() {
        presenter.onUseFileCheckboxChecked(true);
        verify(view).setPostBodyFileHint();
    }

    @Test
    public void uncheckingUseFileBoxHidesPickFileButton() {
        presenter.onUseFileCheckboxChecked(false);
        verify(view).hidePickFileButton();
    }

    @Test
    public void uncheckingUseFileBoxSetsPostBodyDefaultHint() {
        presenter.onUseFileCheckboxChecked(false);
        verify(view).setPostBodyDefaultHint();
    }

    @Test
    public void clickingPickFileButtonShowsFileChooser() {
        presenter.onPickFileButtonClicked();
        verify(view).showFileChooser();
    }

    @Test
    public void clickingAddQueryButtonShowsPopup() {
        presenter.onAddQueryButtonClicked();
        verify(view).showAddQueryPopup();
    }

    @Test
    public void clickingAddHeadersButtonShowsPopup() {
        presenter.onAddHeadersButtonClicked();
        verify(view).showAddHeaderPopup();
    }

    @Test
    public void clickingSaveItemNavigatesToManagerSaveScreen() {
        presenter.onSaveItemClicked();
        verify(navigator).navigateToManagerScreenForSave();
    }

    @Test
    public void clickingManagerItemNavigatesToManager() {
        presenter.onManagerItemClicked();
        verify(navigator).navigateToManagerScreen();
    }

    @Test
    public void clickingClearItemClearsFields() {
        presenter.onClearItemClicked();
        verify(view).clearFields();
    }

    @Test
    public void selectingPostMethodShowsPostLayout() {
        presenter.onPostMethodSelected();
        verify(view).showPostLayout();
    }

    @Test
    public void deselectinPostMethodHidesPostLayout() {
        presenter.onPostMethodUnselected();
        verify(view).hidePostLayout();
    }

    @Test
    public void dismissingAddQueryPopupUndimsBackground() {
        presenter.addQueryPopupDismissed();
        verify(rxBus).send(any(UndimBackgroundEvent.class));
    }

    @Test
    public void requestingUndimBackgroundSendsEvent() {
        presenter.requestUnDimBackground();
        verify(rxBus).send(any(UndimBackgroundEvent.class));
    }

    @Test
    public void requestingDimBackgroundSendsEvent() {
        presenter.requestDimBackground();
        verify(rxBus).send(any(DimBackgroundEvent.class));
    }

}
