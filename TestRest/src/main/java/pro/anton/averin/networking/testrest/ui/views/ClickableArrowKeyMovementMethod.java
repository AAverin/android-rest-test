package pro.anton.averin.networking.testrest.ui.views;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by AAverin on 10.11.13.
 */
public class ClickableArrowKeyMovementMethod extends ArrowKeyMovementMethod {

    private static ClickableArrowKeyMovementMethod instance = null;

    public static ClickableArrowKeyMovementMethod getInstance() {
        if (instance == null) {
            instance = new ClickableArrowKeyMovementMethod();
        }
        return instance;
    }

    @Override
    public boolean onKeyDown(TextView widget, Spannable text, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (event.getRepeatCount() == 0) {
                    ClickableSpan[] spans = text.getSpans(Selection.getSelectionStart(text), Selection.getSelectionEnd(text), ClickableSpan.class);
                    if (spans.length != 1) {
                        return false;
                    }
                    spans[0].onClick(widget);
                    return false;
                }
        }
        return super.onKeyDown(widget, text, keyCode, event);
    }

    @Override
    public boolean onKeyOther(TextView widget, Spannable text, KeyEvent event) {
        return super.onKeyOther(widget, text, event);
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] spans = buffer.getSpans(off, off, ClickableSpan.class);
            if (spans.length != 0) {
                spans[0].onClick(widget);
                return super.onTouchEvent(widget, buffer, event);
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}
