package com.fenix.wakonga.servicos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.Servico;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListaServicos extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Servico, ViewHolderFood> adapter;
    RelativeLayout relativeLayoutServicos;

    FirebaseDatabase database;
    DatabaseReference listaServico;
    String categoriaId="";
    private View mProgressView;
    private View mLoginFormView;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
        //Firebase
        database=FirebaseDatabase.getInstance();
        listaServico=database.getReference("Servico");
        relativeLayoutServicos=findViewById(R.id.id_servicos_casamento);
        recyclerView=findViewById(R.id.recyler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mLoginFormView =findViewById(R.id.recyler_food);
        mProgressView = findViewById(R.id.login_progress);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(this);
        // progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        
        if (getIntent()!=null){
            categoriaId=getIntent().getStringExtra("categoriaId");
        }
        if (!categoriaId.isEmpty()&&categoriaId!=null){
            loadListService(categoriaId);
        }
    }

    private void loadListService(String categoriaId) {
        showProgress(true);
        progressDialog.show();
        adapter=new FirebaseRecyclerAdapter<Servico, ViewHolderFood>(
                Servico.class,
                R.layout.categoria_item,
                ViewHolderFood.class,
                listaServico.orderByChild("menuId").equalTo(categoriaId)
        ) {
            @Override
            protected void populateViewHolder(final  ViewHolderFood viewHolder, Servico model, int position) {

                if (model!=null) {
                    viewHolder.food_name.setText(model.getNome());
             /*   Picasso.get()
                        .load(model.getImagem())
                        .into(viewHolder.food_image);

                */

                    Glide.with(getApplicationContext())
                            .load(model.getImagem())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        //rootContent.setBackground(resource);
                                        viewHolder.food_image.setImageDrawable(resource);
                                    }
                                }
                            });
                }else {
                    Glide.with(getApplicationContext())
                            .load(R.drawable.logoeditado)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        //rootContent.setBackground(resource);
                                        relativeLayoutServicos.setBackground(resource);
                                    }
                                }
                            });
                }
                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                viewHolder.itemView.startAnimation(animation);
                progressDialog.hide();

                viewHolder.setOnClickListener(new ViewHolderFood.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(ListaServicos.this, DetalheServico.class);
                        intent.putExtra("servicoId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        showProgress(false);

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
