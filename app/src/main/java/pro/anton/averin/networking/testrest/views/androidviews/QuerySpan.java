package pro.anton.averin.networking.testrest.views.androidviews;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class QuerySpan extends ClickableSpan {

    public String chip;

    public QuerySpan(String chip) {
        this.chip = chip;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(true);
    }
}
