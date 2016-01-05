package pro.anton.averin.networking.testrest.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.data.models.Headers;
import pro.anton.averin.networking.testrest.ui.adapters.HeadersListAdapter;
import pro.anton.averin.networking.testrest.ui.fragments.BaseDialogFragment;

/**
 * Created by AAverin on 9-3-2015.
 */
public class AddHeaderDialog extends BaseDialogFragment {
    private boolean isCustomHeader = false;
    private HeaderPopupListener addPopupListener = null;

    public static AddHeaderDialog getInstance(HeaderPopupListener listener) {
        AddHeaderDialog dialog = new AddHeaderDialog();
        dialog.setHeaderPopupListener(listener);
        return dialog;
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    public void setHeaderPopupListener(HeaderPopupListener listener) {
        this.addPopupListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.width = (int) getResources().getDimension(R.dimen.popup_dialog_max_width);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
//        lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
        window.setAttributes(lp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.addheader_popup, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.75f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final EditText keyEditText = (EditText) contentView.findViewById(R.id.header_name);
        final EditText valueEditText = (EditText) contentView.findViewById(R.id.header_value);
        final Spinner headersSpinner = (Spinner) contentView.findViewById(R.id.header_spinner);

        final HeadersListAdapter adapter = new HeadersListAdapter(getBaseActivity(), baseContext.testRestDb.getSupportedHeaders());

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

        ImageButton okButton = (ImageButton) contentView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCustomHeader) {
                    if ((keyEditText.getText().length() == 0) || (valueEditText.getText().length() == 0)) {
                        Toast.makeText(baseContext, baseContext.getString(R.string.error_keyValue), 3000).show();
                        return;
                    }

                    if (addPopupListener != null) {
                        addPopupListener.onOk(keyEditText.getText().toString(), valueEditText.getText().toString());
                    }


                } else {
                    if (valueEditText.getText().length() == 0) {
                        Toast.makeText(baseContext, baseContext.getString(R.string.error_keyValue), 3000).show();
                        return;
                    }

                    if (addPopupListener != null) {
                        Headers.Header selectedHeader = adapter.getItem(headersSpinner.getSelectedItemPosition());
                        selectedHeader.popularity++;
                        try {
                            baseContext.testRestDb.updateHeader(selectedHeader);
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
    }

    public interface HeaderPopupListener {
        public void onOk(String key, String value);
    }
}
