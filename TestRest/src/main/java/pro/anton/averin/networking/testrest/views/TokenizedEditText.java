package pro.anton.averin.networking.testrest.views;

import android.content.Context;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.BaseMovementMethod;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AAverin on 09.11.13.
 */
public class TokenizedEditText extends EditText {

    private Pattern tokenRegexp;
    private Pattern tokenStringContainsRegexp;
    private Context context;
    private TokenListener tokenListener;

    private SpannableStringBuilder spannableStringBuilder = null;
    private boolean inTokenizer = false;

    public interface TokenListener {
        public ClickableSpan onCreateTokenSpan(String chip);
    }

    public TokenizedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TokenizedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TokenizedEditText(Context context) {
        super(context);
        init(context);
    }

    OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                applyTokens();
            }
        }
    };

    private void init(Context context) {
        addTextChangedListener(tokenizer);
        this.context = context;
        setMovementMethod(ClickableArrowKeyMovementMethod.getInstance());
    }

    public void setTokenRegexp(String regexp) {
        tokenRegexp = Pattern.compile(regexp);
        tokenStringContainsRegexp = Pattern.compile(".*" + regexp + ".*");
    }

    public void setTokenListener(TokenListener listener) {
        this.tokenListener = listener;
    }

    TextWatcher tokenizer = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!inTokenizer && charSequence.toString().matches(tokenStringContainsRegexp.pattern())) {
                tokenize(charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void applyTokens() {
        if (spannableStringBuilder != null) {
            int oldSelection = getSelectionEnd();
            setText(spannableStringBuilder);
            setSelection(oldSelection);
            spannableStringBuilder = null;
        }
    }

    private void tokenize(String sequence) {
        inTokenizer = true;
        spannableStringBuilder = new SpannableStringBuilder(sequence);
        ArrayList<String> chips = new ArrayList<String>();
        Matcher matcher = tokenRegexp.matcher(sequence);
        while (matcher.find()) {
            chips.add(matcher.group(1));
        }
        for (String chip : chips) {
            ClickableSpan span = tokenListener.onCreateTokenSpan(chip);
            int chipStart = sequence.indexOf(chip);
            spannableStringBuilder.setSpan(span, chipStart, chipStart + chip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        applyTokens();
        inTokenizer = false;
    }
}
