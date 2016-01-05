package pro.anton.averin.networking.testrest.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.crashlytics.android.Crashlytics;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.net.URI;
import java.util.ArrayList;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.Config;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.ProtocolType;
import pro.anton.averin.networking.testrest.data.models.Request;
import pro.anton.averin.networking.testrest.data.models.RequestHeader;
import pro.anton.averin.networking.testrest.ui.activities.phone.EntriesManagerActivity;
import pro.anton.averin.networking.testrest.ui.adapters.AddedHeadersAdapter;
import pro.anton.averin.networking.testrest.ui.dialogs.AddHeaderDialog;
import pro.anton.averin.networking.testrest.ui.dialogs.AddQueryDialog;
import pro.anton.averin.networking.testrest.ui.views.AdaptableLinearLayout;
import pro.anton.averin.networking.testrest.ui.views.ProtocolSwitcher;
import pro.anton.averin.networking.testrest.ui.views.TokenizedEditText;

/**
 * Created by AAverin on 09.11.13.
 */
public class RequestFragment extends ViewPagerFragment implements TokenizedEditText.TokenListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener, MenuItem.OnMenuItemClickListener {

    public final static int PICK_FILE_INTENT_ID = 11;
    AddQueryDialog addQueryDialog = null;
    AddHeaderDialog addHeaderDialog = null;
    private Activity activity;
    private BaseContext baseContext;
    private ProtocolSwitcher protocolSwitcher;
    private TokenizedEditText methodUrlEditText;
    AddQueryDialog.QueryPopupListener queryPopupListener = new AddQueryDialog.QueryPopupListener() {
        @Override
        public void onOk(String key, String value) {
            StringBuffer newValue = new StringBuffer();
            String text = methodUrlEditText.getText().toString();
            newValue.append(methodUrlEditText.getText().toString());
            if (text.length() > 0) {
                if (!text.equals("?")) {
                    newValue.append("&");
                }
            } else {
                newValue.append("?");
            }
            newValue.append(key);
            newValue.append("=");
            newValue.append(value);
            methodUrlEditText.setText(newValue.toString());
        }
    };
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
    AddHeaderDialog.HeaderPopupListener headerPopupListener = new AddHeaderDialog.HeaderPopupListener() {
        @Override
        public void onOk(String key, String value) {
            headersList.add(new RequestHeader(key, value));
            addedHeadersAdapter.notifyDataSetChanged();
        }
    };
    private Request request = new Request();

