package pro.anton.averin.networking.testrest.legacy;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.views.androidviews.jsonviewer.JsonTreeViewer;

/**
 * Created by AAverin on 07.12.13.
 */
public class JsonResponseFragment extends ResponseTabFragment {

    LinearLayout progressBarLayout;
    TextView blankSlate;
    private Activity activity;
    private TestRestApp testRestApp;
    private JsonTreeViewer jsonTreeViewer;
    private View mGroupRoot;
    @Override
    public View getView() {
        return mGroupRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        testRestApp = (TestRestApp) activity.getApplicationContext();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot =  inflater.inflate(R.layout.fragment_json_response, container, false);
        return mGroupRoot;
    }

    private void init() {
        jsonTreeViewer = (JsonTreeViewer) getView().findViewById(R.id.jsonviewer_tree);
        blankSlate = (TextView) getView().findViewById(R.id.jsonResponse_blank_slate);
        progressBarLayout = (LinearLayout) getView().findViewById(R.id.jsonResponse_progressbar_layout);

        update();
    }

    public void cancel() {
        if (jsonTreeViewer != null) {
            jsonTreeViewer.cancel();
        }
    }

    public void update() {

        if (testRestApp.currentResponse == null || testRestApp.currentResponse.body == null) {
            blankSlate.setVisibility(View.VISIBLE);
            return;
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(testRestApp.currentResponse.body);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObject != null) {
                progressBarLayout.setVisibility(View.VISIBLE);
                jsonTreeViewer.setJSONObject(jsonObject);
                jsonTreeViewer.showTree(new JsonTreeViewer.JsonTreeViewerListener() {
                    @Override
                    public void onFinish() {
                        progressBarLayout.setVisibility(View.GONE);
                    }
                });
            } else {
                blankSlate.setVisibility(View.VISIBLE);
            }

        }
    }

}
