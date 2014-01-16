package pro.anton.averin.networking.testrest.ui.views.jsonviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 16.01.14.
 */
public class NodeView extends LinearLayout {

    private final static int LEFT_PADDING = 16;

    private String key;

    LinearLayout nodeKeyLayout;
    TextView nodeKey;

    Context context;

    private boolean isBracketOpened = false;

    public NodeView(Context context) {
        super(context);
        init(context);
    }

    public NodeView(Context context, String key) {
        super(context);
        this.key = key;
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.jsonviewer_node, this, true);
        setOrientation(VERTICAL);

        nodeKeyLayout = (LinearLayout) findViewById(R.id.jsonviewer_nodeKey_layout);
        nodeKey = (TextView) findViewById(R.id.jsonviewer_nodeKey);

        if (key != null && key.length() > 0) {
            nodeKey.setText(key + ": ");
        } else {
            nodeKeyLayout.setVisibility(GONE);
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        child.setPadding(isBracketOpened ? LEFT_PADDING * 2 : LEFT_PADDING,0, 0, 0);
    }

    public void openBracket(String bracket) {
        TextView openBracketView = new TextView(context);
        openBracketView.setText(bracket);
        nodeKeyLayout.setVisibility(VISIBLE);
        nodeKeyLayout.addView(openBracketView);
        isBracketOpened = true;
    }

    public void closeBracket(String bracket) {
        isBracketOpened = false;
        TextView openBracketView = new TextView(context);
        openBracketView.setText(bracket);
        addView(openBracketView);
    }
}
