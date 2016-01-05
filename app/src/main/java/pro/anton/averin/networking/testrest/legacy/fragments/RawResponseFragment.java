package pro.anton.averin.networking.testrest.legacy.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.legacy.views.ExpandableContentRow;

/**
 * Created by AAverin on 07.12.13.
 */
public class RawResponseFragment extends ResponseTabFragment {

    private BaseContext baseContext;
    private Activity activity;

    private ExpandableContentRow headersRow;
    private ExpandableContentRow bodyRow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        this.baseContext = (BaseContext) activity.getApplicationContext();
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_raw_response, container, false);
        return contentView;
    }

    private void init() {
        headersRow = (ExpandableContentRow) contentView.findViewById(R.id.headers_row);
        bodyRow = (ExpandableContentRow) contentView.findViewById(R.id.body_row);

        update();
    }

    public void update() {
        if (baseContext.currentResponse == null) {
            return;
        }
        Map<String, List<String>> headers = baseContext.currentResponse.headers;
        StringBuffer htmlHeaders = new StringBuffer();
        htmlHeaders.append("<b>");
        htmlHeaders.append(baseContext.currentResponse.method);
        htmlHeaders.append("</b>");
        htmlHeaders.append(" ");
        htmlHeaders.append(baseContext.currentResponse.url);
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
        if (baseContext.currentResponse.body == null) {
            bodyTextView.setText(getString(R.string.empty_response));
        } else {
            bodyTextView.setText(baseContext.currentResponse.body);
        }
        bodyRow.setContent(bodyTextView);
    }
}
