package pro.anton.averin.networking.testrest.ui.views.jsonviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 16.01.14.
 */
public class NodeView extends LinearLayout {

    private String key;
    private String value;

    TextView nodeKey;
    TextView nodeValue;

    public NodeView(Context context, String key) {
        super(context);
        this.key = key;
        this.value = value;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.jsonviewer_node, this, true);

        nodeKey = (TextView) findViewById(R.id.jsonviewer_nodeKey);
        nodeValue = (TextView) findViewById(R.id.jsonviewer_nodeValue);

        nodeKey.setText(key);
        nodeValue.setText(value);
    }
}
