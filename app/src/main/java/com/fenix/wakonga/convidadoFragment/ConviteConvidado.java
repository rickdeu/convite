package com.fenix.wakonga.convidadoFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.Conservatoria;
import com.fenix.wakonga.model.Convidado;
import com.fenix.wakonga.model.Dizeres;
import com.fenix.wakonga.model.Festa;
import com.fenix.wakonga.model.FundoEstiloConvite;
import com.fenix.wakonga.model.Igreja;
import com.fenix.wakonga.model.Noiva;
import com.fenix.wakonga.model.Noivo;
import com.fenix.wakonga.screeshotConvite.ScreenshotUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.glxn.qrgen.android.QRCode;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.FileProvider.getUriForFile;

public class ConviteConvidado extends AppCompatActivity {

    TextView nomeNoivo, sobrenomeNoivo,dataNascimentoNoivo, telefoneNoivo, casaNoivo,bairroNoivo, emailNoivo, biNoivo, noivoTexto;
    ImageView fotoNoivo, fraseConvite, textocasamentoimagem, textocasmentoimagem1;
    TextView nomeNoiva, sobrenomeNoiva,dataNascimentoNoiva, telefoneNoiva, casaNoiva,bairroNoiva, emailNoiva, biNoiva, noivaTexto;
    TextView nomeConservatoria, dataConservatoria, horaConservatoria;
    TextView nomeIgreja, dataIgreja, horaIgreja;
    TextView nomeSalao, dataSalao, horaSalao;
    TextView cerimonia_civl, cerimonia_religiosa,copo_agua;
    TextView mensagemconvite, textoConvidar, nossoCasamento;
    TextView idConvidadoConvite, acompanhante, numeroConvidado;
    String idConvidado="";
    CircleImageView imageQrConvidado;
    String emailConvidado;


    TextView noivaConvite, noivoConvite;
    ImageView fotoNoiva;
    View viewFraseconvite,textoConvite, nossoCasamentoView;
    EditText inserirFrase, inserirTextoCasamento, inserirTextoNossoCasamento;


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference noivo;
    DatabaseReference dizeres;
    DatabaseReference noiva;
    DatabaseReference civil;
    DatabaseReference igreja;
    DatabaseReference festa;
    DatabaseReference mRef;
    DatabaseReference fundoPrincipal;
    ImageView imagemSecundaria;
    Drawable drawable;





