package pro.anton.averin.networking.testrest.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.Gravity;
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
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.TestRestApp;
import pro.anton.averin.networking.testrest.models.Request;
import pro.anton.averin.networking.testrest.models.RequestHeader;
import pro.anton.averin.networking.testrest.ui.adapters.AddedHeadersAdapter;
import pro.anton.averin.networking.testrest.ui.dialogs.AddHeaderPopup;
import pro.anton.averin.networking.testrest.ui.dialogs.AddQueryPopup;
import pro.anton.averin.networking.testrest.ui.dialogs.QueryMenuPopupWindow;
import pro.anton.averin.networking.testrest.ui.phone.EntriesManagerActivity;
import pro.anton.averin.networking.testrest.ui.views.AdaptableLinearLayout;
import pro.anton.averin.networking.testrest.ui.views.ProtocolSwitcher;
import pro.anton.averin.networking.testrest.ui.views.ProtocolType;
import pro.anton.averin.networking.testrest.ui.views.TokenizedEditText;

/**
 * Created by AAverin on 09.11.13.
 */
public class RequestFragment extends ViewPagerFragment implements TokenizedEditText.TokenListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener, MenuItem.OnMenuItemClickListener {

    private Activity activity;
    private TestRestApp testRestApp;

    private View mGroupRoot;

    public final static int PICK_FILE_INTENT_ID = 11;

    private ProtocolSwitcher protocolSwitcher;
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

    QueryMenuPopupWindow.ChipListener chipListener = new QueryMenuPopupWindow.ChipListener() {

        @Override
        public void onChipDeleted(QuerySpan chip) {
            Toast.makeText(activity, "chip deleted " + chip.chip, 5).show();
        }
    };

    public RequestFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        this.testRestApp = (TestRestApp)activity.getApplicationContext();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_request, null);

        protocolSwitcher = (ProtocolSwitcher) mGroupRoot.findViewById(R.id.protocol_button);

        methodRadioGroup = (RadioGroup) mGroupRoot.findViewById(R.id.method_radiogroup);
        methodRadioGroup.setOnCheckedChangeListener(this);

        baseUrlEditText = (EditText) mGroupRoot.findViewById(R.id.baseurl);

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
        addedHeadersAdapter = new AddedHeadersAdapter(activity.getApplicationContext(), headersList);
        addedHeadersList.setAdapter(addedHeadersAdapter);

        addQueryButton = (TextView) mGroupRoot.findViewById(R.id.add_query_button);
        addQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQueryPopup popup = new AddQueryPopup(activity);
                popup.setQueryPopupListener(queryPopupListener);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        unDimBackground();
                    }
                });
                popup.showAtLocation(mGroupRoot, Gravity.TOP, 0, (int)(120 * getResources().getDisplayMetrics().density));
                dimBackground();
            }
        });

        addHeadersButton = (TextView) mGroupRoot.findViewById(R.id.add_header_button);
        addHeadersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHeaderPopup popup = new AddHeaderPopup(activity);
                popup.setHeaderPopupListener(headerPopupListener);
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        unDimBackground();
                    }
                });
                popup.showAtLocation(mGroupRoot, Gravity.TOP, 0, (int)(120 * getResources().getDisplayMetrics().density));
                dimBackground();
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
                    String text = edit.getText().toString();
                    if (!text.startsWith("?")) {
                        edit.setText("?" + text);
                    }
                }
            }
        });
        sendButton = (Button) mGroupRoot.findViewById(R.id.btn_send);
        sendButton.setOnClickListener(this);

        setHasOptionsMenu(true);

        return mGroupRoot;
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
        TestRestFragment p = (TestRestFragment) ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag("MAIN");
        switch(menuItem.getItemId()) {
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

    private void dimBackground() {
        TestRestFragment p = (TestRestFragment) ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag("MAIN");
        p.dim();
    }

    private void unDimBackground() {
        TestRestFragment p = (TestRestFragment) ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag("MAIN");
        p.unDim();
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

    AddQueryPopup.QueryPopupListener queryPopupListener = new AddQueryPopup.QueryPopupListener() {
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
            unDimBackground();
        }
    };

    AddHeaderPopup.HeaderPopupListener headerPopupListener = new AddHeaderPopup.HeaderPopupListener() {
        @Override
        public void onOk(String key, String value) {
            headersList.add(new RequestHeader(key, value));
            addedHeadersAdapter.notifyDataSetChanged();
            unDimBackground();
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
                if (prepareRequest()) {
                    TestRestFragment p = (TestRestFragment) ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag("MAIN");
                    p.showResponsePage();
                }
                break;
        }
    }

    public boolean prepareRequest() {
        if (!validate()) {
            Toast.makeText(activity, getString(R.string.error_emptyFields), 3000).show();
            baseUrlEditText.requestFocus();
            return false;
        }
        testRestApp.currentResponse = null;
        testRestApp.currentRequest = buildRequest();
        return true;
    }

    private boolean validate() {
        return baseUrlEditText.getText().length() > 0;
    }

    private Request buildRequest() {
        if (testRestApp.currentRequest != null) {
            request.name = testRestApp.currentRequest.name;
        }
        request.protocol = protocolSwitcher.getProtocolText();
        request.baseUrl = baseUrlEditText.getText().toString();
        RadioButton radioButton = (RadioButton) mGroupRoot.findViewById(methodRadioGroup.getCheckedRadioButtonId());
        request.method = radioButton.getText().toString();
        request.queryString = methodUrlEditText.getText().toString();
        request.headers = headersList;
        BugSenseHandler.addCrashExtraData("request.protocol", request.protocol);
        BugSenseHandler.addCrashExtraData("request.baseUrl", request.baseUrl);
        BugSenseHandler.addCrashExtraData("request.method", request.method);
        BugSenseHandler.addCrashExtraData("request.queryString", request.queryString);
        StringBuilder headers = new StringBuilder();
        for (RequestHeader header : headersList) {
            headers.append(header.toString());
        }
        BugSenseHandler.addCrashExtraData("request.headers", headers.toString());
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
                init_withRequest(testRestApp.currentRequest);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
