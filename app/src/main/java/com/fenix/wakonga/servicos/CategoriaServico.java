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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.ServicoCategoria;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoriaServico extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference categoria;
    RecyclerView recycler_menu;
     FirebaseRecyclerAdapter<ServicoCategoria, MenuViewHolder> adapter;
    private ProgressDialog progressDialog;
    private View mProgressView;
    private View mLoginFormView;
    //private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_servico);
        //aqui sera colocado a categoria dos servicos prestados
        mLoginFormView =findViewById(R.id.recyler_menu);
        mProgressView = findViewById(R.id.login_progress);


        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(CategoriaServico.this);
       // progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);

        //mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
       // mLayoutManager.setReverseLayout(true);
       // mLayoutManager.setStackFromEnd(true);



        recycler_menu=findViewById(R.id.recyler_menu);
        recycler_menu.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager=new  GridLayoutManager(getApplicationContext(), 1);
        recycler_menu.setLayoutManager(mLayoutManager);

        database=FirebaseDatabase.getInstance();
        categoria=database.getReference("ServicoCategoria");
        loadMenu();
    }
    private void loadMenu() {
        showProgress(true);
        progressDialog.show();
        adapter=new FirebaseRecyclerAdapter<ServicoCategoria, MenuViewHolder>(
                ServicoCategoria.class,
                R.layout.servico_item,
                MenuViewHolder.class,
                categoria
        ) {
            @Override
            protected void populateViewHolder(final MenuViewHolder viewHolder, ServicoCategoria model, int position) {
                viewHolder.txtMenuName.setText(model.getNome());
              /*  Picasso.get()
                        .load(model.getImagem())
                        .into(viewHolder.imageView);
*/
                Glide.with(getApplicationContext())
                        .load(model.getImagem())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //rootContent.setBackground(resource);
                                    viewHolder.imageView.setImageDrawable(resource);
                                }
                            }
                        });
                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                viewHolder.itemView.startAnimation(animation);
                showProgress(false);
                progressDialog.hide();
                viewHolder.setOnClickListener(new MenuViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(CategoriaServico.this, ListaServicos.class);
                        intent.putExtra("categoriaId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

            }
        };
        recycler_menu.setAdapter(adapter);
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
