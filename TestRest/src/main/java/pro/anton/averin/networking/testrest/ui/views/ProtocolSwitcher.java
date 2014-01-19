package pro.anton.averin.networking.testrest.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 09.11.13.
 */
public class ProtocolSwitcher extends LinearLayout {

    private Context context;

    public class ProtocolToggleButton extends LinearLayout {

        CheckedTextView buttonTextView;
        public ProtocolToggleButton(Context context, String text) {
            super(context, null);
            buttonTextView = new CheckedTextView(context);

            XmlResourceParser colorXml = getResources().getXml(R.color.protocol_button_textcolor_selector);
            try {
                ColorStateList colorStateList = ColorStateList.createFromXml(getResources(), colorXml);
                buttonTextView.setTextColor(colorStateList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            buttonTextView.setTextSize(getResources().getDimension(R.dimen.protocol_button_textSize));
            buttonTextView.setText(text);
            addView(buttonTextView);

            setGravity(Gravity.CENTER);
            setLayoutParams(new LayoutParams(getResources().getDimensionPixelSize(R.dimen.protocol_button_width), getResources().getDimensionPixelSize(R.dimen.protocol_button_height)));
            setBackgroundDrawable(context.getResources().getDrawable(R.drawable.protocol_button_selector));
        }

        public void select() {
            setSelected(true);
            buttonTextView.setChecked(true);
        }

        public void deselect() {
            setSelected(false);
            buttonTextView.setChecked(false);
        }

        public String getText() {
            return buttonTextView.getText().toString();
        }
    }

    ProtocolToggleButton httpButton;
    ProtocolToggleButton httpsButton;

    private boolean isHttpsEnabled = false;

    public ProtocolSwitcher(Context context) {
        super(context);
        init(context);
    }

    public ProtocolSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        httpButton = new ProtocolToggleButton(context, "http://");
        httpsButton = new ProtocolToggleButton(context, "https://");

        httpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                set(ProtocolType.HTTP);
            }
        });
        httpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                set(ProtocolType.HTTPS);
            }
        });

        addView(httpButton);
        addView(httpsButton);

        set(ProtocolType.HTTP);
    }

    public void set(int type) {
        if (type == ProtocolType.HTTP) {
            isHttpsEnabled = false;
            httpButton.select();
            httpsButton.deselect();
        } else {
            isHttpsEnabled = true;
            httpButton.deselect();
            httpsButton.select();
        }
    }

    public void toggleProtocol() {
        isHttpsEnabled = !isHttpsEnabled;
        if (isHttpsEnabled) {
            set(ProtocolType.HTTP);
        } else {
            set(ProtocolType.HTTPS);
        }
    }

    public String getProtocolText() {
        if (isHttpsEnabled) {
            return httpButton.getText();
        } else {
            return httpsButton.getText();
        }
    }

}
