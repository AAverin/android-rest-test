package pro.anton.averin.networking.testrest.views.androidviews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.utils.Utils;

public class ExpandableRow extends LinearLayout {

    public final static int ANIMATION_DURATION = 300;
    int contentHeight = 0, contentWidth = 0;
    int titleId = -1;
    int contentId = -1;
    boolean isExpanded = true, collapsible = true;
    TextView titleView = null;
    Drawable upArrow, downArrow = null;
    boolean shouldRefreshOnFirstExpand = false, contentWidthObtained, measureNotExpandedDeferred = false;
    //    ViewGroup contentView = null;
    private ImageView arrow;
    private LinearLayout content;

    public ExpandableRow(Context context) {
        super(context);
        init();
    }

    public ExpandableRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public ExpandableRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandableRow, 0, 0);
        isExpanded = a.getBoolean(R.styleable.ExpandableRow_expandablerow_isOpened, false);
        titleId = a.getResourceId(R.styleable.ExpandableRow_expandablerow_title, -1);
        contentId = a.getResourceId(R.styleable.ExpandableRow_expandablerow_content, -1);
        collapsible = a.getBoolean(R.styleable.ExpandableRow_expandablerow_collapsible, true);
        a.recycle();

        init();
    }

    void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.i_exapandable_band, this, true);
        arrow = (ImageView) findViewById(R.id.i_exp_row_arrow);
        content = (LinearLayout) findViewById(R.id.i_exp_row_content);
        Utils.globalLayoutOnce(content, new Utils.OnGlobalLayoutCallback() {
            @Override
            public void onGlobalLayout(View viewGroup) {
                contentWidth = content.getMeasuredWidth();
                contentWidthObtained = true;
                if (measureNotExpandedDeferred) {
                    measureNotExpandedDeferred = false;
                    measureNotExpanded();
                }
            }
        });

        titleView = (TextView) findViewById(R.id.i_exp_row_title);
        if (titleId != -1) {
            titleView.setText(titleId);
        }
        if (contentId != -1) {
            setContent(contentId);
        }

        upArrow = getResources().getDrawable(R.drawable.ic_arrow_up);
        downArrow = getResources().getDrawable(R.drawable.ic_arrow_down);

        LinearLayout row = (LinearLayout) findViewById(R.id.i_exp_row);

        if (isExpanded) {
            arrow.setImageDrawable(upArrow);
        } else {
            shouldRefreshOnFirstExpand = true;
            arrow.setImageDrawable(downArrow);
        }


        row.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    public void setContent(View view) {
        if (isExpanded) {
            refreshContentHeight(false);
        }
        content.removeAllViews();
        content.addView(view);
        if (!isExpanded) {
            if (contentWidthObtained) {
                measureNotExpanded();
            } else {
                measureNotExpandedDeferred = true;
            }
        }
    }

    public void refreshContentHeight(boolean force) {
        Utils.globalLayoutOnce(content, new Utils.OnGlobalLayoutCallback() {
            @Override
            public void onGlobalLayout(View viewGroup) {
                contentHeight = content.getMeasuredHeight();
            }
        });
        if (force) {
            content.requestLayout();
        }
    }

    public void setContent(int viewId) {
        try {
            ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(viewId, content, false);
            setContent(view);
        } catch (ClassCastException c) {
            //in case this is not a view group
            View view = LayoutInflater.from(getContext()).inflate(viewId, content, false);
            setContent(view);
        }

    }

    private void measureNotExpanded() {
        int withMeasureSpec = View.MeasureSpec.makeMeasureSpec(contentWidth, View.MeasureSpec.AT_MOST);
        content.measure(withMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        contentHeight = content.getMeasuredHeight();
        LayoutParams params = (LayoutParams) content.getLayoutParams();
        params.height = 0;
        content.setLayoutParams(params);
    }

    public void toggle() {
        if (isExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (isExpanded) {
            return;
        }
        isExpanded = true;
        arrow.setImageDrawable(upArrow);

        ValueAnimator anim = ValueAnimator.ofInt(0, contentHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = content.getLayoutParams();
                layoutParams.height = val;
                content.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                content.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (shouldRefreshOnFirstExpand) {
                    refreshContentHeight(true);
                    shouldRefreshOnFirstExpand = false;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    public void collapse() {
        if (!isExpanded) {
            return;
        }
        isExpanded = false;
        if (!collapsible) {
            return;
        }

        arrow.setImageDrawable(downArrow);

        ValueAnimator anim = ValueAnimator.ofInt(contentHeight, 0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = content.getLayoutParams();
                layoutParams.height = val;
                content.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }
}
