package pro.anton.averin.networking.testrest.ui.views.jsonviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 16.01.14.
 */
public class NodeView extends LinearLayout implements View.OnClickListener {

    private final static int LEFT_PADDING = 16;

    private String key;

    LinearLayout nodeKeyExpandedLayout;
    LinearLayout nodeKeyCollapsedLayout;
    LinearLayout nodeContent;
    TextView nodeKeyExpanded;
    TextView nodeKeyCollapsed;
    ImageView collapseButton;

    TextView ellipsesTextView = null;

    String openBracket = null;
    String closeBracket = null;
    boolean wasCollapsed = false;

    Context context;

    private boolean isExpanded = true;

    private boolean isBracketOpened = false;

    public NodeView(Context context) {
        super(context);
        init(context, true);
    }

    public NodeView(Context context, String key) {
        this(context, key, true);
    }

    public NodeView(Context context, String key, boolean canExpand) {
        super(context);
        this.key = key;
        init(context, canExpand);
    }

    private void init(Context context, boolean canExpand) {
        this.context = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.jsonviewer_node, this, true);
        setOrientation(VERTICAL);

        ellipsesTextView = new TextView(context);
        ellipsesTextView.setText("...");

        nodeContent = (LinearLayout) findViewById(R.id.jsonviewer_nodeContent);

        nodeKeyExpandedLayout = (LinearLayout) findViewById(R.id.jsonviewer_nodeKey_expandedLayout);


        nodeKeyCollapsedLayout = (LinearLayout) findViewById(R.id.jsonviewer_nodeKey_collapsedLayout);
        nodeKeyExpanded = (TextView) findViewById(R.id.jsonviewer_nodeKey_expanded);
        nodeKeyCollapsed = (TextView) findViewById(R.id.jsonviewer_nodeKey_collapsed);

        collapseButton = (ImageView) findViewById(R.id.jsonviewer_node_collapse);
        if (canExpand) {
            collapseButton.setOnClickListener(this);
        } else {
            collapseButton.setVisibility(GONE);
        }


        if (canExpand && key != null && key.length() > 0) {
            nodeKeyExpanded.setText(key + ": ");
            nodeKeyCollapsed.setText(key + ": ");
        } else {
            nodeKeyExpandedLayout.setVisibility(GONE);
        }
    }

    @Override
    public void addView(View child) {
        nodeContent.addView(child);
        child.setPadding(isBracketOpened ? LEFT_PADDING * 2 : LEFT_PADDING,0, 0, 0);
    }

    public void openBracket(String bracket) {
        openBracket = bracket;
        TextView openBracketView = new TextView(context);
        openBracketView.setText(bracket);
        nodeKeyExpandedLayout.setVisibility(VISIBLE);
        nodeKeyExpandedLayout.addView(openBracketView);
        isBracketOpened = true;
    }

    public void closeBracket(String bracket) {
        closeBracket = bracket;
        isBracketOpened = false;
        TextView closeBracketView = new TextView(context);
        closeBracketView.setText(bracket);
        addView(closeBracketView);
    }

    public void toggle() {
        if (isExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        isExpanded = true;
        collapseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_collapse));
        nodeKeyExpandedLayout.setVisibility(VISIBLE);
        nodeKeyCollapsedLayout.setVisibility(GONE);

        nodeContent.setVisibility(VISIBLE);
    }

    public void collapse() {
        isExpanded = false;

        collapseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_expand));

        nodeKeyExpandedLayout.setVisibility(GONE);
        nodeKeyCollapsedLayout.setVisibility(VISIBLE);

        if (closeBracket != null && !wasCollapsed) {
            TextView collapsedOpenBracketView = new TextView(context);
            collapsedOpenBracketView.setText(openBracket);
            nodeKeyCollapsedLayout.addView(collapsedOpenBracketView);

            nodeKeyCollapsedLayout.addView(ellipsesTextView);

            TextView collapsedCloseBracketView = new TextView(context);
            collapsedCloseBracketView.setText(closeBracket);
            nodeKeyCollapsedLayout.addView(collapsedCloseBracketView);
            wasCollapsed = true;
        }

        nodeContent.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        toggle();
    }
}
