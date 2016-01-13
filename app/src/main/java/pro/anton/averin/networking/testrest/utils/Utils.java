package pro.anton.averin.networking.testrest.utils;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

public class Utils {
    public static void globalLayoutOnce(final View view, final OnGlobalLayoutCallback callback) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.safeRemoveLayoutListener(view.getViewTreeObserver(), this);
                if (callback != null) {
                    callback.onGlobalLayout(view);
                }
            }
        });
    }

    public static void safeRemoveLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeOnGlobalLayoutListener(listener);
        } else {
            observer.removeGlobalOnLayoutListener(listener);
        }
    }

    public interface OnGlobalLayoutCallback {
        void onGlobalLayout(View view);
    }
}
