package pro.anton.averin.networking.testrest.ui.loaders;

import android.content.Context;

import java.util.List;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.models.Request;

/**
 * Created by AAverin on 17.12.13.
 */
public class SavedRequestsLoader extends AbstractDataLoader<List<Request>> {

    private BaseContext baseContext;

    public SavedRequestsLoader(Context context) {
        super(context);
        baseContext = (BaseContext) context;
    }

    @Override
    protected List<Request> buildList() {
        return baseContext.testRestDb.getRequests();
    }
}
