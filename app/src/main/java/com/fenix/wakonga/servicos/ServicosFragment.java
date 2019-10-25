package com.fenix.wakonga.servicos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fenix.wakonga.R;
import com.fenix.wakonga.model.ServicoCategoria;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ServicosFragment extends Fragment {
   View v;
    FirebaseDatabase database;
    DatabaseReference categoria;
    RecyclerView recycler_menu;
    FirebaseRecyclerAdapter<ServicoCategoria, MenuViewHolder> adapter;
    private ProgressDialog progressDialog;
    private View mProgressView;
    private View mLoginFormView;

    public ServicosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.servicos_fragment, container,false);

        //aqui sera colocado a categoria dos servicos prestados
        mLoginFormView =v.findViewById(R.id.recyler_menu);
        mProgressView = v.findViewById(R.id.login_progress);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);

        recycler_menu=v.findViewById(R.id.recyler_menu);
        recycler_menu.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getContext(), 2);
        recycler_menu.setLayoutManager(mLayoutManager);
        database=FirebaseDatabase.getInstance();
        categoria=database.getReference("ServicoCategoria");

loadMenu();

        return v;
    }

    private void loadMenu() {
        showProgress(true);
        adapter=new FirebaseRecyclerAdapter<ServicoCategoria, MenuViewHolder>(
                ServicoCategoria.class,
                R.layout.servico_item,
                MenuViewHolder.class,
                categoria
        ) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, ServicoCategoria model, int position) {
                viewHolder.txtMenuName.setText(model.getNome());
                Picasso.get()
                        .load(model.getImagem())
                        .into(viewHolder.imageView);
                Animation animation= AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
                viewHolder.itemView.startAnimation(animation);
                showProgress(false);

                viewHolder.setOnClickListener(new MenuViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(getContext(), ListaServicos.class);
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
