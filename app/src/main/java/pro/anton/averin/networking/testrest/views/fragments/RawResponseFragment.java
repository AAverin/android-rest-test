package pro.anton.averin.networking.testrest.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.Response;
import pro.anton.averin.networking.testrest.presenters.RawResponsePresenter;
import pro.anton.averin.networking.testrest.presenters.RawResponseView;
import pro.anton.averin.networking.testrest.views.androidviews.ExpandableRow;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;

public class RawResponseFragment extends BaseViewPresenterViewpagerFragment<RawResponsePresenter> implements RawResponseView {

    @Inject
    RawResponsePresenter presenter;

    @Bind(R.id.headers_row)
    ExpandableRow headersRow;
    @Bind(R.id.body_row)
    ExpandableRow bodyRow;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_raw_response, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
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
}
