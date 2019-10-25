package com.fenix.wakonga.servicos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.FundoEstiloConvite;
import com.fenix.wakonga.model.ImageModelSlideServicos;
import com.fenix.wakonga.model.Servico;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

public class DetalheServico extends AppCompatActivity {
    TextView servico_nome, servico_preco, servico_desc, telefone, email, provincia, municipio;
    //ImageView servico_imagem;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    String servidoId="";
    private static final int WRITE_EXTERNAL_STORAGE_CODE=1;
    private static final  int REQUEST_CALL=1;
    SlidingImage_Adapter mAdapter;

    FirebaseDatabase database;
    DatabaseReference estilosRef;
    DatabaseReference listaServico;
    RecyclerView recycler_menu;
    private LinearLayoutManager mLayoutManager;


    private  ViewPager mPager;
    CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    FirebaseRecyclerAdapter<ImageModelSlideServicos, SlidingImage_Adapter> adapter;
    CoordinatorLayout fundo_principal;
    DatabaseReference fundoPrincipal;
    private FirebaseAuth mAuth;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_servico);

        recycler_menu=findViewById(R.id.pager);



        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
         mLayoutManager.setReverseLayout(true);
         mLayoutManager.setStackFromEnd(true);

        recycler_menu.setHasFixedSize(true);
       // RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getApplicationContext(), 1);
        recycler_menu.setLayoutManager(mLayoutManager);

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){

            database=FirebaseDatabase.getInstance();
            fundoPrincipal=database.getInstance().getReference("Convite/"+user.getUid()+"/FundoEstiloConvite");
            listaServico=database.getInstance().getReference("Servico");
            estilosRef= database.getInstance().getReference("ImageModelSlideServicos");
            setFundoPrincipal();
        }else {
            setFundoPrincipal();
        }
        btnCart=findViewById(R.id.btncart);
        servico_desc=findViewById(R.id.servico_desc);
        servico_nome=findViewById(R.id.nome_servico);
        servico_preco=findViewById(R.id.preco_servico);
        telefone=findViewById(R.id.telefone_servico);
        email=findViewById(R.id.emailservico);
        provincia=findViewById(R.id.provinciaservico);
        municipio=findViewById(R.id.municipioservico);
        //servico_imagem=findViewById(R.id.imagem_servico);
        collapsingToolbarLayout=findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

      //  imageModelArrayList = populateList();

        if (getIntent()!=null){
            servidoId=getIntent().getStringExtra("servicoId");
        }
        if (!servidoId.isEmpty()){
            getDetalheServico(servidoId);
            init(servidoId);
        }
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liparParaServico();
            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareScreenshot();
            }
        });

    }
    private void getDetalheServico(final String servidoId) {
        listaServico.child(servidoId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Servico servico=dataSnapshot.getValue(Servico.class);

                //Picasso.get().load(servico.getImagem()).into(servico_imagem);

               /* Glide.with(getApplicationContext())
                        .load(servico.getImagem())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //rootContent.setBackground(resource);
                                    servico_imagem.setImageDrawable(resource);
                                }
                            }
                        });*/


                collapsingToolbarLayout.setTitle(servico.getNome());
                servico_preco.setText(servico.getPreco());
                servico_nome.setText(servico.getNome());
                servico_desc.setText(servico.getDescricao());
                telefone.setText(servico.getTelefone());
                email.setText(servico.getEmail());
                provincia.setText(servico.getProvincia());
                municipio.setText(servico.getMunicipio());
                ActionBar actionBar=getSupportActionBar();
                actionBar.setTitle(servico_nome.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void shareScreenshot() {



        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        //intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL,  email.getText());
        intent.putExtra(Intent.EXTRA_SUBJECT, "SERVIÇO DE CASAMENTO");
        //intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            //startActivity(Intent.createChooser(intent, "Enviar email"));
        }

    }


    private void liparParaServico() {
        String nomeDialog=servico_nome.getText().toString();
        final  android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        builder.setTitle("Entre em contacto");
        builder.setMessage("Pretende ligar para  "+nomeDialog+" ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ligarServico();
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
    private void ligarServico(){
        String numero=telefone.getText().toString();
        if (numero.trim().length()>0){
            if (ContextCompat.checkSelfPermission(DetalheServico.this,
                    Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(DetalheServico.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else{
                String dial="tel: "+numero;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else {
            Toast.makeText(DetalheServico.this, "Numero de telefone em falta!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CALL){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                ligarServico();
            }else {
                Toast.makeText(this, "Permissao Negada", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Slide show
  /* private ArrayList<ImageModelSlideServicos> populateList(){

        final ArrayList<ImageModelSlideServicos> list = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            ImageModelSlideServicos imageModel = new ImageModelSlideServicos();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }



        return list;
    }*/

    private void init(String servidoId) {
        adapter=new FirebaseRecyclerAdapter<ImageModelSlideServicos, SlidingImage_Adapter>(
                ImageModelSlideServicos.class,
                R.layout.slidingimages_layout,
                SlidingImage_Adapter.class,
                estilosRef.orderByChild("idServico").equalTo(servidoId)
        ) {
            @Override
            protected void populateViewHolder(final SlidingImage_Adapter viewHolder, ImageModelSlideServicos model, int position) {
                if (!model.equals("")){
                    Glide.with(getApplicationContext())
                            .load(model.getImage_drawable())
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
                    Picasso.get()
                            .load(R.drawable.logoeditado)
                            .into(viewHolder.imageView);
                }


                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                viewHolder.itemView.startAnimation(animation);
                viewHolder.setOnClickListener(new SlidingImage_Adapter.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);



    }
    public void setFundoPrincipal(){
        fundoPrincipal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FundoEstiloConvite fundoEstiloConvite=dataSnapshot.getValue(FundoEstiloConvite.class);
                fundo_principal=findViewById(R.id.fundo_principal);

                if (fundoEstiloConvite!=null){
                    if (fundoEstiloConvite.getFundEstiloConvite()!=null){
                        // imagemSecundaria=findViewById(R.id.imagemsecundario);
                        // Picasso.get().load(fundoEstiloConvite.getFundEstiloConvite()).into(imagemSecundaria);
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
                }else {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.intro3)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        fundo_principal.setBackground(resource);
                                    }
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



}
