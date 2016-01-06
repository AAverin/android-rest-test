package pro.anton.averin.networking.testrest.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
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
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.Request;
import pro.anton.averin.networking.testrest.data.models.Response;
import pro.anton.averin.networking.testrest.presenters.ResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.ResponseView;
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
    @Bind(R.id.tabhost)
    FragmentTabHost tabHost;

    private ShareActionProvider shareActionProvider;
    private Intent shareIntent = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);

        tabHost.setup(baseContext, getBaseActivity().getSupportFragmentManager(), R.id.tabFrameLayout);
        tabHost.addTab(tabHost.newTabSpec("rawResponse")
                .setIndicator(getString(R.string.raw_response)), RawResponseFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("jsonResponse")
                .setIndicator(getString(R.string.json_response)), JsonResponseFragment.class, null);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                JsonResponseFragment jsonResponseFragment = ((JsonResponseFragment) (getBaseActivity()).getSupportFragmentManager().findFragmentByTag("jsonResponse"));
                if (jsonResponseFragment != null) {
                    jsonResponseFragment.cancel();
                }
            }
        });

        setHasOptionsMenu(true);
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
    public void updateShareIntent(Response currentResponse, Request currentRequest) {
        Activity activity = getBaseActivity();

        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");

        StringBuilder subject = new StringBuilder();
        subject.append("Response to ");
        subject.append(currentResponse.url);
        subject.append(" via TestRest Android");

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());

        StringBuffer htmlHeaders = new StringBuffer();
        htmlHeaders.append("<b>");
        htmlHeaders.append(currentResponse.method);
        htmlHeaders.append("</b>");
        htmlHeaders.append(" ");
        htmlHeaders.append(currentResponse.url);
        htmlHeaders.append("<br/>");
        Map<String, List<String>> headers = currentResponse.headers;
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

        StringBuilder body = new StringBuilder();
        body.append("<h1>Headers</h1>");
        body.append(htmlHeaders.toString());
        body.append("<h1>Response body</h1>");

        String state = Environment.getExternalStorageState();
        File tempFileForBody = null;
        boolean isBodyLong = body.length() > 500;
        if (isBodyLong) {

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                //we can write to external storage
                String root = activity.getExternalCacheDir().getAbsolutePath();
                File storageDir = new File(root + File.separator + "TestRest");
                storageDir.mkdirs();
                try {
                    tempFileForBody = File.createTempFile("testrest", currentRequest.name, storageDir);
                    FileOutputStream fos = new FileOutputStream(tempFileForBody);
                    fos.write(currentResponse.body.getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Unable to save file to external storage", Toast.LENGTH_LONG).show();
            }


            if (tempFileForBody == null) {
                body.append(currentResponse.body);
            } else {
                body.append("see in attachment");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFileForBody));
            }
        } else {
            body.append(currentResponse.body);
        }

        String htmlBody = Html.fromHtml(body.toString()).toString();
        shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, htmlBody);
        shareIntent.putExtra(Intent.EXTRA_TEXT, htmlBody);
        shareActionProvider.setShareIntent(shareIntent);
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
}
