package pro.anton.averin.networking.testrest.ui.dialogs;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.ui.RequestFragment;

/**
 * Created by AAverin on 10.11.13.
 */
public class QueryMenuPopupWindow extends PopupWindow {

    private ChipListener chipListener;
    private RequestFragment.QuerySpan chip;

    public interface ChipListener {
        void onChipDeleted(RequestFragment.QuerySpan chip);
    }

    View.OnClickListener queryMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.query_delete_button:
                    chipListener.onChipDeleted(chip);
                    break;
            }
        }
    };

    public void setChipListener(ChipListener chipListener) {
        this.chipListener = chipListener;
    }

    public QueryMenuPopupWindow(Context context, RequestFragment.QuerySpan chip) {
        super(context);

        this.chip = chip;

        View popupView = LayoutInflater.from(context).inflate(R.layout.querymenu_popup, null);
        popupView.findViewById(R.id.query_delete_button).setOnClickListener(queryMenuClickListener);

        setContentView(popupView);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }
}
