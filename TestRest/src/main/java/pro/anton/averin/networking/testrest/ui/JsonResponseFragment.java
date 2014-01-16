package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.ui.views.jsonviewer.JsonTreeViewer;

/**
 * Created by AAverin on 07.12.13.
 */
public class JsonResponseFragment extends ResponseTabFragment {

    private Activity activity;
    private TestRestApp testRestApp;
    private JsonTreeViewer jsonTreeViewer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        testRestApp = (TestRestApp) activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        jsonTreeViewer = new JsonTreeViewer(activity);

        update();

        return jsonTreeViewer;
    }

    public void update() {

        if (testRestApp.currentResponse == null) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(testRestApp.currentResponse.body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            jsonTreeViewer.setJSONObject(jsonObject);
            jsonTreeViewer.showTree();
        }

    }

}
