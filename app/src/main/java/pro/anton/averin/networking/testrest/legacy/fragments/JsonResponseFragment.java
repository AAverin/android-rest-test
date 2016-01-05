package pro.anton.averin.networking.testrest.legacy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.legacy.activities.BaseActivity;
import pro.anton.averin.networking.testrest.legacy.views.jsonviewer.JsonTreeViewer;

/**
 * Created by AAverin on 07.12.13.
 */
public class JsonResponseFragment extends ResponseTabFragment {

    LinearLayout progressBarLayout;
    TextView blankSlate;
    private BaseActivity activity;
    private BaseContext baseContext;
    private JsonTreeViewer jsonTreeViewer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (BaseActivity) getActivity();
        baseContext = (BaseContext) activity.getApplicationContext();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_json_response, container, false);
        return contentView;
    }

    private void init() {
        jsonTreeViewer = (JsonTreeViewer) contentView.findViewById(R.id.jsonviewer_tree);
        blankSlate = (TextView) contentView.findViewById(R.id.jsonResponse_blank_slate);
        progressBarLayout = (LinearLayout) contentView.findViewById(R.id.jsonResponse_progressbar_layout);

        update();
    }

    public void cancel() {
        if (jsonTreeViewer != null) {
            jsonTreeViewer.cancel();
        }
    }

    public void update() {
        if (activity == null || !activity.isActive())
            return;

        if (baseContext.currentResponse == null || baseContext.currentResponse.body == null) {
            blankSlate.setVisibility(View.VISIBLE);
            return;
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(baseContext.currentResponse.body);
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
