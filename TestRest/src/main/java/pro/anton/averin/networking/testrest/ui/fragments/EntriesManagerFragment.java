package pro.anton.averin.networking.testrest.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.models.Request;
import pro.anton.averin.networking.testrest.ui.adapters.RequestsAdapter;
import pro.anton.averin.networking.testrest.ui.fragments.BaseFragment;
import pro.anton.averin.networking.testrest.ui.loaders.SavedRequestsLoader;
import pro.anton.averin.networking.testrest.ui.views.opensource.SwipeDismissListViewTouchListener;

/**
 * Created by AAverin on 17.12.13.
 */
public class EntriesManagerFragment extends BaseFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<List<Request>> {

    private Activity activity;

    private View mActionBarCustomView;
    ActionBar actionBar;
    BaseContext baseContext;

    private LinearLayout pickANameLayout;
    private EditText nameEditText;
    private ListView entriesList;
    private RequestsAdapter entriesAdapter;
    private LinearLayout entriesLayout;
    private TextView orSelect;
    private TextView blankSlate;
    SwipeDismissListViewTouchListener dismissTouchListener;

    FrameLayout cancelButton;
    FrameLayout doneButton;

    private ProgressDialog progressDialog;

    private final static int LOADER_ID = 1;

    private boolean saveMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_entriesmanager, container, false);
        return contentView;
    }

    private void refreshRequestsList() {
        ((FragmentActivity) activity).getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.baseContext = (BaseContext)activity.getApplicationContext();
        saveMode = activity.getIntent().getBooleanExtra("save", false);
        ((FragmentActivity) activity).getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        init();
        updateUI();
    }

    private void init() {
        actionBar = ((ActionBarActivity)activity).getSupportActionBar();
        LayoutInflater abInflater = (LayoutInflater) actionBar.getThemedContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mActionBarCustomView = abInflater.inflate(R.layout.actionbar_custom_view_done_discard, null);

        doneButton = (FrameLayout) mActionBarCustomView.findViewById(R.id.actionbar_done);
        cancelButton = (FrameLayout) mActionBarCustomView.findViewById(R.id.actionbar_discard);

        pickANameLayout = (LinearLayout) contentView.findViewById(R.id.pickname_layout);
        pickANameLayout.setVisibility(View.GONE);
        nameEditText = (EditText) pickANameLayout.findViewById(R.id.custom_name);
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                entriesList.clearChoices();
            }
        });
        entriesLayout = (LinearLayout) contentView.findViewById(R.id.entries_layout);
        orSelect = (TextView) contentView.findViewById(R.id.or_select);
        blankSlate = (TextView) contentView.findViewById(R.id.blank_slate);

        entriesAdapter = new RequestsAdapter(activity);
        entriesList = (ListView) contentView.findViewById(R.id.entries_list);
        entriesList.setAdapter(entriesAdapter);

        dismissTouchListener = new SwipeDismissListViewTouchListener(entriesList, new SwipeDismissListViewTouchListener.OnDismissCallback() {
            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                Request deletedRequest = entriesAdapter.getItem(reverseSortedPositions[0]);
                baseContext.testRestDb.deleteRequest(deletedRequest.id);
                refreshRequestsList();
            }
        });

        progressDialog = ProgressDialog.show(activity, getString(R.string.loading), getString(R.string.please_wait), true);
    }

    private void updateUI() {
        if (saveMode) {
            actionBar.setTitle(getString(R.string.action_save));
            pickANameLayout.setVisibility(View.VISIBLE);

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setCustomView(mActionBarCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            doneButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

            entriesList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            entriesList.setOnItemClickListener(entriesListItemClickListener);
        } else {
            actionBar.setTitle(getString(R.string.entriesmanager_title));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setCustomView(null);
            entriesList.clearChoices();

            pickANameLayout.setVisibility(View.GONE);
            entriesList.setOnItemClickListener(entriesListItemClickListener);
            entriesList.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
            entriesList.setOnTouchListener(dismissTouchListener);
            entriesList.setOnScrollListener(dismissTouchListener.makeScrollListener());
            orSelect.setText(R.string.select_to_load);
        }
    }

    AdapterView.OnItemClickListener entriesListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if (saveMode) {
                if (nameEditText.hasFocus()) {
                    nameEditText.clearFocus();
                }
                entriesList.setItemChecked(position, true);
            } else {
                Request selectedRequest = entriesAdapter.getItem(position);
                baseContext.currentRequest = selectedRequest;
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_done:
                int selectedItemPosition = entriesList.getCheckedItemPosition();
                if (selectedItemPosition >= 0) {
                    Request requestToUpdate = entriesAdapter.getItem(selectedItemPosition);
                    baseContext.currentRequest.name = requestToUpdate.name;
                    baseContext.testRestDb.updateRequest(baseContext.currentRequest, requestToUpdate.id);
                } else {
                    String requestName = nameEditText.getText().toString();
                    if (baseContext.testRestDb.isUniqueRequestName(requestName)) {
                        baseContext.currentRequest.name = requestName;
                        baseContext.currentRequest = baseContext.testRestDb.addRequest(baseContext.currentRequest);
                    } else {
                        Toast.makeText(activity, R.string.error_not_unique_name, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                getLoaderManager().restartLoader(LOADER_ID, null, this);
                //TODO: add a fancy animation displaying "Saved"
                saveMode = false;
                updateUI();
                break;

            case R.id.actionbar_discard:
                activity.finish();
                break;
        }
    }

    @Override
    public Loader<List<Request>> onCreateLoader(int i, Bundle bundle) {
        return new SavedRequestsLoader(activity.getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Request>> listLoader, List<Request> requests) {
        entriesAdapter.setData(requests);
        entriesAdapter.notifyDataSetChanged();
        if (requests == null || requests.size() == 0) {
            orSelect.setVisibility(View.GONE);
            entriesList.setVisibility(View.GONE);
            blankSlate.setVisibility(View.VISIBLE);
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
