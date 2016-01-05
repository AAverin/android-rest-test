package pro.anton.averin.networking.testrest.di;

import pro.anton.averin.networking.testrest.views.activities.TestRestActivity;
import pro.anton.averin.networking.testrest.views.fragments.RequestFragment;
import pro.anton.averin.networking.testrest.views.fragments.ResponseFragment;

public interface ActivityInjectsTo {

    void injectTo(TestRestActivity testRestActivity);

    void injectTo(RequestFragment requestFragment);

    void injectTo(ResponseFragment responseFragment);
}

