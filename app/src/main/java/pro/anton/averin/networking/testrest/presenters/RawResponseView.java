package pro.anton.averin.networking.testrest.presenters;

import pro.anton.averin.networking.testrest.data.models.Response;

public interface RawResponseView extends BaseView {
    void update(Response currentResponse);
}
