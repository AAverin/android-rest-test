package pro.anton.averin.networking.testrest.views.androidviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 09.11.13.
 */
public class TokenizedEditText extends EditText {

    private Pattern tokenRegexp;
    private Pattern tokenStringContainsRegexp;
    private Context context;
    private TokenListener tokenListener;

    private SpannableStringBuilder spannableStringBuilder = null;
    OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                applyTokens();
            }
        }
    };
    private boolean inTokenizer = false;
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

    public TokenizedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        initAttrs(attrs);
    }

    public TokenizedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttrs(attrs);
    }

    public TokenizedEditText(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        addTextChangedListener(tokenizer);
        this.context = context;
        setMovementMethod(ClickableArrowKeyMovementMethod.getInstance());
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TokenizedEditText, 0, 0);
        String regexp = a.getString(R.styleable.TokenizedEditText_regexp);
        if (regexp != null && regexp.length() > 0) {
            setTokenRegexp(regexp);
        }
        a.recycle();
    }

    public void setTokenRegexp(String regexp) {
        tokenRegexp = Pattern.compile(regexp);
        tokenStringContainsRegexp = Pattern.compile(".*" + regexp + ".*");
    }

    public void setTokenListener(TokenListener listener) {
        this.tokenListener = listener;
    }

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

    public interface TokenListener {
        ClickableSpan onCreateTokenSpan(String chip);
    }
}
