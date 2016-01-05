package pro.anton.averin.networking.testrest.views.fragments;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
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
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.ProtocolType;
import pro.anton.averin.networking.testrest.data.models.RequestHeader;
import pro.anton.averin.networking.testrest.presenters.RequestPresenter;
import pro.anton.averin.networking.testrest.presenters.RequestView;
import pro.anton.averin.networking.testrest.views.adapters.AddedHeadersAdapter;
import pro.anton.averin.networking.testrest.views.androidviews.AdaptableLinearLayout;
import pro.anton.averin.networking.testrest.views.androidviews.AddHeaderPopup;
import pro.anton.averin.networking.testrest.views.androidviews.AddQueryPopup;
import pro.anton.averin.networking.testrest.views.androidviews.ProtocolSwitcher;
import pro.anton.averin.networking.testrest.views.androidviews.QuerySpan;
import pro.anton.averin.networking.testrest.views.androidviews.TokenizedEditText;
import pro.anton.averin.networking.testrest.views.base.BaseViewPresenterViewpagerFragment;


public class RequestFragment extends BaseViewPresenterViewpagerFragment<RequestPresenter> implements RequestView {

    public final static int PICK_FILE_INTENT_ID = 11;

    @Bind(R.id.protocol_button)
    ProtocolSwitcher protocolSwitcher;
    @Bind(R.id.method_radiogroup)
    RadioGroup methodRadioGroup;
    @Bind(R.id.baseurl)
    EditText baseUrlEditText;
    @Bind(R.id.post_layout)
    LinearLayout postLayout;
    @Bind(R.id.use_file_checkbox)
    CheckBox useFileCheckbox;
    @Bind(R.id.pick_file_button)
    TextView pickFileButton;
    @Bind(R.id.post_body)
    EditText postBody;
    @Bind(R.id.addedheaders_list)
    AdaptableLinearLayout addedHeadersList;
    @Bind(R.id.add_query_button)
    TextView addQueryButton;
    @Bind(R.id.add_header_button)
    TextView addHeadersButton;
    @Bind(R.id.method_url)
    TokenizedEditText methodUrlEditText;
    @Bind(R.id.btn_send)
    Button sendButton;

    @Inject
    RequestPresenter presenter;

    AddedHeadersAdapter addedHeadersAdapter;

    private ArrayList<RequestHeader> headersList = new ArrayList<RequestHeader>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBaseActivity().getComponent().injectTo(this);

        initializePresenter(presenter, this);

        setHasOptionsMenu(true);

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

        methodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.checkbox_post) {
                    presenter.onPostMethodSelected();
                } else {
                    presenter.onPostMethodUnselected();
                }
            }
        });

        useFileCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.onUseFileCheckboxChecked(isChecked);
            }
        });

        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPickFileButtonClicked();
            }
        });

        addQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddQueryButtonClicked();
            }
        });

        addHeadersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddHeadersButtonClicked();
            }
        });

        methodUrlEditText.setTokenRegexp("[\\?&]([^&?]+=[^&?]+)");
        methodUrlEditText.setTokenListener(new TokenizedEditText.TokenListener() {
            @Override
            public ClickableSpan onCreateTokenSpan(String chip) {
                return new QuerySpan(chip);
            }
        });
        methodUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //TODO: refactor to presenter logic
                if (hasFocus) {
                    TokenizedEditText edit = ((TokenizedEditText) view);
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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendClicked();
            }
        });

        addedHeadersAdapter = new AddedHeadersAdapter(baseContext, headersList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_request, container, false);
        return contentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.request_screen_menu, menu);

        menu.findItem(R.id.action_save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                presenter.onSaveItemClicked();
                return true;
            }
        });
        menu.findItem(R.id.action_manager).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                presenter.onManagerItemClicked();
                return true;
            }
        });
        menu.findItem(R.id.action_clear).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                presenter.onClearItemClicked();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
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
    public void cleanPostBody() {
        postBody.setText("");
    }

    @Override
    public void showPickFileButton() {
        pickFileButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPostBodyFileHint() {
        postBody.setHint(R.string.post_body_file_hint);
    }

    @Override
    public void hidePickFileButton() {
        pickFileButton.setVisibility(View.GONE);
    }

    @Override
    public void setPostBodyDefaultHint() {
        postBody.setHint(R.string.post_body_hint);
    }

    @Override
    public void showFileChooser() {
        Intent fileChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooserIntent.setType("*/*");
        startActivityForResult(Intent.createChooser(fileChooserIntent, "Pick a file"), PICK_FILE_INTENT_ID);
    }

    @Override
    public void showAddQueryPopup() {
        AddQueryPopup popup = new AddQueryPopup(getBaseActivity());
        popup.setQueryPopupListener(new AddQueryPopup.QueryPopupListener() {
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
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                presenter.addQueryPopupDismissed();
            }
        });
        popup.showAtLocation(getView(), Gravity.TOP, 0, (int) (120 * getResources().getDisplayMetrics().density));
        dimBackground();
    }

    @Override
    public void showAddHeaderPopup() {
        AddHeaderPopup popup = new AddHeaderPopup(getBaseActivity());
        popup.setHeaderPopupListener(new AddHeaderPopup.HeaderPopupListener() {
            @Override
            public void onOk(String key, String value) {
                headersList.add(new RequestHeader(key, value));
                addedHeadersAdapter.notifyDataSetChanged();
                unDimBackground();
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                unDimBackground();
            }
        });
        popup.showAtLocation(getView(), Gravity.TOP, 0, (int) (120 * getResources().getDisplayMetrics().density));
        dimBackground();
    }

    @Override
    public void showPostLayout() {
        postLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePostLayout() {
        postLayout.setVisibility(View.GONE);
    }

    @Override
    public void showResponseScreen() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void unDimBackground() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void dimBackground() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void clearFields() {
        methodRadioGroup.check(getMethodRadioButtonId("GET"));
        protocolSwitcher.set(ProtocolType.HTTP);
        baseUrlEditText.setText("");
        methodUrlEditText.setText("");
        headersList.clear();
        addedHeadersAdapter.notifyDataSetChanged();
    }

    @Override
    public void showManagerScreen() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void showManagerScreenForSave() {
        throw new RuntimeException("not implemented");
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
}
