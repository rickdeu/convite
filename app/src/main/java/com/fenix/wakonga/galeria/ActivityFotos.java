package com.fenix.wakonga.galeria;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.FotoCasamento;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ActivityFotos extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    ViewHolderFotoCasamento viewHolderFotoCasamento;
    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<FotoCasamento, ViewHolderFotoCasamento> firebaseRecyclerAdapter;
    View view;
    private ArrayList<FotoCasamento> images;

    TextView tituloFotoAddAlbum;
    ImageView imagemFotoAddAlbum;
    String pastaFotos="Album";
    String Database_Path = "Convite";

    //uri
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private View mProgressView;
    private View mLoginFormView;

    //image request code for onActivityResult
    int IMAGE_REQUEST_CODE=100;
    ProgressDialog progressDialog;
    private AlertDialog waitingDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotos);

        LayoutInflater layoutInflater=LayoutInflater.from(ActivityFotos.this);
         view=layoutInflater.inflate(R.layout.galeria_adicionar_foto_album, null);
         tituloFotoAddAlbum=(TextView)view.findViewById(R.id.id_titulo_adiconar_album);
         imagemFotoAddAlbum=(ImageView)view.findViewById(R.id.id_imagem_adionar_foto_album);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipelayout);

        images=new ArrayList<FotoCasamento>();

        mLoginFormView =findViewById(R.id.recycler_view_album);
        mProgressView = findViewById(R.id.login_progress);


        //RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getApplicationContext(), 3);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager.setReverseLayout(true);
         mLayoutManager.setStackFromEnd(true);

        mRecyclerView=findViewById(R.id.recycler_view_album);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /*waitingDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Aguarde...")
                .setCancelable(true)
                .build();*/


        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mFirebaseDatabase=FirebaseDatabase.getInstance();


        //Assign FireBaseStorage instance with root databse name
        storageReference=FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(ActivityFotos.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        //permite escolher a fot a ser carregada


        //verifica se um usuario esta logado
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){
            mRef=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/FotoCasamento");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.id_add_foto_album);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // dialogAdicionarFotoAlbum();
                Intent intent=new Intent(ActivityFotos.this, AdicionarFotosAlbum.class);
                startActivity(intent);
                finish();

            }
        });

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
                        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                    }
                },300);
            }
        });
            listarFotos();
    }
    private void listarFotos() {
        showProgress(true);
        images.clear();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<FotoCasamento, ViewHolderFotoCasamento>(
                FotoCasamento.class,
                R.layout.item_foto_album,
                ViewHolderFotoCasamento.class,
                mRef
        ) {
            @Override
            protected void populateViewHolder(final ViewHolderFotoCasamento viewHolder, FotoCasamento model, int position) {

                viewHolder.txtMenuName.setText(model.getTitle());

              //  Picasso.get().load(model.getFoto()).into(viewHolder.imageView);

                Glide.with(getApplicationContext())
                        .load(model.getFoto())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //rootContent.setBackground(resource);
                                    viewHolder.imageView.setImageDrawable(resource);

                                }
                            }
                        });


                images.add(model);

                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                viewHolder.itemView.startAnimation(animation);
                showProgress(false);
                viewHolder.setOnClickListener(new ViewHolderFotoCasamento.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       /* Bundle bundle = new Bundle();
                        bundle.putSerializable("images", images);
                        bundle.putInt("position", position);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        VisualizarImagem newFragment = VisualizarImagem.newInstance().newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");*/

                    }
                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });


            }
        };
        //showProgress(false);
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
       // showProgress(false);

    }

    protected void dialogAdicionarFotoAlbum(){
        //  Button enviarSugestao=view.findViewById(R.id.guardarDados);
        imagemFotoAddAlbum.setOnClickListener(new View.OnClickListener() {
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

        AlertDialog.Builder builder=new AlertDialog.Builder(ActivityFotos.this);
        builder.setCancelable(false);

        builder.setTitle("Clique na imagem...");
        builder.setView(view);
        builder.setCancelable(false)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        carregarDados();
                    }
                }).setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ActivityFotos.this, ActivityFotos.class);
                        startActivity(intent);
                    finish();}
                });
        AlertDialog dialog=builder.create();
        dialog.show();
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
                imagemFotoAddAlbum.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
               // selecionarFoto.setText("Imagem selecionada");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }



    private void carregarDados() {
        if (uri!=null){

            progressDialog.setTitle("A adicionar foto ao album...");
            progressDialog.show();

            String image=UUID.randomUUID().toString();

            StorageReference storageReference2nd=storageReference.child(pastaFotos+"/"+image);

            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            mAuth=FirebaseAuth.getInstance();
                            FirebaseUser user=mAuth.getCurrentUser();

                            Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete());
                            Uri uri1=uriTask.getResult();

                            FotoCasamento fotoCasamento=new FotoCasamento();
                            fotoCasamento.setTitle(tituloFotoAddAlbum.getText().toString().trim());
                            fotoCasamento.setFoto(uri1.toString().trim());

                            databaseReference.child(user.getUid()).child(fotoCasamento.toString()).push().setValue(fotoCasamento);
                            //chamar a tela de listagem depois que a img for carregada
                            Intent intent=new Intent(ActivityFotos.this, ActivityFotos.class);
                            startActivity(intent);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityFotos.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setTitle("A adicionar foto ao album...");
                        }
                    });

        }
        else {
            Toast.makeText(ActivityFotos.this, "Por favor selecione uma imagem!", Toast.LENGTH_LONG).show();
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

}
