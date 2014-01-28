package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.ui.views.jsonviewer.JsonTreeViewer;

/**
 * Created by AAverin on 07.12.13.
 */
public class JsonResponseFragment extends ResponseTabFragment {

    private Activity activity;
    private TestRestApp testRestApp;
    private JsonTreeViewer jsonTreeViewer;
    LinearLayout progressBarLayout;
    TextView blankSlate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        testRestApp = (TestRestApp) activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mGroupRoot = inflater.inflate(R.layout.fragment_json_response, container, false);

        jsonTreeViewer = (JsonTreeViewer) mGroupRoot.findViewById(R.id.jsonviewer_tree);
        blankSlate = (TextView) mGroupRoot.findViewById(R.id.jsonResponse_blank_slate);
        progressBarLayout = (LinearLayout) mGroupRoot.findViewById(R.id.jsonResponse_progressbar_layout);

        update();

        return mGroupRoot;
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
