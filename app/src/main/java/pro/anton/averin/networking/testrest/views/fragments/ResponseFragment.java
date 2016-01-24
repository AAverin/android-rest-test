package pro.anton.averin.networking.testrest.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.ResponseView;
import pro.anton.averin.networking.testrest.views.androidviews.ExpandableRow;
import pro.anton.averin.networking.testrest.views.base.ViewpagerPFragment;

public class ResponseFragment extends ViewpagerPFragment<ResponsePresenter> implements ResponseView {

    @Inject
    ResponsePresenter presenter;

    @Inject
    LayoutInflater inflater;

    @Bind(R.id.response_layout)
    LinearLayout responseLayout;
    @Bind(R.id.progressbar_layout)
    LinearLayout progressbarLayout;
    @Bind(R.id.nodata_layout)
    LinearLayout noDataLayout;

    @Bind(R.id.headers_row)
    ExpandableRow headersRow;
    @Bind(R.id.tv_headers)
    TextView headers;
    @Bind(R.id.body_row)
    ExpandableRow bodyRow;
    @Bind(R.id.content_text)
    TextView rawResponse;
    @Bind(R.id.format_json_switch)
    SwitchCompat formatJsonSwitch;
    @Bind(R.id.jsonviewer_tree)
    pro.anton.averin.networking.testrest.views.androidviews.jsonviewer.JsonTreeView jsonTree;

    private ShareActionProvider shareActionProvider;
    private Intent shareIntent = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);
        initPresenter(presenter, savedInstanceState);
        presenter.setView(this);

        inflater = getBaseActivity().getLayoutInflater();

        setHasOptionsMenu(true);
    }

    @Override
    public void setHeaders(String htmlHeaders) {
        headers.setText(Html.fromHtml(htmlHeaders));
        headersRow.refreshContentHeight(false);
    }

    @Override
    public void setEmptyBody() {
        rawResponse.setText(getString(R.string.empty_response));
        bodyRow.refreshContentHeight(false);
    }

    @Override
    public void setResponseBody(String body) {
        rawResponse.setText(body);
        bodyRow.refreshContentHeight(false);
    }

    @Override
    public void hideRawResponse() {
        rawResponse.setVisibility(View.GONE);
    }

    @Override
    public void showJson() {

        jsonTree.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRawResponse() {
        rawResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideJson() {
        jsonTree.setVisibility(View.GONE);
    }

    @Override
    public void enableJson() {
        formatJsonSwitch.setEnabled(true);
    }

    @Override
    public void disableJson() {
        formatJsonSwitch.setEnabled(false);
    }

    @Override
    public void showJsonConfirmationDialog() {
        new AlertDialog.Builder(getBaseActivity())
                .setTitle(R.string.label_show_formatted_json)
                .setMessage(R.string.label_formatted_json_message)
                .setPositiveButton(R.string.label_continue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.onShowJson();
                    }
                })
                .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.cancelJsonConfirmationDialog();
                    }
                })
                .create().show();
    }

    @Override
    public void turnOffJsonSwitch() {
        formatJsonSwitch.setChecked(false);
    }

    @Override
    public void setJson(JsonElement jsonObject) {
        jsonTree.setJson(jsonObject);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_response, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.response_screen_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (shareIntent == null) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/html");
        }
        shareActionProvider.setShareIntent(shareIntent);
    }

    @OnCheckedChanged(R.id.format_json_switch)
    public void onJsonSwitchToggled(boolean checked) {
        if (checked) {
            presenter.onShowJsonRequest();
        } else {
            presenter.onHideJson();
        }
    }

    @Override
    public void setShareIntent(Intent shareIntent) {
        this.shareIntent = shareIntent;
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void hideNoDataLayout() {
        noDataLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        progressbarLayout.setVisibility(View.GONE);
    }

    @Override
    public void showResponseLayout() {
        responseLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        progressbarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideResponseLayout() {
        responseLayout.setVisibility(View.GONE);
    }

    @Override
    public void displayMediaNotMountedMessage() {
        Snackbar.make(contentView, "Media is not mounted, unable to save body contents into a file", Snackbar.LENGTH_LONG).show();
    }
}
