package pro.anton.averin.networking.testrest.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 09.11.13.
 */
public class ProtocolToggleButton extends Button {

    private boolean isHttpsEnabled = false;

    public ProtocolToggleButton(Context context) {
        super(context);
    }

    public ProtocolToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProtocolToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void toggleProtocol() {
        isHttpsEnabled = !isHttpsEnabled;
        if (isHttpsEnabled) {
            setText(R.string.https);
        } else {
            setText(R.string.http);
        }
    }

}
