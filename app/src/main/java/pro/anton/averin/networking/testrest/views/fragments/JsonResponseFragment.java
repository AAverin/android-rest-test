package pro.anton.averin.networking.testrest.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.Response;
import pro.anton.averin.networking.testrest.presenters.JsonResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.JsonResponseView;
import pro.anton.averin.networking.testrest.views.androidviews.jsonviewer.JsonTreeViewer;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class JsonResponseFragment extends BaseViewPresenterViewpagerFragment<JsonResponsePresenter> implements JsonResponseView {

    @Inject
    JsonResponsePresenter presenter;

    @Bind(R.id.jsonResponse_progressbar_layout)
    LinearLayout progressBarLayout;
    @Bind(R.id.jsonResponse_blank_slate)
    TextView blankSlate;
    @Bind(R.id.jsonviewer_tree)
    JsonTreeViewer jsonTreeViewer;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_json_response, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    public void cancel() {
        if (jsonTreeViewer != null) {
            jsonTreeViewer.cancel();
        }
    }


    @Override
    public void update(Response currentResponse) {
        if (currentResponse == null || currentResponse.body == null) {
            blankSlate.setVisibility(View.VISIBLE);
            return;
        } else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(currentResponse.body);
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