    public RequestFragment() {
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

        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_request, container, false);
        return contentView;
    }

    private void init() {
        protocolSwitcher = (ProtocolSwitcher) contentView.findViewById(R.id.protocol_button);

        methodRadioGroup = (RadioGroup) contentView.findViewById(R.id.method_radiogroup);
        methodRadioGroup.setOnCheckedChangeListener(this);

        baseUrlEditText = (EditText) contentView.findViewById(R.id.baseurl);

        baseUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                result = result.replace("https://", "");
                result = result.replace("http://", "");
                if (!s.toString().equals(result)) {
                    baseUrlEditText.setText(result);
                }
            }
        });

        postLayout = (LinearLayout) contentView.findViewById(R.id.post_layout);
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

        addedHeadersList = (AdaptableLinearLayout) contentView.findViewById(R.id.addedheaders_list);
        addedHeadersAdapter = new AddedHeadersAdapter(activity.getApplicationContext(), headersList);
        addedHeadersList.setAdapter(addedHeadersAdapter);

        addQueryButton = (TextView) contentView.findViewById(R.id.add_query_button);
        addQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQueryDialog = AddQueryDialog.getInstance(queryPopupListener);
                addQueryDialog.displayDialog(getBaseActivity(), "ADD_HEADER");
            }
        });

        addHeadersButton = (TextView) contentView.findViewById(R.id.add_header_button);
        addHeadersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHeaderDialog = AddHeaderDialog.getInstance(headerPopupListener);
                addHeaderDialog.displayDialog(getBaseActivity(), "ADD_HEADER");
            }
        });

        methodUrlEditText = (TokenizedEditText) contentView.findViewById(R.id.method_url);
        methodUrlEditText.setTokenRegexp("[\\?&]([^&?]+=[^&?]+)");
        methodUrlEditText.setTokenListener(this);

        methodUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TokenizedEditText edit = ((TokenizedEditText) view);
                    if (edit.getText().length() == edit.getSelectionEnd()) {
                        highlightQueryButton();
                    }
                    String text = edit.getText().toString();
                    if (!text.startsWith("?") && !baseUrlEditText.getText().toString().contains("?")) {
                        edit.setText("?" + text);
                    }
                }
            }
        });
        sendButton = (Button) contentView.findViewById(R.id.btn_send);
        sendButton.setOnClickListener(this);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.request_screen_menu, menu);

        menu.findItem(R.id.action_save).setOnMenuItemClickListener(this);
        menu.findItem(R.id.action_manager).setOnMenuItemClickListener(this);
        menu.findItem(R.id.action_clear).setOnMenuItemClickListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        buildRequest();
        TestRestFragment p = (TestRestFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("MAIN");
        switch (menuItem.getItemId()) {
            case R.id.action_save:
                p.openManagerActivity(true);
                break;

            case R.id.action_manager:
                p.openManagerActivity(false);
                break;

            case R.id.action_clear:
                clearFields();
                break;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (addQueryDialog != null && addQueryDialog.getDialog() != null && addQueryDialog.getDialog().isShowing()) {
            addQueryDialog.dismiss();
        }
        addQueryDialog = null;
        if (addHeaderDialog != null && addHeaderDialog.getDialog() != null && addHeaderDialog.getDialog().isShowing()) {
            addHeaderDialog.dismiss();
        }
        addHeaderDialog = null;
    }

    public void init_withRequest(Request request) {
        methodRadioGroup.check(getMethodRadioButtonId(request.method));
        protocolSwitcher.set(request.getProtocolType());
        baseUrlEditText.setText(request.baseUrl);
        methodUrlEditText.setText(request.queryString);


        headersList.clear();
        if (request.headers != null && request.headers.size() > 0) {
            for (RequestHeader header : request.headers) {
                headersList.add(header);
            }
        }
        addedHeadersAdapter.notifyDataSetChanged();
    }

    public void clearFields() {
        methodRadioGroup.check(getMethodRadioButtonId("GET"));
        protocolSwitcher.set(ProtocolType.HTTP);
        baseUrlEditText.setText("");
        methodUrlEditText.setText("");
        headersList.clear();
        addedHeadersAdapter.notifyDataSetChanged();
    }

    private int getMethodRadioButtonId(String method) {
        if (method.toUpperCase().equals("GET")) {
            return R.id.checkbox_get;
        } else if (method.toUpperCase().equals("POST")) {
            return R.id.checkbox_post;
        } else if (method.toUpperCase().equals("PUT")) {
            return R.id.checkbox_put;
        } else if (method.toUpperCase().equals("DELETE")) {
            return R.id.checkbox_delete;
        }
        return -1;
    }

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
                if (prepareRequest()) {
                    TestRestFragment p = (TestRestFragment) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("MAIN");
                    p.showResponsePage();
                }
                break;
        }
    }

    public boolean prepareRequest() {
        if (!validate()) {
            Toast.makeText(activity, getString(R.string.error_emptyFields), Toast.LENGTH_LONG).show();
            baseUrlEditText.requestFocus();
            return false;
        }
        baseContext.currentResponse = null;
        baseContext.currentRequest = buildRequest();
        if (baseContext.currentRequest == null) {
            Toast.makeText(activity, getString(R.string.error_request_generic), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private boolean validate() {
        return baseUrlEditText.getText().length() > 0;
    }

    private Request buildRequest() {
        if (baseContext.currentRequest != null) {
            request.name = baseContext.currentRequest.name;
        }
        request.protocol = protocolSwitcher.getProtocolText();
        request.baseUrl = baseUrlEditText.getText().toString();
        RadioButton radioButton = (RadioButton) contentView.findViewById(methodRadioGroup.getCheckedRadioButtonId());
        request.method = radioButton.getText().toString();
        request.queryString = methodUrlEditText.getText().toString();
        request.headers = headersList;
        if (Config.isCrashlyticsEnabled) {
            Crashlytics.setString("request.protocol", request.protocol);
            Crashlytics.setString("request.baseUrl", request.baseUrl);
            Crashlytics.setString("request.method", request.method);
            Crashlytics.setString("request.queryString", request.queryString);
            StringBuilder headers = new StringBuilder();
            for (RequestHeader header : headersList) {
                headers.append(header.toString());
            }
            Crashlytics.setString("request.headers", headers.toString());
        }

        try {
            URI.create(request.asURI());
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

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
            if (requestCode == EntriesManagerActivity.ENTRIESMANAGER_REQUEST_CODE) {
                init_withRequest(baseContext.currentRequest);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

//            QueryMenuPopupWindow popup = new QueryMenuPopupWindow(activity, this);
//            popup.setChipListener(chipListener);
//            popup.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + dx, location[1]);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(true);
        }
    }
}
