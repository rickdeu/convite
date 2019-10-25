package com.fenix.wakonga.scannerFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.fenix.wakonga.R;

public class ScannerFragment extends Fragment {
   View v;
    ImageView qrcode2;
   Button btn_scan;
    public ScannerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.scanner_fragment, container,false);
      //  btn_scan=v.findViewById(R.id.btn_scan);
        qrcode2 = (ImageView) v.findViewById(R.id.qrcode2);

        qrcode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ScanActivity.class);
                startActivity(intent);
            }
        });


       /* VCard lVCard=new VCard("Fenix")
                .setEmail("fenixinnovation@gmail.com")
                .setAddress("Lubango")
                .setTitle("Fenix")
                .setCompany("Fenix Technology")
                .setPhoneNumber("940171369")
                .setWebsite("www.fenixinnovation.info");

        Bitmap lBitmap1=QRCode.from(lVCard)
                .withColor(0x00000000, R.color.colorPrimary)
                .bitmap();
        qrcode2.setImageBitmap(lBitmap1);*/
        return v;
    }


    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT>=19&&Build.VERSION.SDK_INT<21){
            setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);

        }
        if (Build.VERSION.SDK_INT>=19){
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT>=21){
            setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(Activity activity, final int bits, boolean on){

        Window win=activity.getWindow();
        WindowManager.LayoutParams winParams=win.getAttributes();
        if (on){
            winParams.flags|=bits;
        }else {
            winParams.flags&=~bits;
        }
        win.setAttributes(winParams);
    }
}
