package pro.anton.averin.networking.testrest.legacy.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.anton.averin.networking.testrest.BaseContext;
import pro.anton.averin.networking.testrest.R;
import pro.anton.averin.networking.testrest.legacy.activities.BaseActivity;


/**
 * Created by AAverin on 27.01.14.
 */
public abstract class BasePopupDialog extends DialogFragment implements View.OnClickListener {

    public final static String FRAGMENT_TAG = "PopupDialog";
    public OnDialogDismissListener dialogDismissCallback = null;
    protected BaseContext baseContext;
    protected ViewGroup contentView;
    protected boolean isDirty = false;
    ViewGroup basePopupView;
    LinearLayout titleLayout;
    TextView titleView;
    ImageButton okButton;
    private String title;
    private boolean isOutsideDismissable = true;

    public BasePopupDialog() {

    }

    public BasePopupDialog(OnDialogDismissListener callback) {
        this.dialogDismissCallback = callback;
    }

    public BasePopupDialog(String title, Drawable titleDrawable) {
        init(title, titleDrawable);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Panel);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseContext = (BaseContext) activity.getApplicationContext();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.75f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        getDialog().setCanceledOnTouchOutside(isOutsideDismissable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (title != null) {
            titleView = (TextView) basePopupView.findViewById(R.id.dialog_title);
            titleView.setText(title);
        }

        titleLayout = (LinearLayout) basePopupView.findViewById(R.id.dialog_title_layout);

        okButton = (ImageButton) basePopupView.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okButtonVisible(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.width = (int) getResources().getDimension(R.dimen.popup_dialog_max_width);

//        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
//        lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
        window.setAttributes(lp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        basePopupView = (ViewGroup) inflater.inflate(R.layout.popup_base, container, false);
        basePopupView.addView(getPopupContentView());
        return basePopupView;
    }

    abstract protected ViewGroup getPopupContentView();

    protected void setTitle(String title) {
        this.title = title;
        if (title != null) {
            if (titleView == null) {
                titleView = (TextView) basePopupView.findViewById(R.id.dialog_title);
            }
            titleView.setText(title);
        }
    }

    public void setOnDialogDismissListener(OnDialogDismissListener callback) {
        this.dialogDismissCallback = callback;
    }

    protected void init(String title, Drawable titleDrawable) {
        this.title = title;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dialogDismissCallback != null) {
            dialogDismissCallback.onDismiss(dialog, isDirty);
        }
        super.onDismiss(dialog);
    }

    public void setOutsideDismissable(boolean dismissable) {
        isOutsideDismissable = dismissable;
        Dialog d = getDialog();
        if (d != null) {
            d.setCanceledOnTouchOutside(dismissable);
        }
    }

    public void okButtonVisible(boolean visible) {
        if (visible) {
            okButton.setVisibility(View.VISIBLE);
        } else {
            okButton.setVisibility(View.GONE);
        }
    }

    public void titleLayoutVisible(boolean visible) {
        if (visible) {
            titleLayout.setVisibility(View.VISIBLE);
        } else {
            titleLayout.setVisibility(View.GONE);
        }
    }

    public void displayDialog(BaseActivity activity) {
        if (activity == null || !activity.isActive()) {
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        if (fragmentManager == null || fragmentManager.isDestroyed()) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment oldDialog = fragmentManager.findFragmentByTag(BasePopupDialog.FRAGMENT_TAG);
        if (oldDialog != null) {
            transaction.remove(oldDialog);
        }
        transaction.addToBackStack(null);

        //fix for IllegalStateException: Can not perform this action after onSaveInstanceState
        //http://stackoverflow.com/questions/15729138/on-showing-dialog-i-get-can-not-perform-this-action-after-onsaveinstancestate
        if (!activity.isActive()) { //final safeguard in case activity was closed in the middle
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        this.show(ft, BasePopupDialog.FRAGMENT_TAG);
    }

    public interface OnDialogDismissListener {
        public void onDismiss(DialogInterface dialog, boolean isDirty);
    }
}

