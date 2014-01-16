package pro.anton.averin.networking.testrest.ui.views.jsonviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 16.01.14.
 */
public class LeafView extends LinearLayout {

    private String key;
    private Object value;

    TextView leafKey;
    TextView leafValue;

    public LeafView(Context context, String key, Object value) {
        super(context);
        this.key = key;
        this.value = value;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.jsonviewer_leaf, this, true);

        leafKey = (TextView) findViewById(R.id.jsonviewer_leafKey);
        leafValue = (TextView) findViewById(R.id.jsonviewer_leafValue);

        leafKey.setText(key);
        leafValue.setText(String.valueOf(value));
    }


}
