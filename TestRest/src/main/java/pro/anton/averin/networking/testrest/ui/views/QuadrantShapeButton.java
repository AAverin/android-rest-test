package pro.anton.averin.networking.testrest.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 28.11.13.
 */
public class QuadrantShapeButton extends Button {

    private int mWidth;
    private int mHeight;

    public QuadrantShapeButton(Context context) {
        super(context);
    }

    public QuadrantShapeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuadrantShapeButton(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

}
