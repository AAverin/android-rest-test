package pro.anton.averin.networking.testrest.ui;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.zip.Inflater;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.models.Headers;
import pro.anton.averin.networking.testrest.models.Request;
import pro.anton.averin.networking.testrest.ui.adapters.AddedHeadersAdapter;
import pro.anton.averin.networking.testrest.ui.dialogs.AddHeaderPopup;
import pro.anton.averin.networking.testrest.ui.dialogs.AddQueryPopup;
import pro.anton.averin.networking.testrest.ui.views.AdaptableLinearLayout;
import pro.anton.averin.networking.testrest.ui.views.ProtocolToggleButton;
import pro.anton.averin.networking.testrest.ui.dialogs.QueryMenuPopupWindow;
import pro.anton.averin.networking.testrest.ui.views.TokenizedEditText;

/**
 * Created by AAverin on 09.11.13.
 */
public class RequestFragment extends ViewPagerFragment implements TokenizedEditText.TokenListener {

    private View mGroupRoot;

    private ProtocolToggleButton protocolToggleButton;
    private TokenizedEditText methodUrlEditText;
    private TextView addQueryButton;
    private TextView addHeadersButton;

    private RadioGroup methodRadioGroup;
    private EditText baseUrlEditText;
    private AdaptableLinearLayout addedHeadersList;
    private AddedHeadersAdapter addedHeadersAdapter;
    private ArrayList<Headers.ViewHeader> headersList = new ArrayList<Headers.ViewHeader>();

    private Request request = new Request();

    View.OnClickListener protocolToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            protocolToggleButton.toggleProtocol();
        }
    };

    QueryMenuPopupWindow.ChipListener chipListener = new QueryMenuPopupWindow.ChipListener() {

        @Override
        public void onChipDeleted(QuerySpan chip) {
            Toast.makeText(getActivity(), "chip deleted " + chip.chip, 5).show();
        }
    };

    public RequestFragment() {
    }

    public class QuerySpan extends ClickableSpan {

        public String chip;
        public QuerySpan(String chip) {
            this.chip = chip;
        }

        @Override
        public void onClick(View view) {
//            int[] location = new int[2];
//            view.getLocationOnScreen(location);
//
//            TokenizedEditText textView = (TokenizedEditText)view;
//            String text = textView.getText().toString();
//            int dx = Math.round(textView.getPaint().measureText(text.substring(0, text.indexOf(chip) + chip.length())));

//            QueryMenuPopupWindow popup = new QueryMenuPopupWindow(getActivity(), this);
//            popup.setChipListener(chipListener);
//            popup.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + dx, location[1]);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_request, null);

        protocolToggleButton = (ProtocolToggleButton) mGroupRoot.findViewById(R.id.protocol_button);
        protocolToggleButton.setOnClickListener(protocolToggleListener);

        methodRadioGroup = (RadioGroup) mGroupRoot.findViewById(R.id.method_radiogroup);
        baseUrlEditText = (EditText) mGroupRoot.findViewById(R.id.baseurl);

        addedHeadersList = (AdaptableLinearLayout) mGroupRoot.findViewById(R.id.addedheaders_list);
        addedHeadersAdapter = new AddedHeadersAdapter(getActivity().getApplicationContext(), headersList);
        addedHeadersList.setAdapter(addedHeadersAdapter);

        addQueryButton = (TextView) mGroupRoot.findViewById(R.id.add_query_button);
        addQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQueryPopup popup = new AddQueryPopup(getActivity());
                popup.setQueryPopupListener(queryPopupListener);
                popup.showAtLocation(mGroupRoot, Gravity.TOP, 0, (int)(120 * getResources().getDisplayMetrics().density));
            }
        });

        addHeadersButton = (TextView) mGroupRoot.findViewById(R.id.add_header_button);
        addHeadersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHeaderPopup popup = new AddHeaderPopup(getActivity());
                popup.setHeaderPopupListener(headerPopupListener);
                popup.showAtLocation(mGroupRoot, Gravity.TOP, 0, (int)(120 * getResources().getDisplayMetrics().density));
            }
        });

        methodUrlEditText = (TokenizedEditText) mGroupRoot.findViewById(R.id.method_url);
        methodUrlEditText.setTokenRegexp("[\\?&]([^&?]+=[^&?]+)");
        methodUrlEditText.setTokenListener(this);

        methodUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TokenizedEditText edit = ((TokenizedEditText)view);
                    if (edit.getText().length() == edit.getSelectionEnd()) {
                        highlightQueryButton();
                    }
                }
            }
        });

        return mGroupRoot;
    }

    AddQueryPopup.QueryPopupListener queryPopupListener = new AddQueryPopup.QueryPopupListener() {
        @Override
        public void onOk(String key, String value) {
            StringBuffer newValue = new StringBuffer();
            newValue.append(methodUrlEditText.getText().toString());
            newValue.append("&");
            newValue.append(key);
            newValue.append("=");
            newValue.append(value);
            methodUrlEditText.setText(newValue.toString());
        }
    };

    AddHeaderPopup.HeaderPopupListener headerPopupListener = new AddHeaderPopup.HeaderPopupListener() {
        @Override
        public void onOk(String key, String value) {
            headersList.add(new Headers.ViewHeader(key, value));
            addedHeadersAdapter.notifyDataSetChanged();
        }
    };

    private void highlightQueryButton() {
        ValueAnimator animation = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(android.R.color.black),
                getResources().getColor(R.color.valid_green));
        animation.setDuration(1000);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                addQueryButton.setTextColor((Integer) valueAnimator.getAnimatedValue());
            }
        });

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                addQueryButton.setTextColor(getResources().getColor(android.R.color.black));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animation.start();
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.requestViewTitle);
    }

    @Override
    public ClickableSpan onCreateTokenSpan(String chip) {
        return new QuerySpan(chip);
    }

    private Request buildRequest() {
        request.baseUrl = baseUrlEditText.getText().toString();
        CheckBox checkedBox = (CheckBox) mGroupRoot.findViewById(methodRadioGroup.getCheckedRadioButtonId());
        request.method = checkedBox.getText().toString();
        request.queryString = methodUrlEditText.getText().toString();

        return request;
    }
}