    private FirebaseAuth mAuth;
    private Bitmap qr;
    private LinearLayout rootContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_convite_convidado);


        idConvidadoConvite=findViewById(R.id.id_convidado_convite);
        acompanhante=findViewById(R.id.acompanhante_convite_convidado);
        numeroConvidado=findViewById(R.id.numero_convite_detalhe);
        imageQrConvidado=findViewById(R.id.img_qr_convido);

        findViews();

        //Dados do noivo
        //nomeNoivo=findViewById(R.id.nomeNoivo);
        fotoNoivo=findViewById(R.id.fotoNoivo);
        sobrenomeNoivo=findViewById(R.id.sobrenomeNoivo);
        dataNascimentoNoivo=findViewById(R.id.data_nascimento_noivo);
        telefoneNoivo=findViewById(R.id.telefone_noivo);
        casaNoivo=findViewById(R.id.casa_noivo);
        bairroNoivo=findViewById(R.id.morada_noivo);
        emailNoivo=findViewById(R.id.email_noivo);
        biNoivo=findViewById(R.id.bilhete_noivo);
        noivoTexto=findViewById(R.id.noivo);


        //Dados da noiva
       // nomeNoiva=findViewById(R.id.nomeNoiva);
        fotoNoiva=findViewById(R.id.fotoNoiva);
        sobrenomeNoiva=findViewById(R.id.sobrenomeNoiva);
        dataNascimentoNoiva=findViewById(R.id.data_nascimento_noiva);
        telefoneNoiva=findViewById(R.id.telefone_noiva);
        casaNoiva=findViewById(R.id.casa_noiva);
        bairroNoiva=findViewById(R.id.morada_noiva);
        emailNoiva=findViewById(R.id.email_noiva);
        biNoiva=findViewById(R.id.bilhete_noiva);
        noivaTexto=findViewById(R.id.noiva);


        //Dados conservatoria
        nomeConservatoria=findViewById(R.id.nome_conservatoria);
        dataConservatoria=findViewById(R.id.data_conservatoria);
        horaConservatoria=findViewById(R.id.hora_conservatoria);
        cerimonia_civl=findViewById(R.id.cerimonia_civl);

        //Dados Igreja
        nomeIgreja=findViewById(R.id.nome_igreja);
        dataIgreja=findViewById(R.id.data_igreja);
        horaIgreja=findViewById(R.id.hora_igreja);
        cerimonia_religiosa=findViewById(R.id.cerimonia_religiosa);

        //Dados Salao
        nomeSalao=findViewById(R.id.nome_salao);
        // dataSalao=v.findViewById(R.id.data_igreja);
        horaSalao=findViewById(R.id.hora_salao);
        copo_agua=findViewById(R.id.copo_agua);
        noivoConvite=findViewById(R.id.noivo_convite);
        noivaConvite=findViewById(R.id.noiva_convite);
        fraseConvite=findViewById(R.id.fraseconvite);
        textocasamentoimagem=findViewById(R.id.textocasamentoimagem);
        textocasmentoimagem1=findViewById(R.id.textocasamentoimagem1);
        //dizeres convite
        mensagemconvite=findViewById(R.id.mensagemconvite);
        textoConvidar=findViewById(R.id.textoConvidar);
        nossoCasamento=findViewById(R.id.nossocasamento);
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
            fundoPrincipal=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/FundoEstiloConvite");

        }
        dadosNoivo();
        dadosNoiva();
        dadosConservatoria();
        dadosIgreja();
        dadosSalao();
        dizeresConvite();
        setFundoPrincipal();

        if (getIntent()!=null){
            idConvidado=getIntent().getStringExtra("ConvidadoId");
        }
        if (!idConvidado.isEmpty()){
            getDetalheConvidado(idConvidado);
        }

        imageQrConvidado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conviteConvidado();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        setFundoPrincipal();
    }
    private void dizeresConvite(){
        dizeres.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dizeres dizeres=dataSnapshot.getValue(Dizeres.class);
                if (dizeres!=null){
                    if (dizeres.getMensagem3()!=null){
                        mensagemconvite.setText(dizeres.getMensagem3());

                    }else {
                        mensagemconvite.setText("Frase de convite");

                    }

                    if (dizeres.getTexto1()!=null){
                        textoConvidar.setText(dizeres.getTexto1());

                    }else {
                        textoConvidar.setText("Temos a honra de convidar");
                    }
                    if (dizeres.getTexto2()!=null){
                        nossoCasamento.setText(dizeres.getTexto1());

                    }else {
                        nossoCasamento.setText("Para o nosso casamento");
                    }
                }
                else {
                    //Toast.makeText(ConviteConvidado.this, "Preencha os dados do convite", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ConviteConvidado.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
    }
    public void dadosNoivo(){
        noivo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noivo noivo=dataSnapshot.getValue(Noivo.class);
                if (noivo!=null){
                   /* nomeNoivo.setText(noivo.getNome());
                    sobrenomeNoivo.setText(noivo.getSobrenome());
                    telefoneNoivo.setText(noivo.getTelefone());
                    casaNoivo.setText(noivo.getCasa());
                    bairroNoivo.setText(noivo.getBairro());
                    emailNoivo.setText(noivo.getEmail());
                    biNoivo.setText(noivo.getBi());
                    noivoTexto.setText(noivo.toString());
                    dataNascimentoNoivo.setText(noivo.getDataNascimento());
                    idadeNoivo.setText("0");

                     if (noivo.getFoto()!=null){
                        Picasso.get().load(noivo.getFoto()).into(fotoNoivo);
                    }else {
                        Picasso.get().load(R.drawable.semfoto).into(fotoNoivo);
                    }
                    */
                    noivoConvite.setText(noivo.getNome()+" "+noivo.getSobrenome()+" & ");

                }else {
                    Toast.makeText(ConviteConvidado.this, "Dados do Noivo em falta", Toast.LENGTH_LONG).show();
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
                   /* nomeNoiva.setText(noiva.getNome());
                    sobrenomeNoiva.setText(noiva.getSobrenome());
                    telefoneNoiva.setText(noiva.getTelefone());
                    casaNoiva.setText(noiva.getCasa());
                    bairroNoiva.setText(noiva.getBairro());
                    emailNoiva.setText(noiva.getEmail());
                    biNoiva.setText(noiva.getBi());
                    dataNascimentoNoiva.setText(noiva.getDataNascimento().toString());
                    idadeNoiva.setText("0");


                     if (noiva.getFoto()!=null){
                        Picasso.get().load(noiva.getFoto()).into(fotoNoiva);
                    }else {
                        Picasso.get().load(R.drawable.semfoto).into(fotoNoiva);
                    }*/
                    noivaConvite.setText(noiva.getNome()+" "+noiva.getSobrenome());
                    //noivaTexto.setText(noiva.toString());
                }else {
                    Toast.makeText(ConviteConvidado.this, "Dados da Noiva em falta", Toast.LENGTH_LONG).show();
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
                    nomeConservatoria.setText(conservatoria.getNome());
                    dataConservatoria.setText("Dia "+conservatoria.getData());
                    horaConservatoria.setText("às "+conservatoria.getHora());
                    cerimonia_civl.setText("Cerimônia Cívil");
                }else {
                    Toast.makeText(ConviteConvidado.this, "Dados da Conservatoria em falta", Toast.LENGTH_LONG).show();
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
                    nomeIgreja.setText(igreja.getNome());
                    dataIgreja.setText("Dia "+igreja.getData());
                    horaIgreja.setText("às "+igreja.getHora());
                    cerimonia_religiosa.setText("Cerimônia Religiosa");
                }else {
                    Toast.makeText(ConviteConvidado.this, "Dados da Igreja em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void dadosSalao(){
        festa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Festa festa=dataSnapshot.getValue(Festa.class);
                if (festa!=null){
                    nomeSalao.setText(festa.getSalao());
                    //  dataIgreja.setText("Dia "+igreja.getData());
                    horaSalao.setText("às "+festa.getHora());
                    copo_agua.setText("Copo de Água");
                }else {
                    Toast.makeText(ConviteConvidado.this, "Dados do Salao em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void getDetalheConvidado(final String idConvidado) {
        mRef.child(idConvidado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Convidado convidado=dataSnapshot.getValue(Convidado.class);
                acompanhante.setText(convidado.getAcompanhante());
                emailConvidado=convidado.getEmail();
                //numeroConvidado.setText(convidado.getNumeroConvidados());
                numeroConvidado.setText(Integer.toString(convidado.getNumeroConvidados()) );
                idConvidadoConvite.setText(convidado.getNome()+" "+convidado.getSobrenome());
               qr= QRCode.from(dataSnapshot.getKey())
                       //.withColor(0x00000000, R.color.colorPrimary)
                       .bitmap();
               imageQrConvidado.setImageBitmap(qr);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    /*  Find all views Ids  */
    private void findViews() {

        rootContent = (LinearLayout) findViewById(R.id.screshoot_convite);

    }
    private void takeScreenshot(){
        Bitmap b= ScreenshotUtils.getScreenshot(rootContent);
        if (b!=null){
            File saveFile=ScreenshotUtils.getMainDirectoryName(this);



            File file=ScreenshotUtils.store(b, idConvidadoConvite.getText()+".jpg", saveFile);
            shareScreenshot(file);
        }
        else {
            //If bitmap is null show toast message
            Toast.makeText(this, "Vazio", Toast.LENGTH_SHORT).show();
        }
    }
    /*  Share Screenshot  */
    private void shareScreenshot(File file) {
        //Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing


        Uri uri = getUriForFile(getApplicationContext(), "com.fenix.wakonga", file);





        //Uri uri = FileProvider.getUriForFile(this, "com.fenix.wakonga.convidados", file);





        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        intent.putExtra(Intent.EXTRA_EMAIL, new  String[]{emailConvidado});
        intent.putExtra(Intent.EXTRA_SUBJECT, "CONVITE DE CASAMENTO");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_TEXT, mensagemconvite.getText());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            //startActivity(Intent.createChooser(intent, "Enviar Convite"));
        }



    }

/*
    public void share(){
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT,
                "ANDROID");
        share.putExtra(Intent.EXTRA_TEXT,
                "LINK OF APP ON PLAYSTORE");

        startActivity(Intent.createChooser(share, "Share link!"));}
    */

    private void conviteConvidado() {
        final  android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        builder.setTitle("Enviar Convite");
        builder.setMessage("Envir convite para "+idConvidadoConvite.getText());
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    takeScreenshot();


            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void setFundoPrincipal(){
        fundoPrincipal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FundoEstiloConvite fundoEstiloConvite=dataSnapshot.getValue(FundoEstiloConvite.class);
                if (fundoEstiloConvite!=null){
                    if (fundoEstiloConvite.getFundEstiloConvite()!=null){
                        imagemSecundaria=findViewById(R.id.imagemsecundario);
                        Picasso.get().load(fundoEstiloConvite.getFundEstiloConvite()).into(imagemSecundaria);
                       // drawable=imagemSecundaria.getDrawable();
                       // rootContent.setBackground(drawable);



                        Glide.with(getApplicationContext())
                                .load(fundoEstiloConvite.getFundEstiloConvite())
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            rootContent.setBackground(resource);
                                        }
                                    }
                                });
                    }
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
