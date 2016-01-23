package pro.anton.averin.networking.testrest.resolution;

import java.net.ConnectException;

import javax.inject.Inject;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.di.ActivityScope;

@ActivityScope
public class UIResolution extends BaseResolution {

    @Inject
    UIResolver uiResolver;

    @Inject
    public UIResolution() {

    }

    @Override
    public void handle(ConnectException connectException) {
        uiResolver.snackBar(R.string.error_connected_exception);
    }

    @Override
    public void success() {

    }
}
