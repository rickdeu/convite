package com.fenix.wakonga.scannerFragment;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.wakonga.MainActivity;
import com.fenix.wakonga.R;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{

    BarcodeReader barcodeReader;
    TextView terminar_verifcao;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //get the barcode reader instance
        barcodeReader=(BarcodeReader)getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
        terminar_verifcao=findViewById(R.id.terminar_verificacao);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        //verifica se um usuario esta logado
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){
            mRef=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Convidados");
        }

        terminar_verifcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });



    }

   @Override
    public void onScanned(final Barcode barcode) {
        //playing barcode reader  beep sound
        barcodeReader.playBeep();
        mRef.child(barcode.displayValue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getKey().equals(barcode.displayValue)){
                    Intent intent=new Intent(ScanActivity.this, ConvidadoIntruso.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(ScanActivity.this, ConvidadoConfirmado.class);
                    intent.putExtra("ConvidadoId", barcode.displayValue);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG);
            }
        });


       /* if (mRef.getKey().equals(barcode.displayValue)){
            Intent intent=new Intent(ScanActivity.this, ConvidadoConfirmado.class);
           // Log.e("TAG","Key"+mRef.getKey());
            intent.putExtra("ConvidadoId", barcode.displayValue);
            startActivity(intent);
        }else {
            Intent intent=new Intent(ScanActivity.this, ConvidadoIntruso.class);
            startActivity(intent);
        }*/


    }
    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Erro durante a verificação: "+s, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onCameraPermissionDenied() {
finish();
    }


}
