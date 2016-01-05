package pro.anton.averin.networking.testrest.views.androidviews;

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

import javax.inject.Inject;

import butterknife.ButterKnife;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.presenters.AddQueryPopupPresenter;
import pro.anton.averin.networking.testrest.presenters.AddQueryPopupView;
import pro.anton.averin.networking.testrest.views.base.BaseActivity;

/**
 * Created by AAverin on 10.11.13.
 */
public class AddQueryPopup extends PopupWindow implements AddQueryPopupView {

    @Inject
    AddQueryPopupPresenter presenter;

    private QueryPopupListener queryPopupListener = null;

    public AddQueryPopup(final BaseActivity activity) {
        super(activity);

        activity.getComponent().injectTo(this);
        presenter.onCreate();

        View popupView = LayoutInflater.from(activity).inflate(R.layout.addquery_popup, null);

        ButterKnife.bind(this, popupView);


        final EditText keyEditText  = (EditText) popupView.findViewById(R.id.key);
        final EditText valueEditText = (EditText) popupView.findViewById(R.id.value);

        ImageButton okButton = (ImageButton) popupView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((keyEditText.getText().length() == 0) || (valueEditText.getText().length() == 0)) {
                    Toast.makeText(activity, activity.getString(R.string.error_keyValue), 3000).show();
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

    public void setQueryPopupListener(QueryPopupListener listener) {
        this.queryPopupListener = listener;
    }

    public interface QueryPopupListener {
        public void onOk(String key, String value);
    }
}
