package pro.anton.averin.networking.testrest.views.fragments;

import android.app.Activity;
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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.Response;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.ResponseView;
import pro.anton.averin.networking.testrest.views.androidviews.ExpandableRow;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class ResponseFragment extends BaseViewPresenterViewpagerFragment<ResponsePresenter> implements ResponseView {

    @Inject
    ResponsePresenter presenter;

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

        setHasOptionsMenu(true);
    }

    @Override
    public void update(Response currentResponse) {
        if (currentResponse == null) {
            return;
        }

        Activity activity = getBaseActivity();

        Map<String, List<String>> headers = currentResponse.headers;
        StringBuffer htmlHeaders = new StringBuffer();
        htmlHeaders.append("<b>");
        htmlHeaders.append(currentResponse.method);
        htmlHeaders.append("</b>");
        htmlHeaders.append(" ");
        htmlHeaders.append(currentResponse.url);
        htmlHeaders.append("<br/>");
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                htmlHeaders.append("<b>");
                htmlHeaders.append(key);
                htmlHeaders.append("</b>");
                List<String> values = headers.get(key);
                for (String value : values) {
                    htmlHeaders.append(" ");
                    htmlHeaders.append(value);
                }
                htmlHeaders.append("<br/>");
            }
        }
        TextView headersHtmlTextView = new TextView(activity);
        headersHtmlTextView.setText(Html.fromHtml(htmlHeaders.toString()));
        headersRow.setContent(headersHtmlTextView);

        TextView bodyTextView = new TextView(activity);
        if (currentResponse.body == null) {
            bodyTextView.setText(getString(R.string.empty_response));
        } else {
            bodyTextView.setText(currentResponse.body);
        }
        bodyRow.setContent(bodyTextView);
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
    public void updateShareIntent(Intent shareIntent) {
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
