package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.models.Request;
import pro.anton.averin.networking.testrest.ui.adapters.RequestsAdapter;
import pro.anton.averin.networking.testrest.ui.loaders.SavedRequestsLoader;

/**
 * Created by AAverin on 17.12.13.
 */
public class EntriesManagerFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<List<Request>> {

    private View mGroupRoot;
    private View mActionBarCustomView;
    ActionBar actionBar;
    TestRestApp testRestApp;

    private LinearLayout pickANameLayout;
    private ListView entriesList;
    private RequestsAdapter entriesAdapter;
    private LinearLayout entriesLayout;
    private TextView orSelect;
    private TextView blankSlate;

    FrameLayout cancelButton;
    FrameLayout doneButton;

    private ProgressDialog progressDialog;

    private final static int LOADER_ID = 1;

    private boolean saveMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGroupRoot = inflater.inflate(R.layout.fragment_entriesmanager, container, false);

        actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        LayoutInflater abInflater = (LayoutInflater) actionBar.getThemedContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mActionBarCustomView = abInflater.inflate(R.layout.actionbar_custom_view_done_discard, null);

        doneButton = (FrameLayout) mActionBarCustomView.findViewById(R.id.actionbar_done);
        cancelButton = (FrameLayout) mActionBarCustomView.findViewById(R.id.actionbar_discard);

        pickANameLayout = (LinearLayout) mGroupRoot.findViewById(R.id.pickname_layout);
        pickANameLayout.setVisibility(View.GONE);
        entriesLayout = (LinearLayout) mGroupRoot.findViewById(R.id.entries_layout);
        orSelect = (TextView) mGroupRoot.findViewById(R.id.or_select);
        blankSlate = (TextView) mGroupRoot.findViewById(R.id.blank_slate);

        entriesAdapter = new RequestsAdapter(getActivity());
        entriesList = (ListView) mGroupRoot.findViewById(R.id.entries_list);
        entriesList.setAdapter(entriesAdapter);

        progressDialog = ProgressDialog.show(getActivity(), getString(R.string.loading), getString(R.string.please_wait), true);

        return mGroupRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        testRestApp = (TestRestApp) getActivity().getApplicationContext();
        saveMode = getActivity().getIntent().getBooleanExtra("save", false);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        updateUI();
    }

    private void updateUI() {
        if (saveMode) {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.action_save));
            pickANameLayout.setVisibility(View.VISIBLE);

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setCustomView(mActionBarCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            doneButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        } else {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.action_load));
            orSelect.setText(R.string.select_to_load);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_done:
                testRestApp.currentRequest = testRestApp.restTestDb.addRequest(testRestApp.currentRequest);
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                break;

            case R.id.actionbar_discard:
                getActivity().finish();
                break;
        }
    }

    @Override
    public Loader<List<Request>> onCreateLoader(int i, Bundle bundle) {
        return new SavedRequestsLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Request>> listLoader, List<Request> requests) {
        entriesAdapter.setData(requests);
        entriesAdapter.notifyDataSetChanged();
        if (requests == null || requests.size() == 0) {
            if (saveMode) {
                orSelect.setVisibility(View.GONE);
                entriesList.setVisibility(View.GONE);
                blankSlate.setVisibility(View.VISIBLE);
            }
        } else {
            orSelect.setVisibility(View.VISIBLE);
            entriesList.setVisibility(View.VISIBLE);
            blankSlate.setVisibility(View.GONE);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Request>> listLoader) {
        entriesAdapter.setData(null);
    }
}
