package com.fenix.wakonga.noivos;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.wakonga.MainActivity;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.FotoDeCapaModelo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

public class FotoCapa extends AppCompatActivity {

    ImageView fotoCapa;
    Button guardarFoto;
    TextView procurar_foto_capa;

    //banco de firebase database
    String Database_Path = "Convite";
    //Pasta de fotos do firebase Storage
    String pastaFotos="FotoDeCapaModelo";
    private AlertDialog waitingDialog;


    //uri
    Uri uri;
    //StorageReference e databaseReference
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private FirebaseAuth mAuth;

    //image request code for onActivityResult
    int IMAGE_REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_capa);
        fotoCapa=findViewById(R.id.fotocapa);
        guardarFoto=findViewById(R.id.guardarFoto);
        procurar_foto_capa=findViewById(R.id.id_procurar_foto_capa);

        //Assign FireBaseStorage instance with root databse name
        storageReference= FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        waitingDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Aguarde...")
                .setCancelable(false)
                .build();
        //permite escolher a fot a ser carregada
        procurar_foto_capa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating intent.
                Intent intent = new Intent();
                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Escolher foto de capa"), IMAGE_REQUEST_CODE);

            }
        });
        guardarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarFotoCapa();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();


            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // Setting up bitmap selected image into ImageView.
                fotoCapa.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                 procurar_foto_capa.setText("Foto selecionada");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    private void carregarFotoCapa() {
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        if (uri!=null){

            waitingDialog.show();


            //String image= UUID.randomUUID().toString();

            //StorageReference storageReference2nd=storageReference.child(pastaFotos+"/"+image);
            final StorageReference storageReference2nd=storageReference.child(pastaFotos+"/"+user.getUid()+"/"+System.currentTimeMillis()+"."+GetFileExtension(uri));


            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FotoDeCapaModelo fotoCapaModelo=new FotoDeCapaModelo();

                           Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri uri1=uriTask.getResult();

                            //ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString());
                            fotoCapaModelo.setFoto(uri1.toString());



                            databaseReference.child(user.getUid()).child(fotoCapaModelo.toString()).setValue(fotoCapaModelo);
                            Intent intent=new Intent(FotoCapa.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            waitingDialog.dismiss();

                            Toast.makeText(FotoCapa.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            waitingDialog.show();
                        }
                    });
        }
        else {
            Toast.makeText(FotoCapa.this, "Selecione uma foto de capa!", Toast.LENGTH_LONG).show();
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
}