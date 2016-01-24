package pro.anton.averin.networking.testrest.presenters;

public interface TestRestView extends BaseView {
    void undim();

    void navigateToManagerScreenWithSave();

    void navigateToManagerScreenWithoutSave();

    void navigateToResponseScreen();
}
