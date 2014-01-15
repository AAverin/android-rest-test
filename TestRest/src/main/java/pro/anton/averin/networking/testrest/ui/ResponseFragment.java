package pro.anton.averin.networking.testrest.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.net.URI;
import java.util.HashMap;

import aaverin.android.net.HttpUrlConnectionResponse;
import aaverin.android.net.NetworkListener;
import aaverin.android.net.NetworkManager;
import aaverin.android.net.NetworkMessage;
import aaverin.android.net.NetworkResponse;
import aaverin.android.net.NetworkResponseProcessException;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.models.Headers;
import pro.anton.averin.networking.testrest.models.RequestHeader;
import pro.anton.averin.networking.testrest.models.Response;

/**
 * Created by AAverin on 09.11.13.
 */
public class ResponseFragment extends ViewPagerFragment implements NetworkListener {

    private TestRestApp testRestApp;
    private View mGroupRoot;
    private FragmentTabHost mTabHost;
    private NetworkMessage message;

    private LinearLayout responseLayout;
    private LinearLayout progressbarLayout;
    private LinearLayout noDataLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        testRestApp = (TestRestApp)getActivity().getApplicationContext();
        testRestApp.networkManager.subscribe(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_response, null);

        responseLayout = (LinearLayout) mGroupRoot.findViewById(R.id.response_layout);
        progressbarLayout = (LinearLayout) mGroupRoot.findViewById(R.id.progressbar_layout);
        noDataLayout = (LinearLayout) mGroupRoot.findViewById(R.id.nodata_layout);

        mTabHost = (FragmentTabHost) mGroupRoot.findViewById(R.id.tabhost);
        mTabHost.setup(getActivity(), getActivity().getSupportFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(mTabHost.newTabSpec("rawResponse")
                .setIndicator(getString(R.string.raw_response)), RawResponseFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("jsonResponse")
                .setIndicator(getString(R.string.json_response)), JsonResponseFragment.class, null);

        return mGroupRoot;
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
        if (testRestApp.currentRequest != null && testRestApp.currentResponse == null) {
            sendMessage();
        } else if (testRestApp.currentResponse != null) {
            showResponse();
        }
    }

    private void showResponse() {
        noDataLayout.setVisibility(View.GONE);
        progressbarLayout.setVisibility(View.GONE);
        responseLayout.setVisibility(View.VISIBLE);
        ResponseTabFragment rawResponseFragment = ((ResponseTabFragment)getActivity().getSupportFragmentManager().findFragmentByTag("rawResponse"));
        if (rawResponseFragment != null) { //may be null if not yet selected
            rawResponseFragment.update();
        }
        ResponseTabFragment jsonResponseFragment = ((ResponseTabFragment)getActivity().getSupportFragmentManager().findFragmentByTag("jsonResponse"));
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
