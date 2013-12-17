package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.models.Headers;
import pro.anton.averin.networking.testrest.models.Request;
import pro.anton.averin.networking.testrest.models.RequestHeader;
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
public class RequestFragment extends ViewPagerFragment implements TokenizedEditText.TokenListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private TestRestApp testRestApp;

    private View mGroupRoot;

    public final static int PICK_FILE_INTENT_ID = 11;

    private ProtocolToggleButton protocolToggleButton;
    private TokenizedEditText methodUrlEditText;
    private TextView addQueryButton;
    private TextView addHeadersButton;

    private Button sendButton;

    private RadioGroup methodRadioGroup;
    private EditText baseUrlEditText;
    private LinearLayout postLayout;
    private CheckBox useFileCheckbox;
    private TextView pickFileButton;
    private EditText postBody;

    private AdaptableLinearLayout addedHeadersList;
    private AddedHeadersAdapter addedHeadersAdapter;
    private ArrayList<RequestHeader> headersList = new ArrayList<RequestHeader>();

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
    public void onCreate(Bundle savedInstanceState) {
        testRestApp = (TestRestApp)getActivity().getApplicationContext();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_request, null);

        protocolToggleButton = (ProtocolToggleButton) mGroupRoot.findViewById(R.id.protocol_button);
        protocolToggleButton.setOnClickListener(protocolToggleListener);

        methodRadioGroup = (RadioGroup) mGroupRoot.findViewById(R.id.method_radiogroup);
        methodRadioGroup.setOnCheckedChangeListener(this);

        baseUrlEditText = (EditText) mGroupRoot.findViewById(R.id.baseurl);

        postLayout = (LinearLayout) mGroupRoot.findViewById(R.id.post_layout);
        useFileCheckbox = (CheckBox) postLayout.findViewById(R.id.use_file_checkbox);
        useFileCheckbox.setOnCheckedChangeListener(this);
        pickFileButton = (TextView) postLayout.findViewById(R.id.pick_file_button);
        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fileChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileChooserIntent.setType("*/*");
                startActivityForResult(Intent.createChooser(fileChooserIntent, "Pick a file"), PICK_FILE_INTENT_ID);
            }
        });
        postBody = (EditText) postLayout.findViewById(R.id.post_body);

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
        sendButton = (Button) mGroupRoot.findViewById(R.id.btn_send);
        sendButton.setOnClickListener(this);

        return mGroupRoot;
    }

    AddQueryPopup.QueryPopupListener queryPopupListener = new AddQueryPopup.QueryPopupListener() {
        @Override
        public void onOk(String key, String value) {
            StringBuffer newValue = new StringBuffer();
            newValue.append(methodUrlEditText.getText().toString());
            if (methodUrlEditText.getText().toString().length() > 0) {
                newValue.append("&");
            } else {
                newValue.append("?");
            }
            newValue.append(key);
            newValue.append("=");
            newValue.append(value);
            methodUrlEditText.setText(newValue.toString());
        }
    };

    AddHeaderPopup.HeaderPopupListener headerPopupListener = new AddHeaderPopup.HeaderPopupListener() {
        @Override
        public void onOk(String key, String value) {
            headersList.add(new RequestHeader(key, value));
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
    protected void onPageSelected() {

    }

    @Override
    public ClickableSpan onCreateTokenSpan(String chip) {
        return new QuerySpan(chip);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (!validate()) {
                    Toast.makeText(getActivity(), getString(R.string.error_emptyFields), 3000).show();
                    baseUrlEditText.requestFocus();
                    return;
                }
                testRestApp.currentResponse = null;
                testRestApp.currentRequest = buildRequest();
                TestRestFragment p = (TestRestFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MAIN");
                p.showResponsePage();
                break;
        }
    }

    private boolean validate() {
        return baseUrlEditText.getText().length() > 0;
    }

    private Request buildRequest() {
        request.protocol = protocolToggleButton.getText().toString();
        request.baseUrl = baseUrlEditText.getText().toString();
        RadioButton radioButton = (RadioButton) mGroupRoot.findViewById(methodRadioGroup.getCheckedRadioButtonId());
        request.method = radioButton.getText().toString();
        request.queryString = methodUrlEditText.getText().toString();
        request.headers = headersList;
        return request;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
        switch (checkedButtonId) {
            case R.id.checkbox_post:
                postLayout.setVisibility(View.VISIBLE);
                break;
            default:
                postLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        postBody.setText("");
        if (b) {
            pickFileButton.setVisibility(View.VISIBLE);
            postBody.setHint(R.string.post_body_file_hint);
        } else {
            pickFileButton.setVisibility(View.GONE);
            postBody.setHint(R.string.post_body_hint);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_INTENT_ID) {
                String s = data.getDataString();
                Uri u = data.getData();
                postBody.setText(data.getDataString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
