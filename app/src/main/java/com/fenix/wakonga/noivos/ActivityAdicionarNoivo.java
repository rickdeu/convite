package com.fenix.wakonga.noivos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.DatePickerDialog.OnDateSetListener;

import com.fenix.wakonga.R;
import com.fenix.wakonga.informacaoCasamento.NoivosCasamento;
import com.fenix.wakonga.model.Noivo;
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
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class ActivityAdicionarNoivo extends AppCompatActivity {

    EditText nome, sobrenome, telefone,casa,bairro, bilhete_identidade, email;
    ImageView fotoNoivo;
    int ano, mes, dia;
    Button guardarDados;
    EditText data_nascimento;
    //banco de firebase database
    String Database_Path = "Convite";
    //Pasta de fotos do firebase Storage
    String pastaFotos="Noivos";
    private AlertDialog waitingDialog;



    //uri
    Uri uri;
    //StorageReference e databaseReference
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private FirebaseAuth mAuth;

    //image request code for onActivityResult
    int IMAGE_REQUEST_CODE=100;
    private CountryCodePicker cp;
    //ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noivo);
        Calendar calendar=Calendar.getInstance();
        ano=calendar.get(Calendar.YEAR);
        mes=calendar.get(Calendar.MARCH);
        dia=calendar.get(Calendar.DAY_OF_MONTH);


        data_nascimento= findViewById(R.id.data_nascimento);
        //data_nascimento.setText(dia+"/"+(mes+1)+"/"+ano);

        nome=(EditText)findViewById(R.id.nome);
        sobrenome=(EditText)findViewById(R.id.sobrenome);
        telefone=(EditText)findViewById(R.id.telefone);
        cp=(CountryCodePicker)findViewById(R.id.cpp);
        cp.registerCarrierNumberEditText(telefone);
        casa=(EditText)findViewById(R.id.casa);
        bairro=(EditText)findViewById(R.id.bairro);
        bilhete_identidade=(EditText) findViewById(R.id.bi);
        email=(EditText)findViewById(R.id.email);

        fotoNoivo=(ImageView)findViewById(R.id.id_foto_noivo);
        guardarDados=(Button)findViewById(R.id.guardarDados);

        //selecionarFoto=(TextView)findViewById(R.id.selecionar_img);

        //Assign FireBaseStorage instance with root databse name
        storageReference=FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        // Assigning Id to ProgressDialog.
        //progressDialog = new ProgressDialog(ActivityAdicionarNoivo.this);
        waitingDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Aguarde...")
                .setCancelable(false)
                .build();

        //permite escolher a fot a ser carregada
        fotoNoivo.setOnClickListener(new View.OnClickListener() {
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




        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarDados();

            }
        });
    }

    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.data_nascimento==id){
            return new DatePickerDialog(this, listener, ano,mes,dia);
        }
        return null;
    }

    private OnDateSetListener listener=new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            ano=year;
            mes=month;
            dia=dayOfMonth;
            data_nascimento.setText(dia+"-"+(mes+1)+"-"+ano);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();


            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // Setting up bitmap selected image into ImageView.
                fotoNoivo.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
              //  selecionarFoto.setText("Imagem selecionada");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void carregarDados() {

        mAuth=FirebaseAuth.getInstance();
       final FirebaseUser user=mAuth.getCurrentUser();
       final Noivo noivo=new Noivo();

       nome.setError(null);
       sobrenome.setError(null);
       telefone.setError(null);
       data_nascimento.setError(null);
       bilhete_identidade.setError(null);
       email.setError(null);
       casa.setError(null);
       bairro.setError(null);
        boolean cancel = false;
        View focusView = null;
        Pattern pat = Pattern.compile ("^[A-Za-z\\s]{1,}[A-Za-z\\p{L}][\\.]{0,1}[A-Za-z\\s]{0,}$");
        Pattern bilhete=Pattern.compile("^[0-9]{9}[A-Z]{2}[0-9]{3}$");
        Pattern enderecovalidar=Pattern.compile("^[0-9][A-Za-z]$");
        //Pattern validarTelefone=Pattern.compile("^[9]{1}[1-9]{8}$");
       // Pattern validarData=Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\\\d{2})$");

        if(nome.getText().toString().isEmpty())
        {
            focusView = nome;
            nome.setError("Nome obrigatorio");
            cancel = true;
        }
        else if(!nome.getText().toString().matches(pat.toString()))
        {
            focusView = nome;
            nome.setError("Nome invalido");
            cancel = true;
        }
        else
        if(sobrenome.getText().toString().isEmpty())
        {
            focusView = sobrenome;
            sobrenome.setError("Sobrenome obrigatorio");
            cancel = true;
        }
        else if(!sobrenome.getText().toString().matches(pat.toString()))
        {
            focusView = sobrenome;
            sobrenome.setError("Sobrenome invalido");
            cancel = true;

        }
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

        }else if (data_nascimento.getText().toString().isEmpty()||
                data_nascimento.getText().toString().
                        equalsIgnoreCase("Data Nascimento (Clique para adicionar data)")){
            focusView = data_nascimento;
            data_nascimento.setError("Campo data obrigatorio");
            cancel = true;
        }


        else if (bilhete_identidade.getText().toString().isEmpty()){
            focusView = bilhete_identidade;
            bilhete_identidade.setError("Campo B.I obrigatorio");
            cancel = true;
        }else if (!bilhete_identidade.getText().toString().matches(bilhete.toString())){
            focusView = bilhete_identidade;
            bilhete_identidade.setError("Numero B.I Invalido");
            cancel = true;

        }

        else
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

        }else if (casa.getText().toString().isEmpty()){
            focusView = casa;
            casa.setError("Campo Obrigatorio");
            cancel = true;

        }else if (casa.getText().toString().matches(enderecovalidar.toString())){
            focusView = casa;
            casa.setError("Campo casa invalido");
            cancel = true;
        }else if (bairro.getText().toString().isEmpty()){
            focusView = bairro;
            bairro.setError("Campo Obrigatorio");
            cancel = true;
        }else if (bairro.getText().toString().matches(enderecovalidar.toString())){
            focusView = bairro;
            bairro.setError("Campo casa invalido");
            cancel = true;
        } if (cancel) {

            Animation animation=AnimationUtils.loadAnimation(ActivityAdicionarNoivo.this, android.R.anim.fade_in);

            focusView.requestFocus();
            focusView.startAnimation(animation);
            // focusView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));





        }
        else {
            noivo.setNome(nome.getText().toString().trim());
            noivo.setSobrenome(sobrenome.getText().toString().trim());

            noivo.setDataNascimento(data_nascimento.getText().toString().trim());
            noivo.setTelefone(cp.getFullNumberWithPlus());
            noivo.setCasa(casa.getText().toString().trim());
            noivo.setBairro(bairro.getText().toString().trim());
            noivo.setEmail(email.getText().toString().trim());
            noivo.setBi(bilhete_identidade.getText().toString().trim());
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

        noivo.setFoto(uri1.toString().trim());

        databaseReference.child(user.getUid()).child(noivo.toString()).setValue(noivo);
        Intent intent=new Intent(ActivityAdicionarNoivo.this, NoivosCasamento.class);
        startActivity(intent);
        finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            waitingDialog.dismiss();

                            Toast.makeText(ActivityAdicionarNoivo.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(ActivityAdicionarNoivo.this, "Selecione uma foto para o perfil!", Toast.LENGTH_LONG).show();
        }}

    }





}

