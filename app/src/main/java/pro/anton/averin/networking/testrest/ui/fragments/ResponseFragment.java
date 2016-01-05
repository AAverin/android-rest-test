package pro.anton.averin.networking.testrest.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aaverin.android.net.HttpUrlConnectionResponse;
import aaverin.android.net.NetworkListener;
import aaverin.android.net.NetworkManager;
import aaverin.android.net.NetworkMessage;
import aaverin.android.net.NetworkResponse;
import aaverin.android.net.NetworkResponseProcessException;
import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.Config;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.RequestHeader;
import pro.anton.averin.networking.testrest.data.models.Response;

/**
 * Created by AAverin on 09.11.13.
 */
public class ResponseFragment extends ViewPagerFragment implements NetworkListener {

    ShareActionProvider shareActionProvider;
    Intent shareIntent = null;
    private Activity activity;
    private BaseContext baseContext;
    private FragmentTabHost mTabHost;
    private NetworkMessage message;
    private FragmentManager fragmentManager;
    private LinearLayout responseLayout;
    private LinearLayout progressbarLayout;
    private LinearLayout noDataLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        ((FragmentActivity) activity).supportInvalidateOptionsMenu();
        fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();

        baseContext = (BaseContext) activity.getApplicationContext();
        baseContext.networkManager.subscribe(this);

        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_response, null);

        activity = getActivity();
        if (activity != null) {
            mTabHost = (FragmentTabHost) contentView.findViewById(R.id.tabhost);
            mTabHost.setup(activity, ((FragmentActivity) activity).getSupportFragmentManager(), R.id.tabFrameLayout);

            mTabHost.addTab(mTabHost.newTabSpec("rawResponse")
                    .setIndicator(getString(R.string.raw_response)), RawResponseFragment.class, null);
            mTabHost.addTab(mTabHost.newTabSpec("jsonResponse")
                    .setIndicator(getString(R.string.json_response)), JsonResponseFragment.class, null);
            mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    JsonResponseFragment jsonResponseFragment = ((JsonResponseFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("jsonResponse"));
                    if (jsonResponseFragment != null) {
                        jsonResponseFragment.cancel();
                    }
                }
            });
        }
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void init() {
        responseLayout = (LinearLayout) contentView.findViewById(R.id.response_layout);
        progressbarLayout = (LinearLayout) contentView.findViewById(R.id.progressbar_layout);
        noDataLayout = (LinearLayout) contentView.findViewById(R.id.nodata_layout);


        setHasOptionsMenu(true);
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

    private void sendMessage() {
        message = new NetworkMessage(true);
        message.setURI(URI.create(baseContext.currentRequest.asURI()));
        message.setMethod(baseContext.currentRequest.method);
        HashMap<String, String> headers = new HashMap<String, String>();
        if (baseContext.currentRequest.headers != null && baseContext.currentRequest.headers.size() > 0) {
            for (RequestHeader header : baseContext.currentRequest.headers) {
                headers.put(header.name, header.value);
            }
        }
        message.setHeaders(headers);

        baseContext.networkManager.putMessage(message);
        baseContext.networkManager.releaseQueue();

        noDataLayout.setVisibility(View.GONE);
        progressbarLayout.setVisibility(View.VISIBLE);
        responseLayout.setVisibility(View.GONE);
    }

    private Response buildResponse(NetworkMessage request, NetworkResponse responseMessage) {
        Response response = new Response();
        if (NetworkManager.NETWORK_MANAGER_CORE == NetworkManager.NetworkManagerCore.HTTPURLCONNECTION) {
            HttpUrlConnectionResponse networkResponse = (HttpUrlConnectionResponse) responseMessage;
            response.method = request.getMethod();
            response.url = request.getURI().toString();
            response.status = networkResponse.getStatus();
            response.headers = networkResponse.getHeaders();
            try {
                response.body = networkResponse.getResponseBody();
            } catch (NetworkResponseProcessException e) {
                e.printStackTrace();
            }
        }

        return response;
    }


    @Override
    protected String getPageTitle() {
        return getString(R.string.responseViewTitle);
    }

    @Override
    protected void onPageSelected() {
        if (Config.adsEnabled) {
            displayAds();
        }
        if (baseContext.currentRequest != null && baseContext.currentResponse == null) {
            sendMessage();
        } else if (baseContext.currentResponse != null) {
            showResponse();
        }
    }

    private void displayAds() {
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        Fragment oldDialog = fragmentManager.findFragmentByTag(AdsDialog.FRAGMENT_TAG);
//        if (oldDialog != null) {
//            transaction.remove(oldDialog);
//        }
//        transaction.addToBackStack(null);
//
//        DialogFragment dialog = new AdsDialog();
//
//        dialog.show(fragmentManager, AdsDialog.FRAGMENT_TAG);

//        String android_id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
//        String deviceId = TestRestApp.md5(android_id).toUpperCase();
//
//        final InterstitialAd interstitialAd = new InterstitialAd(activity);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("8FA8176CAE5DE5E252B7055C4D5BB0A7")
//                .build();
//
//        interstitialAd.setAdUnitId("ca-app-pub-8295594407653786/4276744952");
//
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                interstitialAd.show();
//            }
//        });
//
//        interstitialAd.loadAd(adRequest);
    }

    private void showResponse() {
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");

        StringBuilder subject = new StringBuilder();
        subject.append("Response to ");
        subject.append(baseContext.currentResponse.url);
        subject.append(" via TestRest Android");

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());

        StringBuffer htmlHeaders = new StringBuffer();
        htmlHeaders.append("<b>");
        htmlHeaders.append(baseContext.currentResponse.method);
        htmlHeaders.append("</b>");
        htmlHeaders.append(" ");
        htmlHeaders.append(baseContext.currentResponse.url);
        htmlHeaders.append("<br/>");
        Map<String, List<String>> headers = baseContext.currentResponse.headers;
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
                    tempFileForBody = File.createTempFile("testrest", baseContext.currentRequest.name, storageDir);
                    FileOutputStream fos = new FileOutputStream(tempFileForBody);
                    if (baseContext.currentResponse.body != null)
                        fos.write(baseContext.currentResponse.body.getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Unable to save file to external storage", Toast.LENGTH_LONG).show();
            }


            if (tempFileForBody == null) {
                body.append(baseContext.currentResponse.body);
            } else {
                body.append("see in attachment");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFileForBody));
            }
        } else {
            body.append(baseContext.currentResponse.body);
        }

        if (baseContext.currentResponse != null && baseContext.currentResponse.body != null && Config.isCrashlyticsEnabled) {
            Crashlytics.setString("responseBodyLength", String.valueOf(baseContext.currentResponse.body.length()));
        }

        String htmlBody = Html.fromHtml(body.toString()).toString();
        shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, htmlBody);
        shareIntent.putExtra(Intent.EXTRA_TEXT, htmlBody);
        if (shareActionProvider != null) { //may be null if we not yet fully restored after configuration change
            shareActionProvider.setShareIntent(shareIntent);
        }

        noDataLayout.setVisibility(View.GONE);
        progressbarLayout.setVisibility(View.GONE);
        responseLayout.setVisibility(View.VISIBLE);
        ResponseTabFragment rawResponseFragment = ((ResponseTabFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("rawResponse"));
        if (rawResponseFragment != null) { //may be null if not yet selected
            rawResponseFragment.update();
        }
        ResponseTabFragment jsonResponseFragment = ((ResponseTabFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("jsonResponse"));
        if (jsonResponseFragment != null) {
            jsonResponseFragment.update();
        }

    }


    @Override
    public void queueStart() {

    }

    @Override
    public void queueFinish() {

    }

    @Override
    public void queueFailed() {

    }

    @Override
    public void requestStart(NetworkMessage message) {

    }

    @Override
    public void requestSuccess(NetworkMessage message, NetworkResponse response) {
        baseContext.currentResponse = buildResponse(message, response);
        showResponse();
    }

    @Override
    public void requestFail(NetworkMessage message, NetworkResponse response) {
        baseContext.currentResponse = buildResponse(message, response);
        showResponse();
    }

    @Override
    public void requestProgress(NetworkMessage message) {

    }
}
