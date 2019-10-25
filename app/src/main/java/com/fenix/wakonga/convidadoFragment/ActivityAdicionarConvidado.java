package com.fenix.wakonga.convidadoFragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.wakonga.MainActivity;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.Convidado;
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
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class ActivityAdicionarConvidado extends AppCompatActivity {

    EditText nome, sobrenome, email, telefone,acompanhante, quantidade;
    CountryCodePicker cp;
    Button guardarDados;
    String Database_Path = "Convite";
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    ImageView fotoConvidado;
    //image request code for onActivityResult
    int IMAGE_REQUEST_CODE=100;
    private AlertDialog waitingDialog;
   TextView selecionarFoto;
    private Uri uri;
    String pastaFotos="Convidados";
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_convidado);
        nome=(EditText)findViewById(R.id.nome);
        sobrenome=(EditText)findViewById(R.id.sobrenome);
        email=(EditText)findViewById(R.id.email);
        telefone=(EditText)findViewById(R.id.telefone_convidado);
        acompanhante=(EditText)findViewById(R.id.acompanhante);
        quantidade=(EditText)findViewById(R.id.numero_convidados);
        fotoConvidado=(ImageView)findViewById(R.id.id_foto_convidado);
        cp=(CountryCodePicker)findViewById(R.id.cpp);
        cp.registerCarrierNumberEditText(telefone);
        selecionarFoto=(TextView)findViewById(R.id.selecionarFoto);

        guardarDados=(Button)findViewById(R.id.guardarDados);

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        //Assign FireBaseStorage instance with root databse name
        storageReference=FirebaseStorage.getInstance().getReference();
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarDados();

            }
        });
        waitingDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Aguarde...")
                .setCancelable(false)
                .build();
        //permite escolher a fot a ser carregada
        selecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                Intent intent = new Intent();
                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), IMAGE_REQUEST_CODE);

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
                fotoConvidado.setImageBitmap(bitmap);
                // After selecting image change choose button above text.
                selecionarFoto.setText("Imagem selecionada");
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    private void carregarDados() {




        mAuth=FirebaseAuth.getInstance();
       final FirebaseUser user=mAuth.getCurrentUser();
        final Convidado convidado=new Convidado();

        nome.setError(null);
        sobrenome.setError(null);
        //email.setError(null);
        telefone.setError(null);
        acompanhante.setError(null);
        quantidade.setError(null);
        acompanhante.setError(null);

        boolean cancel = false;
        View focusView = null;
        //Pattern pat = Pattern.compile ("^[A-Za-z]+$");
      //  Pattern validarTelefone=Pattern.compile("\"^[2-9]{2}[0-9]{9}$\"");

        //nome.matches("^\\p{L}+ \\p{L}+$"))

        if(nome.getText().toString().isEmpty())

        {
            focusView = nome;
            nome.setError("Nome obrigatorio");
            cancel = true;

        }

       /* else if(!nome.getText().toString().matches(pat.toString()))
        {
            focusView = nome;
            nome.setError("Nome invalido");
            cancel = true;
        }*/

        else
        if(sobrenome.getText().toString().isEmpty())
        {
            focusView = sobrenome;
            sobrenome.setError("Sobrenome obrigatorio");
            cancel = true;
        }

        /*else if(!sobrenome.getText().toString().matches(pat.toString()))
        {
            focusView = sobrenome;
            sobrenome.setError("Sobrenome invalido");
            cancel = true;

        }*/

       /* else
        if(email.getText().toString().isEmpty())

        {
            focusView = email;
            email.setError("Email obrigatorio");
            cancel = true;

        }

        else if(!email.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString()))
        {
            focusView = email;
            email.setError("Email invalido");
            cancel = true;

        }*/

        else
        if(telefone.getText().toString().isEmpty())

        {
            focusView = telefone;
            telefone.setError("Telefone obrigatorio");
            cancel = true;

        }
         else
        if(telefone.getText().toString().length()!=9)
        {

            focusView = telefone;
            telefone.setError("Telefone invalido");
            cancel = true;
        }
       else
        if(acompanhante.getText().toString().isEmpty())

        {
            focusView = acompanhante;
            acompanhante.setError("Informe acompanhte");
            cancel = true;

        }

        /*else if(!acompanhante.getText().toString().matches(pat.toString()))
        {
            focusView = acompanhante;
            acompanhante.setError("Nome invalido");
            cancel = true;

        }*/else if ( Integer.parseInt(quantidade.getText().toString())>2){
            focusView = quantidade;
            quantidade.setError("Maximo duas pessoas por convite ");
            cancel = true;
        }else if ( Integer.parseInt(quantidade.getText().toString())<=0){
            focusView = quantidade;
            quantidade.setError("Numero invalido");
            cancel = true;
        }


        if (cancel) {

            Animation animation=AnimationUtils.loadAnimation(ActivityAdicionarConvidado.this, android.R.anim.fade_in);

            focusView.requestFocus();
            focusView.startAnimation(animation);
           // focusView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));





        }

        else {
            convidado.setNome(nome.getText().toString().trim());
            convidado.setSobrenome(sobrenome.getText().toString().trim());
            convidado.setEmail(email.getText().toString().trim());
            convidado.setTelefone(cp.getFullNumberWithPlus());
            convidado.setAcompanhante(acompanhante.getText().toString().trim());
            convidado.setNumeroConvidados(Integer.parseInt(quantidade.getText().toString().trim()));






        if (uri!=null){



            waitingDialog.show();
            String image=UUID.randomUUID().toString();

            StorageReference storageReference2nd=storageReference.child(pastaFotos+"/"+image);


            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri uri1=uriTask.getResult();




                            convidado.setFoto(uri1.toString().trim());
                            databaseReference.child(user.getUid()).child(convidado.toString()).push().setValue(convidado);
                            Intent intent=new Intent(ActivityAdicionarConvidado.this, ListaConvidados.class);
                            startActivity(intent);
                            finish();



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            waitingDialog.dismiss();
                            Toast.makeText(ActivityAdicionarConvidado.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            waitingDialog.show();                        }
                    });


        }
        else {
            //Toast.makeText(ActivityAdicionarConvidado.this, "Por favor selecione uma imagem!", Toast.LENGTH_LONG).show();
            //Picasso.get().load(R.drawable.addfoto).into(fotoConvidado);
            convidado.setFoto(null);
            databaseReference.child(user.getUid()).child(convidado.toString()).push().setValue(convidado);
            Intent intent=new Intent(ActivityAdicionarConvidado.this, MainActivity.class);
            startActivity(intent);
            finish();


        }
        }


    }


}

