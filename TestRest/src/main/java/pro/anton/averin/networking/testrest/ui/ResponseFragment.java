package pro.anton.averin.networking.testrest.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.net.URI;

import aaverin.android.net.NetworkListener;
import aaverin.android.net.NetworkMessage;
import aaverin.android.net.NetworkResponse;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
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

        testRestApp.networkManager.putMessage(message);
        testRestApp.networkManager.releaseQueue();
    }

    private Response buildResponse(NetworkResponse responseMessage) {
        Response response = new Response();
        return response;
    }


    @Override
    protected String getPageTitle() {
        return getString(R.string.responseViewTitle);
    }

    @Override
    protected void onPageSelected() {
        testRestApp = (TestRestApp)getActivity().getApplicationContext();
        if (testRestApp.currentResponse != null) {
            sendMessage();
        } else {
            showResponse();
        }
    }

    private void showResponse() {
        progressbarLayout.setVisibility(View.GONE);
        responseLayout.setVisibility(View.VISIBLE);
        ((ResponseTabFragment)getChildFragmentManager().findFragmentByTag("rawResponse")).update();
        ((ResponseTabFragment)getChildFragmentManager().findFragmentByTag("jsonResponse")).update();
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
        testRestApp.currentResponse = buildResponse(response);
        showResponse();
    }

    @Override
    public void requestFail(NetworkMessage message, NetworkResponse response) {
        testRestApp.currentResponse = buildResponse(response);
        showResponse();
    }

    @Override
    public void requestProgress(NetworkMessage message) {

    }
}
