package com.fenix.wakonga;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.cerimonias.ActivityConservatoria;
import com.fenix.wakonga.cerimonias.ActivityFesta;
import com.fenix.wakonga.cerimonias.ActivityIgreja;
import com.fenix.wakonga.convidadoFragment.ListaConvidados;
import com.fenix.wakonga.convites.DizeresConvite;
import com.fenix.wakonga.convites.EstilosConvite;
import com.fenix.wakonga.informacaoCasamento.NoivosCasamento;
import com.fenix.wakonga.login.LoginActivity;
import com.fenix.wakonga.model.FotoDeCapaModelo;
import com.fenix.wakonga.model.FundoEstiloConvite;
import com.fenix.wakonga.model.Noiva;
import com.fenix.wakonga.model.Noivo;
import com.fenix.wakonga.noivos.ActivityAdicionarNoivo;
import com.fenix.wakonga.noivos.ActivityAdicionarNoiva;
import com.fenix.wakonga.noivos.FotoCapa;
import com.fenix.wakonga.scannerFragment.ScanActivity;
import com.fenix.wakonga.galeria.ActivityFotos;
import com.fenix.wakonga.servicos.CategoriaServico;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
  private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    View viewEmail;
    TextView convidadoSelecionado;
    DatabaseReference noivo;
    DatabaseReference noiva;
    DatabaseReference fotoCapaModelo;
    DatabaseReference fundoPrincipal;
    NestedScrollView fundo_principal;
    EditText assunto, mensagem;
    LinearLayout album_fotos, listaConvidados, noivos, servicosCasamento, idEstilosConvite, id_confirmar_convidado;
     TextView nomeNoivo, nomeNoiva;
     ImageView imagemPrincipal;
    ImageView imagemSecundaria;
    Drawable drawable;
    LinearLayout nav_header;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Integer> mUserItems=new ArrayList<>();
    boolean[] checkedItems;
    int execptedResult = 0;//NUMBER_OF_RESULT; // <--- you must fetch data from firebase in some way, it depends on your implementation
    int fetchCount = 0;
   // DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("FacultyMember");


   // private TabLayout tabLayout;
    //private ViewPager viewPager;
   // private ViewPagerAdapterFragment adapter;
    String Database_Path = "Convite/";

    LayoutInflater layoutInflater;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        album_fotos =findViewById(R.id.album_fotos);
        listaConvidados=findViewById(R.id.lista_convidados);
        idEstilosConvite=findViewById(R.id.idEstilosConvite);
        id_confirmar_convidado=findViewById(R.id.id_confirmar_convidado);

        nomeNoiva=findViewById(R.id.noiva);
        nomeNoivo=findViewById(R.id.noivo);



        noivos=findViewById(R.id.id_noivos);
        servicosCasamento=findViewById(R.id.id_servicos_casamento);
        collapsingToolbarLayout=findViewById(R.id.colappsingtoolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        collapsingToolbarLayout.setTitle(nomeNoivo.getText() +" "+nomeNoiva.getText());


        // Assign FirebaseDatabase instance with root database name.
       // mRef=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Convidados");


       /* tabLayout=(TabLayout)findViewById(R.id.tablaout_id);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);
        adapter=new ViewPagerAdapterFragment(getSupportFragmentManager());

        //Add Fragment Here
        adapter.addFragment(new ConvidadosFragment(), "");
        adapter.addFragment(new ScannerFragment(), "");
        adapter.addFragment(new InfoFragment(), "");
        adapter.addFragment(new ServicosFragment(), "");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_people_fragment);
        tabLayout.getTabAt(1).setIcon(R.drawable.ver_convidados);
        tabLayout.getTabAt(2).setIcon(R.drawable.info);
        tabLayout.getTabAt(3).setIcon(R.drawable.servicos);*/
        //verifica se um usuario esta logado

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        if (user!=null){
            if (!user.isEmailVerified()){
                //Toast.makeText(getApplicationContext(), "Benvindo(a) "+user.getDisplayName(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

            // mRef=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Convidados");
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path+user.getUid()+"/Convidados");
            noivo=FirebaseDatabase.getInstance().getReference(Database_Path+user.getUid()+"/Noivo");
            noiva=FirebaseDatabase.getInstance().getReference(Database_Path+user.getUid()+"/Noiva");

          //  fotoCapaModelo=FirebaseDatabase.getInstance().getReference(Database_Path+user.getUid()+"/FotoDeCapaModelo");
            fotoCapaModelo=FirebaseDatabase
                    .getInstance()
                    .getReference().child(Database_Path).child(user.getUid()).child("FotoDeCapaModelo");
            fundoPrincipal=FirebaseDatabase.getInstance().getReference(Database_Path+user.getUid()+"/FundoEstiloConvite");

            fotoDeCapa();
            setFundoPrincipal();
            dadosNoivo();
            dadosNoiva();


            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            //Obtém a referência dos TextViews a partir do NavigationView
            CircleImageView imageView=(CircleImageView)headerView.findViewById(R.id.imageLogin);
            TextView emal = (TextView) headerView.findViewById(R.id.nomeLogin);
            TextView nome = (TextView) headerView.findViewById(R.id.emailLogin);
            nav_header=headerView.findViewById(R.id.id_nav);

            Uri photo=user.getPhotoUrl();
            nome.setText(user.getDisplayName());
            emal.setText(user.getEmail());

            if (photo!=null){
                Picasso
                        .get()
                        .load(photo.toString().trim())
                        .error(R.drawable.logoeditado)
                        .into(imageView);
            }else {
                Picasso.get()
                        .load(R.drawable.logoeditado)
                        .into(imageView);
            }
        }else {
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //mostrarDialogAdicionarConvidado();
                Intent intent = new Intent( MainActivity.this , ActivityAdicionarConvidado.class);
                startActivity (intent) ;
            }
        });*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        album_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this , ActivityFotos.class);
                startActivity ( intent ) ;
            }
        });
        listaConvidados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this , ListaConvidados.class);
                startActivity ( intent ) ;
            }
        });
        noivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this , NoivosCasamento.class);
                startActivity ( intent ) ;
            }
        });
        servicosCasamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this , CategoriaServico.class);
                startActivity ( intent ) ;
            }
        });

        idEstilosConvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this , EstilosConvite.class);
                startActivity ( intent ) ;
            }
        });
        id_confirmar_convidado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this , ScanActivity.class);
                startActivity ( intent ) ;
            }
        });
        /*dadosNoiva();
        dadosNoivo();*/

    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        setFundoPrincipal();
    }*/
    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.id_opcoes) {
            mostrarOpcoesCasamentoDialog();
        }
       if(id==R.id.scan_convidado){
           Intent intent = new Intent( MainActivity.this , ScanActivity.class);
           startActivity ( intent ) ;
       }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.id_home) {
            Intent intent = new Intent( MainActivity.this , MainActivity.class);
            startActivity ( intent ) ;
        }
        if (id == R.id.estilo_convite) {
            Intent intent = new Intent( MainActivity.this , EstilosConvite.class);
            startActivity ( intent ) ;
        }

        else if (id == R.id.editar_foto_capa) {
            Intent intent = new Intent( MainActivity.this , FotoCapa.class);
            startActivity ( intent ) ;

            }else if (id == R.id.textoConvite) {
            Intent intent = new Intent( MainActivity.this , DizeresConvite.class);
            startActivity ( intent ) ;
        } else if (id == R.id.id_dados_noivo) {
            Intent intent = new Intent( MainActivity.this , ActivityAdicionarNoivo.class);
            startActivity ( intent ) ;
        }
        else if (id == R.id.id_dados_noiva) {
            Intent intent = new Intent( MainActivity.this , ActivityAdicionarNoiva.class);
            startActivity ( intent ) ;
        }
        else if (id == R.id.id_cerimonia_civil) {
            Intent intent = new Intent( MainActivity.this , ActivityConservatoria.class);
            startActivity ( intent ) ;

        }
        else if (id == R.id.id_cerimonia_religiosa) {
            Intent intent = new Intent( MainActivity.this , ActivityIgreja.class);
            startActivity ( intent ) ;

        }

        else if (id == R.id.id_copo_agua) {
            Intent intent = new Intent( MainActivity.this , ActivityFesta.class);
            startActivity ( intent ) ;
        }

        else if (id==R.id.nav_sair){
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);

