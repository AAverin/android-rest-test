package pro.anton.averin.networking.testrest.legacy;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.legacy.views.ExpandableContentRow;

/**
 * Created by AAverin on 07.12.13.
 */
public class RawResponseFragment extends ResponseTabFragment {

    private TestRestApp testRestApp;
    private Activity activity;

    private ExpandableContentRow headersRow;
    private ExpandableContentRow bodyRow;

    private View mGroupRoot;
    @Override
    public View getView() {
        return mGroupRoot;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.testRestApp = (TestRestApp)activity.getApplicationContext();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGroupRoot =  inflater.inflate(R.layout.fragment_raw_response, container, false);
        return mGroupRoot;
    }

    private void init() {
        headersRow = (ExpandableContentRow) getView().findViewById(R.id.headers_row);
        bodyRow = (ExpandableContentRow) getView().findViewById(R.id.body_row);

        update();
    }

    public void update() {
        if (testRestApp.currentResponse == null) {
            return;
        }
        Map<String, List<String>> headers = testRestApp.currentResponse.headers;
        StringBuffer htmlHeaders = new StringBuffer();
        htmlHeaders.append("<b>");
        htmlHeaders.append(testRestApp.currentResponse.method);
        htmlHeaders.append("</b>");
        htmlHeaders.append(" ");
        htmlHeaders.append(testRestApp.currentResponse.url);
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
        if (testRestApp.currentResponse.body == null) {
            bodyTextView.setText(getString(R.string.empty_response));
        } else {
            bodyTextView.setText(testRestApp.currentResponse.body);
        }
        bodyRow.setContent(bodyTextView);
    }
}
