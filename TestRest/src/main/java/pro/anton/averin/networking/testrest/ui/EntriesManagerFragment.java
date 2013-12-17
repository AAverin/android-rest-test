package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 17.12.13.
 */
public class EntriesManagerFragment extends Fragment {

    private View mGroupRoot;
    private View mActionBarCustomView;
    ActionBar actionBar;

    private LinearLayout pickANameLayout;
    private ListView entriesList;

    FrameLayout cancelButton;
    FrameLayout doneButton;

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
        entriesList = (ListView) mGroupRoot.findViewById(R.id.entries_list);

        return mGroupRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        saveMode = getActivity().getIntent().getBooleanExtra("save", false);
    }

    private void updateUI() {
        if (saveMode) {
            pickANameLayout.setVisibility(View.VISIBLE);

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                    | ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setCustomView(mActionBarCustomView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
