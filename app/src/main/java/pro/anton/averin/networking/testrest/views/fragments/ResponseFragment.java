package pro.anton.averin.networking.testrest.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.ResponseView;
import pro.anton.averin.networking.testrest.views.androidviews.ExpandableRow;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class ResponseFragment extends BaseViewPresenterViewpagerFragment<ResponsePresenter> implements ResponseView {

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
    @Bind(R.id.body_row)
    ExpandableRow bodyRow;

    private ShareActionProvider shareActionProvider;
    private Intent shareIntent = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);

        inflater = getBaseActivity().getLayoutInflater();

        setHasOptionsMenu(true);
    }

    @Override
    public void setHeaders(String htmlHeaders) {
        TextView headersHtmlTextView = (TextView) inflater.inflate(R.layout.expandable_content_headers, (ViewGroup) contentView, false);
        headersHtmlTextView.setText(Html.fromHtml(htmlHeaders));
        headersRow.setContent(headersHtmlTextView);
    }

    @Override
    public void setEmptyBody() {
        TextView bodyTextView = (TextView) inflater.inflate(R.layout.expandable_content_response, (ViewGroup) contentView, false);
        bodyTextView.setText(getString(R.string.empty_response));
        bodyRow.setContent(bodyTextView);

    }

    @Override
    public void setResponseBody(String body) {
        View responseContentView = inflater.inflate(R.layout.expandable_content_response, (ViewGroup) contentView, false);
        TextView bodyTextView = (TextView) responseContentView.findViewById(R.id.content_text);
        bodyTextView.setText(body);
        bodyRow.setContent(responseContentView);
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
