package com.fenix.wakonga.galeria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.FotoCasamento;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AdicionarFotosAlbum extends AppCompatActivity {

    ImageButton mSelectBtn;
    RecyclerView mUploadoList;
    private int RESULT_LOAD_IMAGE=1;
    private List<String> fileNameList;
    private List<String> fileImageList;
    Button guardarDados;

    private List<String> fileDoneList;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;


    String pastaFotos="Album";
    String Database_Path = "Convite";
    private FirebaseAuth mAuth;



    StorageReference storageReference;
    FirebaseDatabase mFirebaseDatabase;




    private UploadListAdapter uploadListAdapter;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_fotos_album);

        mSelectBtn=findViewById(R.id.selectbtn);
        mUploadoList=findViewById(R.id.uploadimagens);
        guardarDados=findViewById(R.id.guardarFotos);
        fileNameList=new ArrayList<>();
        fileImageList=new ArrayList<>();

        fileDoneList=new ArrayList<>();

        uploadListAdapter=new UploadListAdapter(fileNameList, fileDoneList, fileImageList);

        mUploadoList.setLayoutManager(new LinearLayoutManager(this));
        mUploadoList.setHasFixedSize(true);
        mUploadoList.setAdapter(uploadListAdapter);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mFirebaseDatabase= FirebaseDatabase.getInstance();


        //Assign FireBaseStorage instance with root databse name
        storageReference=FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);

        mStorage= FirebaseStorage.getInstance().getReference();

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione as fotos"), RESULT_LOAD_IMAGE);
            }
        });

        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adionarFotos();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            if (data.getClipData()!=null){


                mSelectBtn.setVisibility(View.GONE);


               int  totalItemSelected=data.getClipData().getItemCount();

                for (int i=0; i<totalItemSelected;i++){
                    final Uri fileUri=data.getClipData().getItemAt(i).getUri();
                   String fileName=getFileName(fileUri);
                    fileNameList.add(fileName);
                    fileImageList.add(fileUri.toString());
                    //fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();
                   /* progressDialog.setTitle("A carregar fotos...");
                    progressDialog.show();
                    mAuth= FirebaseAuth.getInstance();
                   final FirebaseUser user=mAuth.getCurrentUser();

                    StorageReference fileToUpLoad=mStorage.child(pastaFotos).child(user.getUid()).child(fileName);
                    fileToUpLoad.putFile(fileUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    FotoCasamento fotoCasamento=new FotoCasamento();
                                    fotoCasamento.setFoto(fileUri.toString());
                                    databaseReference.child(user.getUid()).child(fotoCasamento.toString()).push().setValue(fotoCasamento);
                                    //chamar a tela de listagem depois que a img for carregada
                                    Intent intent=new Intent(getApplicationContext(), ActivityFotos.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.hide();

                                }
                            });*/
                }

                //Toast.makeText(getApplicationContext(), "Selected Multiple Files", Toast.LENGTH_LONG).show();

                guardarDados.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int  totalItemSelected=data.getClipData().getItemCount();

                        for (int i = 0; i<totalItemSelected; i++){
                            final Uri fileUri=data.getClipData().getItemAt(i).getUri();
                            String fileName=getFileName(fileUri);
                           /* fileNameList.add(fileName);
                            fileImageList.add(fileUri.toString());
                            //fileDoneList.add("uploading");
                            uploadListAdapter.notifyDataSetChanged();*/

                            Glide.with(getApplicationContext())
                                    .load(R.drawable.logoeditado)
                                    .into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                //rootContent.setBackground(resource);
                                                mSelectBtn.setImageDrawable(resource);
                                            }
                                        }
                                    });
                            mSelectBtn.setVisibility(View.VISIBLE);
                            mUploadoList.setVisibility(View.GONE);



                            //progressDialog.setTitle("A carregar fotos...");
                            //progressDialog.show();
                            mAuth= FirebaseAuth.getInstance();
                            final FirebaseUser user=mAuth.getCurrentUser();

                            StorageReference fileToUpLoad=mStorage.child(pastaFotos).child(user.getUid()).child(fileName);

                            if (i==0){
                                Toast.makeText(getApplicationContext(), (i+1)+" Foto adicionada", Toast.LENGTH_LONG).show();
                            }else if (i>=1){
                                Toast.makeText(getApplicationContext(), (i+1)+" Fotos adicionadas", Toast.LENGTH_LONG).show();
                            }

                            fileToUpLoad.putFile(fileUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            FotoCasamento fotoCasamento=new FotoCasamento();
                                            fotoCasamento.setFoto(fileUri.toString());
                                            databaseReference.child(user.getUid()).child(fotoCasamento.toString()).push().setValue(fotoCasamento);
                                            //chamar a tela de listagem depois que a img for carregada

                                            Intent intent=new Intent(getApplicationContext(), ActivityFotos.class);
                                            startActivity(intent);
                                            finish();
                                            //progressDialog.hide();

                                        }
                                    });

                        }


                        //Toast.makeText(getApplicationContext(), "Selected Multiple Files", Toast.LENGTH_LONG).show();

                    }
                });


            }else if (data.getData()!=null){
                mSelectBtn.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Selecione varias fotos", Toast.LENGTH_LONG).show();

            }
        }
    }
/*
    private void adionarFotos() {

        if (fileUri!=null) {

            for (int i = 0; i < totalItemSelected; i++) {


                fileName=getFileName(fileUri);
                //fileNameList.add(fileName);
                //fileImageList.add(fileUri.toString());
                //fileDoneList.add("uploading");
                //uploadListAdapter.notifyDataSetChanged();
                progressDialog.setTitle("A carregar fotos...");
                progressDialog.show();
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                StorageReference fileToUpLoad = mStorage.child(pastaFotos).child(user.getUid()).child(fileName);
                fileToUpLoad.putFile(fileUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                FotoCasamento fotoCasamento = new FotoCasamento();
                                fotoCasamento.setFoto(fileUri.toString());

                                databaseReference.child(user.getUid()).child(fotoCasamento.toString()).push().setValue(fotoCasamento);
                                //chamar a tela de listagem depois que a img for carregada
                                Intent intent = new Intent(getApplicationContext(), ActivityFotos.class);
                                startActivity(intent);
                                finish();

                                progressDialog.hide();

                            }
                        });

            }

            //Toast.makeText(getApplicationContext(), "Selected Multiple Files", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "Nenhuma imagem selecionada", Toast.LENGTH_LONG).show();

        }


    }



    */
    public String getFileName(Uri uri){
        String result=null;
        if (uri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor!=null&&cursor.moveToFirst()){
                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }finally {
                cursor.close();
            }
        }
        if (result==null){
            result=uri.getPath();
            int cut=result.lastIndexOf('/');
            if (cut!=-1){
                result=result.substring(cut+1);
            }
        }
        return  result;
    }

}
