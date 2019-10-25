package com.fenix.wakonga.convidadoFragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.wakonga.R;
import com.fenix.wakonga.envioemail.Mail;
import com.fenix.wakonga.model.Conservatoria;
import com.fenix.wakonga.model.Convidado;
import com.fenix.wakonga.model.Dizeres;
import com.fenix.wakonga.model.Festa;
import com.fenix.wakonga.model.Igreja;
import com.fenix.wakonga.model.Noiva;
import com.fenix.wakonga.model.Noivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class DetalheConvidado extends AppCompatActivity {
    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;
    private static final  int REQUEST_CALL=1;
    TextView nome, sobrenome, telefone, email, acompanhante,quantidade;
    ImageView foto, codigoQrImagem;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    String idConvidado="";
    LinearLayout chamar_convidado;
    LinearLayout enviar_email;
    Button gerarCodigo;
    Bitmap bitmapQr;
    Bitmap qr;
    View qrView;
    DatabaseReference noivo;
    DatabaseReference dizeres;
    DatabaseReference noiva;
    DatabaseReference civil;
    DatabaseReference igreja;
    DatabaseReference festa;
    String dizer1, dizer2, dizer3;
    String nomenoivo;
    String nomenoiva;
    String nomeconvidado;
    String nomeconservatoria;
    String nomeigreja;
    String nomesalao;
    Uri file;
    Button ver_convite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_convidado);
        nome=findViewById(R.id.nome_detalhe_convidado);
        foto=findViewById(R.id.img_detalhe_convidado);
        sobrenome=findViewById(R.id.sobrenome_detalhe_convidado);
        telefone=findViewById(R.id.telefone_detalhe_convidado);
        email=findViewById(R.id.email_detalhe_convidado);
        acompanhante=findViewById(R.id.acompanhante_detalhe_convidado);
        quantidade=(TextView)findViewById(R.id.numero_convidados_detalhe);
        gerarCodigo=(Button)findViewById(R.id.gerar_codigo_qr);
        ver_convite=findViewById(R.id.ver_convite);


        chamar_convidado=(LinearLayout)findViewById(R.id.chamar_convidado);
        enviar_email=(LinearLayout)findViewById(R.id.enviar_email);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Detalhes");


        chamar_convidado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligarParaConvidado();
                
                ver_convite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });



            }
        });
        gerarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verCodigoQr();
            }
        });
        enviar_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   mostrarDialogEnviarMensagem();
                if (haveNetworkConnection()){
                    dialogEnviarConvite();
                }else {
                    Toast.makeText(getApplicationContext(), "Sem ligação a internet", Toast.LENGTH_LONG).show();
                }

            }
        });
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        //verifica se um usuario esta logado
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){
            mRef=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Convidados");

            noivo=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Noivo");
            noiva=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Noiva");
            civil=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Conservatoria");
            igreja=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Igreja");
            festa=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Festa");
            dizeres=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Dizeres");

          }
          if (getIntent()!=null){
            idConvidado=getIntent().getStringExtra("ConvidadoId");
          }
          if (!idConvidado.isEmpty()){
            getDetalheConvidado(idConvidado);
          }
          dizeresConvite();
          dadosNoivo();
          dadosNoiva();
          dadosConservatoria();
          dadosIgreja();
          dadosSalao();

        //Dizeres do convite
        //mensagem=dizer1+"\n"+nomenoivo+" "+nomenoiva+"\n"+dizer2+"\n"+nomeconvidado+"\n"+dizer3;

    }



    private void verCodigoQr() {
        String nomeDialog=nome.getText().toString() +" "+sobrenome.getText().toString();
        final  android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        builder.setTitle("Codigo QR");
        builder.setMessage("Ver Codigo QR  "+nomeDialog+" ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gerarCodigoQrConvidado();

            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builder.create().show();
    }

    private void gerarCodigoQrConvidado() {

        qrView= LayoutInflater.from(this)
                .inflate(R.layout.qr_detalhe_convidado,null);
        codigoQrImagem=qrView.findViewById(R.id.qrcode_convidado);

        String nomeDialog=nome.getText().toString() +" "+sobrenome.getText().toString();
        codigoQrImagem.setImageBitmap(qr);
        bitmapQr=((BitmapDrawable)codigoQrImagem.getDrawable()).getBitmap();


        final androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Codigo QR de "+nomeDialog);
        builder.setView(qrView);
        //builder.setIcon(R.drawable.icon);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                        String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
                    }else {
                        salveImage();
                    }
                }else {
                    salveImage();
                }
            }
        });
        builder.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
    private void getDetalheConvidado(final String idConvidado) {
    mRef.child(idConvidado).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Convidado convidado=dataSnapshot.getValue(Convidado.class);
            nome.setText(convidado.getNome());
            sobrenome.setText(convidado.getSobrenome());
            telefone.setText(convidado.getTelefone());
            acompanhante.setText(convidado.getAcompanhante());
            email.setText(convidado.getEmail());
            quantidade.setText(Integer.toString(convidado.getNumeroConvidados()) );
            nomeconvidado=convidado.getNome()+" "+convidado.getSobrenome();
            qr=QRCode.from(dataSnapshot.getKey()).bitmap();

           // file  = getImageUri(getApplicationContext(), qr);

            if (convidado.getFoto()!=null){
                Picasso.get().load(convidado.getFoto()).into(foto);
            }else {
                Picasso.get().load(R.drawable.logoeditado).into(foto);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }

    //convert bitmap em URI
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, nome.toString(), null);

        return Uri.parse(path);
    }




    private void ligarParaConvidado() {
        String nomeDialog=nome.getText().toString() +" "+sobrenome.getText().toString();
        final  android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        builder.setTitle("Ligar para o convidado");
        builder.setMessage("Pretende ligar para  "+nomeDialog+" ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ligarConvidado();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }


    //instrucao para chamada telefonica
    private void ligarConvidado() {
        String numero=telefone.getText().toString();
        if (numero.trim().length()>0){
            if (ContextCompat.checkSelfPermission(DetalheConvidado.this,
                    Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(DetalheConvidado.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else {
                String dial="tel: "+numero;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else {
            Toast.makeText(DetalheConvidado.this, "Numero de telefone em falta!", Toast.LENGTH_LONG).show();
        } }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CALL){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                ligarConvidado();
            }else {
                Toast.makeText(this, "Permissao Negada", Toast.LENGTH_SHORT).show();
            }
        }
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length>0&&grantResults[0]==
                        PackageManager.PERMISSION_GRANTED){
                    salveImage();
                }else {
                    Toast.makeText(this,"Permissao desativada, active para guardar", Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void enviarConvite(){
        String destinatarioEmail[]={email.getText().toString()};
        //String destinatarioEmail[]={"fenixinovation@gmail.com"};
        // String assuntoEmail="Convite de Casamento";
       // String mensagemEmail="Convidamos o sehor";
        Mail mail=new Mail();
        mail.setContext(this);
        mail.setEmail(destinatarioEmail);
        mail.setSubject("Convite");
        mail.setMessage(dizer1+"\n\n"+nomenoivo+" & "+nomenoiva+"\n\n"+dizer2+"\n\n"+nomeconvidado+"\n\n"+dizer3+"\n\n\n"+nomeconservatoria+"\n\n\n"+nomeigreja+"\n\n\n"+nomesalao+"\n\nDesculpa o incoveniente, estamos em fase de texte de uma aplicação, gostariamos que respondesse este email, para certificar que o email foi recebido!\n\nFENIX INNOVATION\nObrigado");
        //mail.addAttachment(getImageUri(getApplicationContext(), qr).toString());
       // mail.setPass("wakonga123fenix123");
      // mail.setUser("wakonga78@gmail.com");
        mail.setPass("916372177");
        mail.setUser("hangaloandre@gmail.com");
        mail.execute();
        //new Mail(DetalheConvidado.this, destinatarioEmail, assuntoEmail, mensagemEmail).execute();
    }

    public void dadosSalao(){
        festa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Festa festa=dataSnapshot.getValue(Festa.class);
                if (festa!=null){
                    nomesalao="COPO DE ÁGUA\n"+"\t"+festa.getSalao()+"\n"+"\tDia: "+festa.getData()+"\n"+"\tHora: "+festa.getHora();


                }else {
                    Toast.makeText(getApplicationContext(), "Dados do Salao em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void dadosConservatoria(){
        civil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Conservatoria conservatoria=dataSnapshot.getValue(Conservatoria.class);
                if (conservatoria!=null){
                    nomeconservatoria="CERIMÔNIA CĨVIL\n"+"\t"+conservatoria.getNome()+"\n"+"\tDia: "+conservatoria.getData()+"\n"+"\tHora: "+conservatoria.getHora();

                }else {
                    Toast.makeText(getApplicationContext(), "Dados da Conservatoria em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void dadosIgreja(){
        igreja.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Igreja igreja=dataSnapshot.getValue(Igreja.class);
                if (igreja!=null){
                    nomeigreja="CERIMÔNIA RELIGIOSA\n"+"\t"+igreja.getNome()+"\n"+"\tDia: "+igreja.getData()+"\n"+"\tHora: "+igreja.getHora();


                }else {
                    Toast.makeText(getApplicationContext(), "Dados da Igreja em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void dizeresConvite(){
        dizeres.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dizeres dizeres=dataSnapshot.getValue(Dizeres.class);
                if (dizeres!=null){
                    dizer1=dizeres.getMensagem3();
                    dizer2=dizeres.getTexto2();
                    dizer3=dizeres.getTexto1();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Dados do convite em falta!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void dadosNoivo(){
        noivo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noivo noivo=dataSnapshot.getValue(Noivo.class);
                if (noivo!=null){
                    nomenoivo=noivo.getNome()+" "+noivo.getSobrenome();


                }else {
                    Toast.makeText(getApplicationContext(), "Dados do Noivo em falta", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void dadosNoiva(){
        noiva.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noiva noiva=dataSnapshot.getValue(Noiva.class);

                if (noiva!=null){
                    nomenoiva=noiva.getNome()+" "+noiva.getSobrenome();

                }else {
                    Toast.makeText(getApplicationContext(), "Dados da Noiva em falta", Toast.LENGTH_LONG).show();
                }
 }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void dialogEnviarConvite(){
        String nomeDialog=nome.getText().toString() +" "+sobrenome.getText().toString();
        final  android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
                    builder.setTitle("Enviar Convie");
                   // builder.setIcon(R.drawable.logoconvite);
                    builder.setMessage("Enviar convite para "+nomeDialog+" ?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enviarConvite();
                        }
                    });
                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
    }
    private void salveImage() {
       // String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss",
               // Locale.getDefault()).format(System.currentTimeMillis());
        String nomeDialog=nome.getText().toString() +" "+sobrenome.getText().toString();
        File path = Environment.getExternalStorageDirectory();
        File dir=new File(path+"/Casamento/"+"Convidados/");
        dir.mkdirs();
        String imageName=nomeDialog+".PNG";
        File file =new File(dir, imageName);
        OutputStream out;
        try {
            out=new FileOutputStream(file);
            bitmapQr.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, imageName+" Guardado em"+dir, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //verifica se tem conexao a internet
    private boolean haveNetworkConnection(){
        boolean haveConnectionWifi=false;
        boolean haveConnectionMobile=false;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfos=cm.getAllNetworkInfo();
        for (NetworkInfo ni:networkInfos){
            if (ni.getTypeName().equalsIgnoreCase("WIFI")){
                if (ni.isConnected())
                    haveConnectionWifi=true;}

            if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                if (ni.isConnected())
                    haveConnectionMobile=true;}
        }
        return haveConnectionWifi || haveConnectionMobile;
    }

}
