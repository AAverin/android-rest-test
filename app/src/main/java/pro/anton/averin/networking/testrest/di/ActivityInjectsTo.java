package pro.anton.averin.networking.testrest.di;

import pro.anton.averin.networking.testrest.views.activities.TestRestActivity;
import pro.anton.averin.networking.testrest.views.activities.ToolbarActivity;
import pro.anton.averin.networking.testrest.views.androidviews.AddHeaderPopup;
import pro.anton.averin.networking.testrest.views.androidviews.AddQueryPopup;
import pro.anton.averin.networking.testrest.views.fragments.JsonResponseFragment;
import pro.anton.averin.networking.testrest.views.fragments.RequestFragment;
import pro.anton.averin.networking.testrest.views.fragments.ResponseFragment;

public interface ActivityInjectsTo {

    void injectTo(TestRestActivity testRestActivity);

    void injectTo(RequestFragment requestFragment);

    void injectTo(ResponseFragment responseFragment);

    void injectTo(AddHeaderPopup addHeaderPopup);

    void injectTo(AddQueryPopup addQueryPopup);

    void injectTo(JsonResponseFragment jsonResponseFragment);

    void injectTo(ToolbarActivity toolbarActivity);
}

