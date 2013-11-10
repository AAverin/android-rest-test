package pro.anton.averin.networking.testrest;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

import pro.anton.averin.networking.testrest.views.ProtocolToggleButton;
import pro.anton.averin.networking.testrest.views.QueryMenuPopupWindow;
import pro.anton.averin.networking.testrest.views.TokenizedEditText;

/**
 * Created by AAverin on 09.11.13.
 */
public class RequestFragment extends ViewPagerFragment implements TokenizedEditText.TokenListener {

    private View mGroupRoot;

    private ProtocolToggleButton protocolToggleButton;
    private TokenizedEditText methodUrlEditText;
    private TextView addQueryButton;

    View.OnClickListener protocolToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            protocolToggleButton.toggleProtocol();
        }
    };

    QueryMenuPopupWindow.ChipListener chipListener = new QueryMenuPopupWindow.ChipListener() {
        @Override
        public void onChipEdited(QuerySpan chip) {
            Toast.makeText(getActivity(), "chip  edited " + chip.chip, 5).show();
        }

        @Override
        public void onChipDeleted(QuerySpan chip) {
            Toast.makeText(getActivity(), "chip deleted " + chip.chip, 5).show();
        }
    };

    public class QuerySpan extends ClickableSpan {

        public String chip;
        public QuerySpan(String chip) {
            this.chip = chip;
        }

        @Override
        public void onClick(View view) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            QueryMenuPopupWindow popup = new QueryMenuPopupWindow(getActivity(), this);
            popup.setChipListener(chipListener);
            popup.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getMeasuredHeight());
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroupRoot = inflater.inflate(R.layout.fragment_request, null);

        protocolToggleButton = (ProtocolToggleButton) mGroupRoot.findViewById(R.id.protocol_button);
        protocolToggleButton.setOnClickListener(protocolToggleListener);

        addQueryButton = (TextView) mGroupRoot.findViewById(R.id.add_query_button);
        addQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add query dialog
            }
        });

        methodUrlEditText = (TokenizedEditText) mGroupRoot.findViewById(R.id.method_url);
        methodUrlEditText.setTokenRegexp("[\\?&]([^&?]+=[^&?]+)");
        methodUrlEditText.setTokenListener(this);

        methodUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TokenizedEditText edit = ((TokenizedEditText)view);
                    if (edit.getText().length() == edit.getSelectionEnd()) {
                        highlightQueryButton();
                    }
                }
            }
        });

        return mGroupRoot;
    }

    private void highlightQueryButton() {
        ValueAnimator animation = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(android.R.color.black),
                getResources().getColor(R.color.valid_green));
        animation.setDuration(1000);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                addQueryButton.setTextColor((Integer) valueAnimator.getAnimatedValue());
            }
        });

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                addQueryButton.setTextColor(getResources().getColor(android.R.color.black));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animation.start();
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.requestViewTitle);
    }

    @Override
    public ClickableSpan onCreateTokenSpan(String chip) {
        return new QuerySpan(chip);
    }
}
