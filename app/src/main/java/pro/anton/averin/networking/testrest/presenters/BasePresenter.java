package pro.anton.averin.networking.testrest.presenters;

public interface BasePresenter<B extends BaseView> {

    void setView(B view);

    void onCreate();

    void onResume();

    void onPause();

    void onDestroy();
}
