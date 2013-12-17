package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
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

    private LinearLayout pickANameLayout;
    private ListView entriesList;
    private RequestsAdapter entriesAdapter;
    private LinearLayout entriesLayout;
    private TextView orSelect;
    private TextView blankSlate;

    FrameLayout cancelButton;
    FrameLayout doneButton;

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

        return mGroupRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        saveMode = getActivity().getIntent().getBooleanExtra("save", false);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        updateUI();
    }

    private void updateUI() {
        if (saveMode) {
            pickANameLayout.setVisibility(View.VISIBLE);

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setCustomView(mActionBarCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            doneButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_done:
                break;

            case R.id.actionbar_discard:
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
    }

    @Override
    public void onLoaderReset(Loader<List<Request>> listLoader) {
        entriesAdapter.setData(null);
    }
}
