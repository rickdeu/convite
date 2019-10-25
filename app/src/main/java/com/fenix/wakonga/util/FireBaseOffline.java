package com.fenix.wakonga.util;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class FireBaseOffline extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //if (!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseReference scoresRef=FirebaseDatabase.getInstance().getReference("wakonga-e3e9c");
            scoresRef.keepSynced(true);
        //}
    }
}
