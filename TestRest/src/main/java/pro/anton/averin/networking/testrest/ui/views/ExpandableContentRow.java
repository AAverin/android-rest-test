package pro.anton.averin.networking.testrest.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 07.12.13.
 */
public class ExpandableContentRow extends LinearLayout implements View.OnClickListener {

    private TextView mTitleView;
    private LinearLayout content;
    private ImageButton expandButton;
    private boolean expandButtonState = true;

    public ExpandableContentRow(Context context) {
        super(context);
        initView(context, null);
    }

    public ExpandableContentRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ExpandableContentRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.expandable_content_row, this, true);
        content = (LinearLayout) findViewById(R.id.content);
        expandButton = (ImageButton)findViewById(R.id.expand_button);
        expandButton.setOnClickListener(this);
        mTitleView = (TextView) findViewById(R.id.title);

        //apply attributes
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ExpandableContentRow,
                    0, 0);

            try {
                setTitle(a.getString(R.styleable.ExpandableContentRow_expandable_row_title));
                int contentId = a.getResourceId(R.styleable.ExpandableContentRow_expandable_row_content, 0);
                setContent(LayoutInflater.from(context).inflate(contentId, null));
            } finally {
                a.recycle();
            }


        }
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    public void setContent(View view) {
        content.removeAllViews();
        content.addView(view);
    }

    public void setContent(ViewGroup view) {
        content.removeAllViews();
        content.addView(view);
    }

    public void expand() {
        expandButton.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float));
        content.setVisibility(View.VISIBLE);
    }

    public void shrink() {
        expandButton.setImageDrawable(getResources().getDrawable(android.R.drawable.arrow_up_float));
        content.setVisibility(GONE);
    }

    @Override
    public void onClick(View view) {
        expandButtonState = !expandButtonState;
        if (expandButtonState) {
            shrink();
        } else {
            expand();
        }
    }
}
