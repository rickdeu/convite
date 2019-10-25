package com.fenix.wakonga.convidadoFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.login.LoginActivity;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.Convidado;
import com.fenix.wakonga.model.FundoEstiloConvite;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ListaConvidados extends AppCompatActivity {



    private View mProgressView;
    private View mLoginFormView;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    // AlertDialog waitingDialog;
    //View v;
    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<Convidado, ViewHolderConvidado> firebaseRecyclerAdapter;
    ProgressDialog progressDialog;
    FloatingActionButton fab;
    RelativeLayout fundo_principal;
    DatabaseReference fundoPrincipal;




    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    //i created List of int type to store id of data, you can create custom class type data according to your need.
    private List<Integer> selectedIds = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_convidados);

        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipelayout);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
       /* waitingDialog=new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage("Aguarde...")
                .setCancelable(true)
                .build();*/
        mLoginFormView =findViewById(R.id.recyclerView);
        mProgressView = findViewById(R.id.login_progress);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("A carregar");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);

        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mostrarDialogAdicionarConvidado();
                Intent intent = new Intent( getApplicationContext(), ActivityAdicionarConvidado.class);
                startActivity (intent) ;



            }
        });



        //verifica se um usuario esta logado
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){

            mRef=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Convidados");
            fundoPrincipal=mFirebaseDatabase.getInstance().getReference("Convite/"+user.getUid()+"/FundoEstiloConvite");


        }else {
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.white);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        listarConvidados();
                    }
                },300);
            }
        });
            listarConvidados();
            setFundoPrincipal();
    }



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


    public void listarConvidados(){
        showProgress(true);
        firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Convidado, ViewHolderConvidado>(
                        Convidado.class,
                        R.layout.item_convidado,
                        ViewHolderConvidado.class,
                        mRef.orderByChild("nome")
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderConvidado viewHolder, Convidado model, int position) {
                        if (model!=null){
                            // progressDialog.show();
                            viewHolder.setDetails(getApplicationContext(), model.getNome(),
                                    model.getSobrenome(), model.getFoto(), model.getTelefone(),
                                    model.getEmail(), model.getAcompanhante(), model.getNumeroConvidados());
                        }else {
                            Toast.makeText(getApplicationContext(), "Lista de Convidados Esta vazia", Toast.LENGTH_LONG).show();
                        }
                        //animation
                        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                        viewHolder.itemView.startAnimation(animation);
                        // progressDialog.hide();
                        showProgress(false);
                    }
                    @Override
                    public ViewHolderConvidado onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolderConvidado viewHolder=super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolderConvidado.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent=new Intent(view.getContext(), ConviteConvidado.class);
                                intent.putExtra("ConvidadoId", firebaseRecyclerAdapter.getRef(position).getKey());
                                startActivity(intent);
                            }
                            @Override
                            public void onItemLongClick(View view, int position) {
                                String currenteEmail=getItem(position).getTelefone();
                                String currenteImagem=getItem(position).getFoto();
                                showDeleteDataDialog(currenteEmail,currenteImagem);
                                // mostrarOpcoesConvidadosDialog();


                            }
                        });


                        return viewHolder;
                    }
                };
        //set adapter to recyclerview
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDeleteDataDialog(final String currenteEmail, final String currenteImagem) {
        String[] sortOptions={"Eliminar Convidado"};
        //final  AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("Opções Convidado");
        builder.setItems(sortOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    //inicio eliminar convidado
                    builder.setTitle("Apagar");
                    builder.setMessage("Eliminar convidado da lista?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (currenteEmail!=null){
                            Query query=mRef.orderByChild("telefone").equalTo(currenteEmail);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                                        ds.getRef().removeValue();
                                    }
                                    Toast.makeText(getApplicationContext(), "Convidado eliminado", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();


                                }
                            });}


                            if (currenteImagem!=null){



                                StorageReference eliminarImagem=getInstance().getReference(currenteImagem);
                                eliminarImagem.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Imagem apagada com sucesso...", Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });}


                        }
                    });
                    builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    //fim eliminar convidado
                }
            }

        });
        AlertDialog dialog=builder.create();
        dialog.show();



    }



    public void pesquisarConvidado(String convidado){
        String query=convidado.toLowerCase();
        Query query1=mRef.orderByChild("procurar").startAt(query).endAt(query+"\uf8ff");

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Convidado, ViewHolderConvidado>(
                Convidado.class,
                R.layout.item_convidado,
                ViewHolderConvidado.class,
                query1
        ){
            @Override
            protected void populateViewHolder(ViewHolderConvidado viewHolder, Convidado model, int position) {
                if (model!=null){
                    progressDialog.show();
                    viewHolder.setDetails(getApplicationContext(), model.getNome(),
                            model.getSobrenome(), model.getFoto(), model.getTelefone(),
                            model.getEmail(), model.getAcompanhante(), model.getNumeroConvidados());
                }else {
                    Toast.makeText(getApplicationContext(), "Lista de Convidados Esta vazia", Toast.LENGTH_LONG).show();
                }
                //animation
                Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                viewHolder.itemView.startAnimation(animation);

            }

            @Override
            public ViewHolderConvidado onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolderConvidado viewHolderConvidado=super.onCreateViewHolder(parent, viewType);
                viewHolderConvidado.setOnClickListener(new ViewHolderConvidado.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(view.getContext(), DetalheConvidado.class);
                        intent.putExtra("ConvidadoId", firebaseRecyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String currenteEmail=getItem(position).getEmail();
                        String currenteImagem=getItem(position).getFoto();
                        showDeleteDataDialog(currenteEmail,currenteImagem);
                        // mostrarOpcoesConvidadosDialog();

                    }
                });

                return viewHolderConvidado;
            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }




    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show){
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public void setFundoPrincipal(){
        fundoPrincipal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FundoEstiloConvite fundoEstiloConvite=dataSnapshot.getValue(FundoEstiloConvite.class);
                if (fundoEstiloConvite!=null){
                    if (fundoEstiloConvite.getFundEstiloConvite()!=null){
                       // imagemSecundaria=findViewById(R.id.imagemsecundario);
                        fundo_principal=findViewById(R.id.fundo_principal);
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
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
