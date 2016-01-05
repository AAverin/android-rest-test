package pro.anton.averin.networking.testrest.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.InterstitialAd;

import pro.anton.averin.networking.testrest.R;

/**
 * Created by AAverin on 02.02.14.
 */
public class AdsDialog extends DialogFragment {

    public final static String FRAGMENT_TAG = "AdsDialog";
    private Activity activity;

    private InterstitialAd interstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Panel);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup dialogView = (ViewGroup) inflater.inflate(R.layout.adsdialog, container, false);



        return dialogView;
    }



}
