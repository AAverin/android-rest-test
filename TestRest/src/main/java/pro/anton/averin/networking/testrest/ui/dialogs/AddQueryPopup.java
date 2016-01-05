package pro.anton.averin.networking.testrest.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 10.11.13.
 */
public class AddQueryPopup extends PopupWindow {

    public interface QueryPopupListener {
        public void onOk(String key, String value);
    }

    private QueryPopupListener queryPopupListener = null;

    public AddQueryPopup(final Context context) {
        super(context);
        View popupView = LayoutInflater.from(context).inflate(R.layout.addquery_popup, null);

        final EditText keyEditText  = (EditText) popupView.findViewById(R.id.key);
        final EditText valueEditText = (EditText) popupView.findViewById(R.id.value);

        ImageButton okButton = (ImageButton) popupView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((keyEditText.getText().length() == 0) || (valueEditText.getText().length() == 0)) {
                    Toast.makeText(context, context.getString(R.string.error_keyValue), 3000).show();
                    return;
                }

                if (queryPopupListener != null) {
                    queryPopupListener.onOk(keyEditText.getText().toString(), valueEditText.getText().toString());
                }
                queryPopupListener = null;
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

    public void setQueryPopupListener(QueryPopupListener listener) {
        this.queryPopupListener = listener;
    }
}
