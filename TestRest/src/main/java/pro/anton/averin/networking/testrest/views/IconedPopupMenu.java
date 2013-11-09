package pro.anton.averin.networking.testrest.views;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by AAverin on 09.11.13.
 */
public class IconedPopupMenu extends PopupMenu {
    public IconedPopupMenu(Context context, View anchor) {
        super(context, anchor);
    }

    public void forceShowIcons() {
        try {
            Field[] fields = Class.forName("android.support.v7.widget.PopupMenu").getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(this);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
