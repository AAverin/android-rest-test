package pro.anton.averin.networking.testrest.ui.loaders;

import android.content.Context;

import java.util.List;

import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.models.Request;

/**
 * Created by AAverin on 17.12.13.
 */
public class SavedRequestsLoader extends AbstractDataLoader<List<Request>> {

    private TestRestApp testRestApp;

    public SavedRequestsLoader(Context context) {
        super(context);
        testRestApp = (TestRestApp) context;
    }

    @Override
    protected List<Request> buildList() {
        return testRestApp.restTestDb.getRequests();
    }
}
