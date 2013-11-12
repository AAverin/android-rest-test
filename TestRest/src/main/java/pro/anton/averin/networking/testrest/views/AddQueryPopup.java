package pro.anton.averin.networking.testrest.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 10.11.13.
 */
public class AddQueryPopup extends PopupWindow {

    public AddQueryPopup(Context context) {
        super(context);
        View popupView = LayoutInflater.from(context).inflate(R.layout.addquery_popup, null);

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
}
