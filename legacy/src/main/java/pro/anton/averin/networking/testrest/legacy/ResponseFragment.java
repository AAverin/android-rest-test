package pro.anton.averin.networking.testrest.legacy;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.anton.averin.networking.testrest.Config;
import pro.anton.averin.networking.testrest.data.models.RequestHeader;
import pro.anton.averin.networking.testrest.data.models.Response;

/**
 * Created by AAverin on 09.11.13.
 */
public class ResponseFragment extends ViewPagerFragment {

    ShareActionProvider shareActionProvider;
    Intent shareIntent = null;
    private Activity activity;
    private FragmentTabHost mTabHost;
    private FragmentManager fragmentManager;
    private LinearLayout responseLayout;
    private LinearLayout progressbarLayout;
    private LinearLayout noDataLayout;
    private View mGroupRoot;
    @Override
    public View getView() {
        return mGroupRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        ((FragmentActivity)activity).supportInvalidateOptionsMenu();
        fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();

        testRestApp = (TestRestApp)activity.getApplicationContext();
        testRestApp.networkManager.subscribe(this);

        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot =  inflater.inflate(R.layout.fragment_response, null);
        return mGroupRoot;
    }

    public void init() {
        responseLayout = (LinearLayout) getView().findViewById(R.id.response_layout);
        progressbarLayout = (LinearLayout) getView().findViewById(R.id.progressbar_layout);
        noDataLayout = (LinearLayout) getView().findViewById(R.id.nodata_layout);

        mTabHost = (FragmentTabHost) getView().findViewById(R.id.tabhost);
        mTabHost.setup(activity, ((FragmentActivity)activity).getSupportFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(mTabHost.newTabSpec("rawResponse")
                .setIndicator(getString(R.string.raw_response)), RawResponseFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("jsonResponse")
                .setIndicator(getString(R.string.json_response)), JsonResponseFragment.class, null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                JsonResponseFragment jsonResponseFragment = ((JsonResponseFragment)((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag("jsonResponse"));
                if (jsonResponseFragment != null) {
                    jsonResponseFragment.cancel();
                }
            }
        });

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
        shareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
        if (shareIntent == null) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/html");
        }
        shareActionProvider.setShareIntent(shareIntent);
    }

    private void sendMessage() {
        message = new NetworkMessage(true);
        message.setURI(URI.create(testRestApp.currentRequest.asURI()));
        message.setMethod(testRestApp.currentRequest.method);
        HashMap<String, String> headers = new HashMap<String, String>();
        if (testRestApp.currentRequest.headers != null && testRestApp.currentRequest.headers.size() > 0) {
            for (RequestHeader header : testRestApp.currentRequest.headers) {
                headers.put(header.name, header.value);
            }
        }
        message.setHeaders(headers);

        testRestApp.networkManager.putMessage(message);
        testRestApp.networkManager.releaseQueue();

        noDataLayout.setVisibility(View.GONE);
        progressbarLayout.setVisibility(View.VISIBLE);
        responseLayout.setVisibility(View.GONE);
    }

    private Response buildResponse(NetworkMessage request, NetworkResponse responseMessage) {
        Response response = new Response();
        if (NetworkManager.NETWORK_MANAGER_CORE == NetworkManager.NetworkManagerCore.HTTPURLCONNECTION) {
            HttpUrlConnectionResponse networkResponse = (HttpUrlConnectionResponse)responseMessage;
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
        if (testRestApp.currentRequest != null && testRestApp.currentResponse == null) {
            sendMessage();
        } else if (testRestApp.currentResponse != null) {
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
        subject.append(testRestApp.currentResponse.url);
        subject.append(" via TestRest Android");

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());

        StringBuffer htmlHeaders = new StringBuffer();
        htmlHeaders.append("<b>");
        htmlHeaders.append(testRestApp.currentResponse.method);
        htmlHeaders.append("</b>");
        htmlHeaders.append(" ");
        htmlHeaders.append(testRestApp.currentResponse.url);
        htmlHeaders.append("<br/>");
        Map<String, List<String>> headers = testRestApp.currentResponse.headers;
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
                    tempFileForBody = File.createTempFile("testrest", testRestApp.currentRequest.name, storageDir);
                    FileOutputStream fos = new FileOutputStream(tempFileForBody);
                    fos.write(testRestApp.currentResponse.body.getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "Unable to save file to external storage", Toast.LENGTH_LONG).show();
            }


            if (tempFileForBody == null) {
                body.append(testRestApp.currentResponse.body);
            } else {
                body.append("see in attachment");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFileForBody));
            }
        } else {
            body.append(testRestApp.currentResponse.body);
        }

        if (testRestApp.currentResponse != null && testRestApp.currentResponse.body != null && Config.isBugsenseEnabled) {
            BugSenseHandler.removeCrashExtraData("responseBodyLength");
            BugSenseHandler.addCrashExtraData("responseBodyLength", String.valueOf(testRestApp.currentResponse.body.length()));
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
        ResponseTabFragment rawResponseFragment = ((ResponseTabFragment)((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag("rawResponse"));
        if (rawResponseFragment != null) { //may be null if not yet selected
            rawResponseFragment.update();
        }
        ResponseTabFragment jsonResponseFragment = ((ResponseTabFragment)((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag("jsonResponse"));
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
        testRestApp.currentResponse = buildResponse(message, response);
        showResponse();
    }

    @Override
    public void requestFail(NetworkMessage message, NetworkResponse response) {
        testRestApp.currentResponse = buildResponse(message, response);
        showResponse();
    }

    @Override
    public void requestProgress(NetworkMessage message) {

    }
}
