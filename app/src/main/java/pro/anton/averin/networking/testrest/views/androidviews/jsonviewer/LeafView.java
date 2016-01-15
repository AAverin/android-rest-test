package pro.anton.averin.networking.testrest.views.androidviews.jsonviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.R;

public class LeafView extends LinearLayout {

    TextView leafKey;
    TextView leafValue;
    private String key;
    private Object value;

    public LeafView(Context context, String key, Object value) {
        super(context);
        this.key = key;
        this.value = value;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.jsonviewer_leaf, this, true);

        leafKey = (TextView) findViewById(R.id.jsonviewer_leafKey);
        leafValue = (TextView) findViewById(R.id.jsonviewer_leafValue);

        if (key != null) {
            leafKey.setText(key + ": ");
        }
        leafValue.setText(String.valueOf(value));
    }


}
