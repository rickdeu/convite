package com.fenix.wakonga.informacaoCasamento;

import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.fenix.wakonga.model.Dizeres;
import com.fenix.wakonga.model.Festa;
import com.fenix.wakonga.model.FundoEstiloConvite;
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
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

public class NoivosCasamento extends AppCompatActivity {

    TextView nomeNoivo, sobrenomeNoivo,dataNascimentoNoivo, telefoneNoivo, casaNoivo,bairroNoivo, emailNoivo, biNoivo, noivoTexto;
    ImageView fotoNoivo, fraseConvite, textocasamentoimagem, textocasmentoimagem1;
    TextView nomeNoiva, sobrenomeNoiva,dataNascimentoNoiva, telefoneNoiva, casaNoiva,bairroNoiva, emailNoiva, biNoiva, noivaTexto;
    TextView nomeConservatoria, dataConservatoria, horaConservatoria;
    TextView nomeIgreja, dataIgreja, horaIgreja;
    TextView nomeSalao, dataSalao, horaSalao;
    TextView cerimonia_civl, cerimonia_religiosa,copo_agua;
    TextView mensagemconvite, textoConvidar, nossoCasamento;
    ImageView imagemPrincipal;
    ImageView imagemSecundaria;
    Drawable drawable;
    LinearLayout fundo_principal;


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
    DatabaseReference fundoPrincipal;




