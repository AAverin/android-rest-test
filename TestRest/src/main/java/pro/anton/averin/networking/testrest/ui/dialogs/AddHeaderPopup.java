package pro.anton.averin.networking.testrest.ui.dialogs;

/**
 * Created by AAverin on 13.11.13.
 */

import android.app.Activity;
import android.content.Context;
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

import java.sql.SQLException;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.models.Headers;
import pro.anton.averin.networking.testrest.ui.adapters.HeadersListAdapter;

/**
 * Created by AAverin on 10.11.13.
 */
public class AddHeaderPopup extends PopupWindow {

    private boolean isCustomHeader = false;

    public interface HeaderPopupListener {
        public void onOk(String key, String value);
    }

    private HeaderPopupListener addPopupListener = null;

    public AddHeaderPopup(final Context context) {
        super(context);
        View popupView = LayoutInflater.from(context).inflate(R.layout.addheader_popup, null);

        final EditText keyEditText  = (EditText) popupView.findViewById(R.id.header_name);
        final EditText valueEditText = (EditText) popupView.findViewById(R.id.header_value);
        final Spinner headersSpinner = (Spinner) popupView.findViewById(R.id.header_spinner);

        final HeadersListAdapter adapter = new HeadersListAdapter(context, ((BaseContext)context.getApplicationContext()).testRestDb
                .getSupportedHeaders());

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
                        Toast.makeText(context, context.getString(R.string.error_keyValue), 3000).show();
                        return;
                    }

                    if (addPopupListener != null) {
                        addPopupListener.onOk(keyEditText.getText().toString(), valueEditText.getText().toString());
                    }


                } else {
                    if (valueEditText.getText().length() == 0) {
                        Toast.makeText(context, context.getString(R.string.error_keyValue), 3000).show();
                        return;
                    }

                    if (addPopupListener != null) {
                        Headers.Header selectedHeader = adapter.getItem(headersSpinner.getSelectedItemPosition());
                        selectedHeader.popularity++;
                        try {
                            ((BaseContext)context.getApplicationContext()).testRestDb.updateHeader(selectedHeader);
                        } catch (SQLException e) {
                            selectedHeader.popularity--;
                            e.printStackTrace();
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
        Window window = ((Activity)context).getWindow();
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
}

