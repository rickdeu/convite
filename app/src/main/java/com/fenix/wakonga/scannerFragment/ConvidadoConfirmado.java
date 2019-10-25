package com.fenix.wakonga.scannerFragment;


import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;

import com.fenix.wakonga.model.Convidado;

import com.fenix.wakonga.model.FundoEstiloConvite;
import com.fenix.wakonga.model.OcorrenciaConvidado;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ConvidadoConfirmado extends AppCompatActivity {
    TextView nome, sobrenome, telefone, email, acompanhante,quantidade;
    ImageView foto;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private FirebaseAuth mAuth;
    String idConvidado="";
    Button convidado_confirmado;
    DatabaseReference mRefConvidadoOcorrencia;
    DatabaseReference mRefConvidadoOcorrenciaLer;
    RecyclerView recycler_ocorrencia;
    FirebaseRecyclerAdapter<OcorrenciaConvidado, OcorrenciaConvidadoViweHolder> adapter;
    DatabaseReference fundoPrincipal;
    ImageView imagemSecundaria;
    Drawable drawable;
    String Database_Path = "Convite";
    private LinearLayout fundo_principalConvidado;
    int  contarelemntos=0;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_convidado_confirmado);
        nome=findViewById(R.id.nome_detalhe_convidado);
        foto=findViewById(R.id.img_detalhe_convidado);
        sobrenome=findViewById(R.id.sobrenome_detalhe_convidado);
        telefone=findViewById(R.id.telefone_detalhe_convidado);
        email=findViewById(R.id.email_detalhe_convidado);
        acompanhante=findViewById(R.id.acompanhante_detalhe_convidado);
        quantidade=(TextView)findViewById(R.id.numero_convidados_detalhe);
        convidado_confirmado=findViewById(R.id.convidado_confirmado);
        fundo_principalConvidado = (LinearLayout) findViewById(R.id.fundo_principalConvidado);

        //Ocorrencias convidados
        recycler_ocorrencia=findViewById(R.id.recyclerViewocorrencia);
        recycler_ocorrencia.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getApplicationContext(), 1);
        recycler_ocorrencia.setLayoutManager(mLayoutManager);


        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        //verifica se um usuario esta logado
        mAuth=FirebaseAuth.getInstance();
         user=mAuth.getCurrentUser();
        if (user!=null){
            mRef=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Convidados");
            fundoPrincipal=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/FundoEstiloConvite");
           mRefConvidadoOcorrenciaLer=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/OcorrenciaConvidado");
            mRefConvidadoOcorrencia = FirebaseDatabase.getInstance().getReference(Database_Path);




            // mRefConvidadoOcorrenciaLer=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Convidados/"+idConvidado);


        }
        if (getIntent()!=null){
            idConvidado=getIntent().getStringExtra("ConvidadoId");
        }else {
            Intent intent=new Intent(ConvidadoConfirmado.this, ConvidadoIntruso.class);
            startActivity(intent);
            finish();
        }
        if (!idConvidado.isEmpty()){
            getDetalheConvidado(idConvidado);
        }
        convidado_confirmado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convidadoConfirmado();
            }
        });
setFundoPrincipal();
    }
    private void convidadoConfirmado() {
        String timeStamp=new SimpleDateFormat("HH:mm:ss_dd-MM-yyy",
                Locale.getDefault()).format(System.currentTimeMillis());
        OcorrenciaConvidado ocorrenciaConvidado=new OcorrenciaConvidado();
        ocorrenciaConvidado.setOcorrencia(timeStamp);
        ocorrenciaConvidado.setIdConvidado(idConvidado);
        //mRefConvidadoOcorrencia.child(ocorrenciaConvidado.toString()).push().setValue(ocorrenciaConvidado);
        mRefConvidadoOcorrencia.child(user.getUid()).child(ocorrenciaConvidado.toString()).push().setValue(ocorrenciaConvidado);


        Intent intent = new Intent(ConvidadoConfirmado.this, ScanActivity.class);
        startActivity(intent);
        finish();
    }
    private void getDetalheConvidado(final String idConvidado) {
        mRef.child(idConvidado).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Convidado convidado=dataSnapshot.getValue(Convidado.class);
                if (convidado!=null){
                nome.setText(convidado.getNome());
                sobrenome.setText(convidado.getSobrenome());
                telefone.setText(convidado.getTelefone());
                acompanhante.setText(convidado.getAcompanhante());
                email.setText(convidado.getEmail());
                quantidade.setText(Integer.toString(convidado.getNumeroConvidados()) );
                if (convidado.getFoto()!=null){
                 //   Picasso.get().load(convidado.getFoto()).into(foto);
                    Glide.with(getApplicationContext())
                            .load(convidado.getFoto())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        //rootContent.setBackground(resource);
                                        foto.setImageDrawable(resource);

                                    }
                                }
                            });
                }else {
                    Picasso.get().load(R.drawable.logoeditado).into(foto);
                }
                //Tratamento das ocorrencias dos convidados
                    loadMenu();

                }
                else {
                    Intent intent=new Intent(ConvidadoConfirmado.this, ConvidadoIntruso.class);
                    startActivity(intent);
                    finish();
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
                        //imagemSecundaria=findViewById(R.id.imagemsecundario);
                       // Picasso.get().load(fundoEstiloConvite.getFundEstiloConvite()).into(imagemSecundaria);
                        //drawable=imagemSecundaria.getDrawable();
                      //  fundo_principalConvidado.setBackground(drawable);

                        Glide.with(getApplicationContext())
                                .load(fundoEstiloConvite.getFundEstiloConvite())
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            //rootContent.setBackground(resource);
                                            fundo_principalConvidado.setBackground(resource);
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
//Metodo ocorrencia convidados
private void loadMenu() {
    adapter=new FirebaseRecyclerAdapter<OcorrenciaConvidado, OcorrenciaConvidadoViweHolder>(
            OcorrenciaConvidado.class,
            R.layout.item_ocorrencia,
            OcorrenciaConvidadoViweHolder.class,
            mRefConvidadoOcorrenciaLer.orderByChild("idConvidado").equalTo(idConvidado)
    ) {
        @Override
        protected void populateViewHolder(final OcorrenciaConvidadoViweHolder viewHolder, OcorrenciaConvidado model, int position) {
          /*  if (position==0){
                Glide.with(getApplicationContext())
                        .load(R.drawable.entrou)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //rootContent.setBackground(resource);
                                    viewHolder.imageView.setImageDrawable(resource);
                                }
                            }
                        });
            }
            if (position==1){
                Glide.with(getApplicationContext())
                        .load(R.drawable.saiu)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //rootContent.setBackground(resource);
                                    viewHolder.imageView.setImageDrawable(resource);
                                }
                            }
                        });
            }*/
            if (position%2==0){
                Glide.with(getApplicationContext())
                        .load(R.drawable.entrou)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //rootContent.setBackground(resource);
                                    viewHolder.imageView.setImageDrawable(resource);
                                }
                            }
                        });
            }else {
                Glide.with(getApplicationContext())
                        .load(R.drawable.saiu)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //rootContent.setBackground(resource);
                                    viewHolder.imageView.setImageDrawable(resource);
                                }
                            }
                        });
            }
            viewHolder.txtMenuName.setText(model.getOcorrencia());
            Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            viewHolder.itemView.startAnimation(animation);
            viewHolder.setOnClickListener(new OcorrenciaConvidadoViweHolder.ClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                }
                @Override
                public void onItemLongClick(View view, int position) {
                }
            });
        }
    };
    recycler_ocorrencia.setAdapter(adapter);
}


}
