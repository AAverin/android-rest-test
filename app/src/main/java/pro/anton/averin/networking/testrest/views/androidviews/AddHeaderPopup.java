package pro.anton.averin.networking.testrest.views.androidviews;


import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.Headers;
import pro.anton.averin.networking.testrest.presenters.AddHeaderPopupPresenter;
import pro.anton.averin.networking.testrest.presenters.AddHeaderPopupView;
import pro.anton.averin.networking.testrest.views.adapters.HeadersListAdapter;
import pro.anton.averin.networking.testrest.views.base.BaseActivity;

public class AddHeaderPopup extends PopupWindow implements AddHeaderPopupView {

    @Bind(R.id.header_name)
    EditText keyEditText;
    @Bind(R.id.header_value)
    EditText valueEditText;
    @Bind(R.id.header_spinner)
    Spinner headersSpinner;

    @Inject
    AddHeaderPopupPresenter presenter;

    private boolean isCustomHeader = false;
    private HeaderPopupListener addPopupListener = null;

    public AddHeaderPopup(final BaseActivity activity) {
        super(activity);

        activity.getComponent().injectTo(this);
        presenter.onCreate();

        View popupView = LayoutInflater.from(activity).inflate(R.layout.addheader_popup, null);

        ButterKnife.bind(this, popupView);

        final HeadersListAdapter adapter = new HeadersListAdapter(activity,
                presenter.getSupportedHeaders()
        );

        headersSpinner.setAdapter(adapter);
        headersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i).name.equals("Custom")) {
                    keyEditText.setVisibility(View.VISIBLE);
                    isCustomHeader = true;
                } else {
                    keyEditText.setVisibility(View.GONE);
                    isCustomHeader = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageButton okButton = (ImageButton) popupView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCustomHeader) {
                    if ((keyEditText.getText().length() == 0) || (valueEditText.getText().length() == 0)) {
                        Toast.makeText(activity, activity.getString(R.string.error_keyValue), 3000).show();
                        return;
                    }

                    if (addPopupListener != null) {
                        addPopupListener.onOk(keyEditText.getText().toString(), valueEditText.getText().toString());
                    }


                } else {
                    if (valueEditText.getText().length() == 0) {
                        Toast.makeText(activity, activity.getString(R.string.error_keyValue), 3000).show();
                        return;
                    }

                    if (addPopupListener != null) {
                        Headers.Header selectedHeader = adapter.getItem(headersSpinner.getSelectedItemPosition());
                        selectedHeader.popularity++;
                        if (!presenter.updateHeader(selectedHeader)) {
                            selectedHeader.popularity--;
                        }
                        addPopupListener.onOk(selectedHeader.name, valueEditText.getText().toString());
                    }
                }




                addPopupListener = null;
                dismiss();
            }
        });

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        popupView.setMinimumWidth((int)(displayRectangle.width() * 0.7f));

        setContentView(popupView);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.QueryPopupAnimation);
    }

    public void setHeaderPopupListener(HeaderPopupListener listener) {
        this.addPopupListener = listener;
    }

    public interface HeaderPopupListener {
        void onOk(String key, String value);
    }
}

