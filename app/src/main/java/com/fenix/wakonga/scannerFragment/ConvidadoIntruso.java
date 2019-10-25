package com.fenix.wakonga.scannerFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fenix.wakonga.R;

public class ConvidadoIntruso extends AppCompatActivity {

    VideoView simpleVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_convidado_intruso);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();


      /*  VideoView mVideoView = (VideoView)findViewById(R.id.id_video);

        String uriPath = "com.fenix.wakonga.scannerFragment/"+R.drawable.video;

        Uri uri = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        mVideoView.start();*/
        // create an object of media controller
        MediaController mediaController = new MediaController(this);
        // initiate a video view
        simpleVideoView = (VideoView) findViewById(R.id.simpleVideoView);
        simpleVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videopato));
        // set media controller object for a video view
        // simpleVideoView.setMediaController(mediaController);
        simpleVideoView.start(); // start a video
        int t =5000;//tempo de espera para abertura da tela principal
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ConvidadoIntruso.this, ScanActivity.class));
                finish();
            }
        },t);
    }
}