/*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;*/
    }



   /* //operacao para envio de email
    protected void mostrarDialogEnviarMensagem(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,
                android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    fetchCount++;
                    final String email = ds.child("email").getValue().toString();
                    list.add(email);
                    checkedItems=new boolean[list.size()];
                        builder.setTitle("Convidados");
                        builder.setIcon(R.drawable.logoconvite);
                        builder.setMultiChoiceItems( list.toArray(new String[list.size()]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if (isChecked){
                                    if (!mUserItems.contains(position)){
                                        mUserItems.add(position);
                                    }
                                }else
                                if (mUserItems.contains(position)){
                                    mUserItems.remove(position);
                                }
                            }
                        });
                }
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String item="";
                            for (int i=0; i<mUserItems.size(); i++){
                                item=item+list.get(mUserItems.get(i));
                                if (i!=mUserItems.size()-1){
                                    item=item+", ";
                                }
                            }
                            convidadoSelecionado.setText(item);
                            enviarEmail();
                            //dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i=0; i<checkedItems.length; i++){
                                checkedItems[i]=false;
                                mUserItems.clear();
                                convidadoSelecionado.setText(getString(R.string.empty));
                            }
                        }
                    });




                        AlertDialog dialog=builder.create();
                        dialog.show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Sem dados disponiveis...",Toast.LENGTH_LONG).show();
            }
        });




    }
*//*
    protected void enviarEmail(){
        View viewEmail=LayoutInflater.from(this)
                .inflate(R.layout.enviar_email,null);
        convidadoSelecionado=viewEmail.findViewById(R.id.emailSelecionado);
        assunto=viewEmail.findViewById(R.id.assuntoSelecionado);
        mensagem=viewEmail.findViewById(R.id.emailMensagem);

        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,
                android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        builder.setTitle("Emails Selecionados");
        builder.setView(viewEmail);
        builder.setCancelable(false)
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // convidadoSelecionado.setText(email.getText().toString());
                        String destinatarioEmail[]={convidadoSelecionado.getText().toString()};
                        String assuntoEmail=assunto.getText().toString();
                        String mensagemEmail=mensagem.getText().toString();

                        new Mail(MainActivity.this, destinatarioEmail, assuntoEmail, mensagemEmail).execute();//cal send
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    */

    private void mostrarOpcoesCasamentoDialog(){
        String[] sortOptions={"Noivo", "Noiva", "Cerimônia Religiosa", "Cerimônia Cívil", "Copo D'Água"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("DADOS DO CASAMENTO")
                //.setIcon(R.drawable.logoconvite)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            Intent intent = new Intent( MainActivity.this , ActivityAdicionarNoivo.class);
                            startActivity ( intent ) ;
                        }
                        else if (which==1){
                            Intent intent = new Intent( MainActivity.this , ActivityAdicionarNoiva.class);
                            startActivity ( intent ) ;
                        }
                        else if (which==2){
                            Intent intent = new Intent( MainActivity.this , ActivityIgreja.class);
                            startActivity ( intent ) ;
                        }
                        else if (which==3){
                            Intent intent = new Intent( MainActivity.this , ActivityConservatoria.class);
                            startActivity ( intent ) ;
                        }
                        else if (which==4){
                            Intent intent = new Intent( MainActivity.this , ActivityFesta.class);
                            startActivity ( intent ) ;
                        }

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
  /*  //verifica se tem conexao a internet
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
*/

    public void dadosNoivo(){
        noivo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noivo noivo=dataSnapshot.getValue(Noivo.class);
                if (noivo!=null){
                    nomeNoivo.setText(noivo.getSobrenome()+" &");

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
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void fotoDeCapa(){

        fotoCapaModelo.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                FotoDeCapaModelo fotoCapaModelo=dataSnapshot.getValue(FotoDeCapaModelo.class);
                if (fotoCapaModelo!=null){
                    imagemPrincipal=findViewById(R.id.imagemprincipal);
                   // Picasso.get().load(fotoCapaModelo.getFoto()).into(imagemPrincipal);
                    Glide.with(getApplicationContext())
                            .load(fotoCapaModelo.getFoto())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        //rootContent.setBackground(resource);
                                        imagemPrincipal.setImageDrawable(resource);

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
                        //drawable=imagemSecundaria.getDrawable();
                       // fundo_principal.setBackground(imagemSecundaria.getDrawable());
                        Glide.with(getApplicationContext())
                                .load(fundoEstiloConvite.getFundEstiloConvite())
                                .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    fundo_principal.setBackground(resource);
                                    nav_header.setBackground(resource);
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
