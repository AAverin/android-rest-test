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


import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.ui.fragments.BaseDialogFragment;

/**
 * Created by AAverin on 9-3-2015.
 */
public class AddQueryDialog extends BaseDialogFragment {
    @Override
    public String getFragmentName() {
        return null;
    }

    public interface QueryPopupListener {
        public void onOk(String key, String value);
    }
    private QueryPopupListener queryPopupListener = null;
    public void setQueryPopupListener(QueryPopupListener listener) {
        this.queryPopupListener = listener;
    }

    public static AddQueryDialog getInstance(QueryPopupListener listener) {
        AddQueryDialog dialog = new AddQueryDialog();
        dialog.setQueryPopupListener(listener);
        return dialog;
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
        contentView = (ViewGroup) inflater.inflate(R.layout.addquery_popup, container, false);
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

        final EditText keyEditText  = (EditText) contentView.findViewById(R.id.key);
        final EditText valueEditText = (EditText) contentView.findViewById(R.id.value);

        ImageButton okButton = (ImageButton) contentView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((keyEditText.getText().length() == 0) || (valueEditText.getText().length() == 0)) {
                    Toast.makeText(baseContext, baseContext.getString(R.string.error_keyValue), 3000).show();
                    return;
                }

                if (queryPopupListener != null) {
                    queryPopupListener.onOk(keyEditText.getText().toString(), valueEditText.getText().toString());
                }
                queryPopupListener = null;
                dismiss();
            }
        });
    }
}