    private FirebaseAuth mAuth;
    private FoldingCell fc,fc2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noivos_casamento);

        //Dados do noivo
        nomeNoivo=findViewById(R.id.nomeNoivo);
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
        nomeNoiva=findViewById(R.id.nomeNoiva);
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
            noivo=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Noivo");
            noiva=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Noiva");
            civil=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Conservatoria");
            igreja=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Igreja");
            festa=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Festa");
            dizeres=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Dizeres");
            fundoPrincipal=mFirebaseDatabase.getInstance().getReference("Convite/"+user.getUid()+"/FundoEstiloConvite");
        }

        dadosNoivo();
        dadosNoiva();
        dadosConservatoria();
        dadosIgreja();
        dadosSalao();
        dizeresConvite();
        setFundoPrincipal();
        fc=(FoldingCell)findViewById(R.id.folding_cell);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fc.toggle(false);
                    fc.fold(true);
                    fc.unfold(false);
                    fc.animate();
            }
        });
        fc2=(FoldingCell)findViewById(R.id.folding_cell2);
        fc2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    fc2.toggle(false);
                    fc2.fold(true);
                    fc2.unfold(false);
                    fc2.animate();
            }
        });
      /*  fraseConvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fraseConviteInserir();
            }
        });
        textocasamentoimagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInserirTextoCasamento();
            }
        });
        textocasmentoimagem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInserirTextoNossoCasamento();
            }
        });
*/
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
                    //Toast.makeText(NoivosCasamento.this, "Preencha os dados do convite", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    /*
        private void fraseConviteInserir(){
            //  viewFraseconvite=inflater.inflate(R.layout.fraseconvite, container, false);
            View viewFraseconvite= LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.fraseconvite,null);
            inserirFrase=viewFraseconvite.findViewById(R.id.textoConvite);
            final AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
            builder.setView(viewFraseconvite);
            builder.setTitle("Editar Frase Convite");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dizeres dizer=new Dizeres();
                    dizer.setMensagem(inserirFrase.getText().toString());
                    dizeres.child(dizer.toString()).setValue(dizer);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
        private void setInserirTextoCasamento() {
            View textoConvite=LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.textocasamento,null);
            inserirTextoCasamento=textoConvite.findViewById(R.id.textocasamento);
            final AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
            builder.setView(textoConvite);
            builder.setTitle("Editar Frase Convite");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dizeres dizer=new Dizeres();
                    dizer.setConvidar(inserirTextoCasamento.getText().toString());
                    dizeres.child(dizer.toString()).setValue(dizer);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
       private void setInserirTextoNossoCasamento() {
            View textoConvite=LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.nossocasamento,null);
            inserirTextoNossoCasamento=textoConvite.findViewById(R.id.nossocasamento);
            final AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
            builder.setView(textoConvite);
            builder.setTitle("Editar Frase Convite");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dizeres dizer=new Dizeres();
                    dizer.setHonra(inserirTextoNossoCasamento.getText().toString());
                    dizeres.child(dizer.toString()).setValue(dizer);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();

        }
        */
    public void dadosNoivo(){
        noivo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noivo noivo=dataSnapshot.getValue(Noivo.class);
                if (noivo!=null){
                    nomeNoivo.setText(noivo.getNome());
                    sobrenomeNoivo.setText(noivo.getSobrenome());
                    telefoneNoivo.setText(noivo.getTelefone());
                    casaNoivo.setText(noivo.getCasa());
                    bairroNoivo.setText(noivo.getBairro());
                    emailNoivo.setText(noivo.getEmail());
                    biNoivo.setText(noivo.getBi());
                    noivoTexto.setText(noivo.toString());
                    dataNascimentoNoivo.setText(noivo.getDataNascimento());
                    noivoConvite.setText(noivo.getNome()+" "+noivo.getSobrenome()+" & ");
                    if (noivo.getFoto()!=null){
                       // Picasso.get().load(noivo.getFoto()).into(fotoNoivo);

                        Glide.with(getApplicationContext())
                                .load(noivo.getFoto())
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            fotoNoivo.setImageDrawable(resource);
                                        }
                                    }
                                });
                    }else {
                        Picasso.get().load(R.drawable.logoeditado).into(fotoNoivo);
                    }
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
                    nomeNoiva.setText(noiva.getNome());
                    sobrenomeNoiva.setText(noiva.getSobrenome());
                    telefoneNoiva.setText(noiva.getTelefone());
                    casaNoiva.setText(noiva.getCasa());
                    bairroNoiva.setText(noiva.getBairro());
                    emailNoiva.setText(noiva.getEmail());
                    biNoiva.setText(noiva.getBi());
                    dataNascimentoNoiva.setText(noiva.getDataNascimento().toString());
                    noivaConvite.setText(noiva.getNome()+" "+noiva.getSobrenome());
                    noivaTexto.setText(noiva.toString());
                    if (noiva.getFoto()!=null){
                        //Picasso.get().load(noiva.getFoto()).into(fotoNoiva);

                        Glide.with(getApplicationContext())
                                .load(noiva.getFoto())
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            fotoNoiva.setImageDrawable(resource);
                                        }
                                    }
                                });

                    }else {
                        Picasso.get().load(R.drawable.logoeditado).into(fotoNoiva);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Dados da Noiva em falta", Toast.LENGTH_LONG).show();
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
                    nomeIgreja.setText(igreja.getNome());
                    dataIgreja.setText("Dia "+igreja.getData());
                    horaIgreja.setText("às "+igreja.getHora());
                    cerimonia_religiosa.setText("Cerimônia Religiosa");
                }else {
                    Toast.makeText(getApplicationContext(), "Dados da Igreja em falta", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Dados do Salao em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void setFundoPrincipal(){
        fundoPrincipal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FundoEstiloConvite fundoEstiloConvite=dataSnapshot.getValue(FundoEstiloConvite.class);
                if (fundoEstiloConvite!=null){
                    if (fundoEstiloConvite.getFundEstiloConvite()!=null){
                        imagemSecundaria=findViewById(R.id.imagemsecundario);
                        fundo_principal=findViewById(R.id.fundo_principal);
                        Picasso.get().load(fundoEstiloConvite.getFundEstiloConvite()).into(imagemSecundaria);
                      //  drawable=imagemSecundaria.getDrawable();
                      //  fundo_principal.setBackground(drawable);


                        Glide.with(getApplicationContext())
                                .load(fundoEstiloConvite.getFundEstiloConvite())
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            fundo_principal.setBackground(resource);
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


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.activar_edicao){
            LayoutInflater  editar=LayoutInflater.from(getContext());
            View activarEdicao=editar.inflate(R.layout.informacao_fragment_casal, null);
            ImageView editar1, editar2,editar3,editar4;

            editar1=(ImageView)activarEdicao.findViewById(R.id.editar1);
            editar2=(ImageView)activarEdicao.findViewById(R.id.editar2);
            editar3=(ImageView)activarEdicao.findViewById(R.id.editar3);
            editar4=(ImageView)activarEdicao.findViewById(R.id.editar4);

            editar1.setEnabled(true);
            editar2.setEnabled(true);
            editar3.setEnabled(true);
            editar4.setEnabled(true);

            editar1.setVisibility(View.VISIBLE);
            editar2.setVisibility(View.VISIBLE);
            editar3.setVisibility(View.VISIBLE);
            editar4.setVisibility(View.VISIBLE);

        }


        return super.onOptionsItemSelected(item);
    }*/

    //verifica se tem conexao a internet
   /* private boolean haveNetworkConnection(){
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
    }*/
}
