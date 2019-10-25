package com.fenix.wakonga.convites;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fenix.wakonga.MainActivity;
import com.fenix.wakonga.R;
import com.fenix.wakonga.model.Estilos;
import com.fenix.wakonga.model.FundoEstiloConvite;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EstilosConvite extends AppCompatActivity{
    FirebaseDatabase database;
    DatabaseReference categoria, fundoEstiloConviteReference;
    RecyclerView recycler_menu;
    FirebaseRecyclerAdapter<Estilos, ConviteAdapter> adapter;
    private ProgressDialog progressDialog;
    private View mProgressView;
    private View mLoginFormView;
    TextView estilo_nome;
    ImageView estilo_convite;
    FirebaseAuth mAuth;

    FloatingActionButton btn_fav;
    BottomSheetDialog bottomSheetDialog;
    String clickedCountryName;
    boolean isFIRSTIMECLICK=true;
    FundoEstiloConvite fundoEstiloConvite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estilos_convite);
        //aqui sera colocado a categoria dos servicos prestados
        mLoginFormView =findViewById(R.id.spinner_countries);
        mProgressView = findViewById(R.id.login_progress);
        fundoEstiloConvite = new FundoEstiloConvite();
        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(EstilosConvite.this);
        //progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Aguarde...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);

        recycler_menu=findViewById(R.id.spinner_countries);
        recycler_menu.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getApplicationContext(), 1);
        recycler_menu.setLayoutManager(mLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        categoria=database.getReference("Estilos");
        fundoEstiloConviteReference=database.getReference("Convite");

        View bottom_sheet_dialog=getLayoutInflater().inflate(R.layout.layout_estilos, null);

        estilo_nome=bottom_sheet_dialog.findViewById(R.id.estilo_nome);
        estilo_convite=bottom_sheet_dialog.findViewById(R.id.estilo_image);
        bottomSheetDialog=new BottomSheetDialog(this);
        btn_fav=bottom_sheet_dialog.findViewById(R.id.btn_fav);
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //fundoEstiloConvite.setFundEstiloConvite(clickedCountryName);
                FirebaseUser user = mAuth.getCurrentUser();
                fundoEstiloConviteReference.child(user.getUid()).child(fundoEstiloConvite.toString()).setValue(fundoEstiloConvite);
                Toast.makeText(getApplicationContext(), "Definido Convite "+estilo_nome.getText(), Toast.LENGTH_LONG).show();
                //chamar a tela de listagem depois que a img for carregada
                Intent intent = new Intent(EstilosConvite.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bottomSheetDialog.setContentView(bottom_sheet_dialog);


        loadMenu();
    }

    private void loadMenu() {
       showProgress(true);
       progressDialog.show();
        adapter=new FirebaseRecyclerAdapter<Estilos, ConviteAdapter>(
                Estilos.class,
                R.layout.convite_spinner_row,
                ConviteAdapter.class,
                categoria
        ) {
            @Override
            protected void populateViewHolder(final ConviteAdapter viewHolder, final Estilos model, int position) {
                viewHolder.txtMenuName.setText(model.getNome());
                viewHolder.txtMenuPrice.setText(model.getPreco());
              /*  Picasso.get()
                        .load(model.getImagem())
                        .into(viewHolder.imageView);
*/

                Glide.with(getApplicationContext())
                        .load(model.getConvite())
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
                viewHolder.setOnClickListener(new ConviteAdapter.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Estilos clickedItem=(Estilos)adapter.getItem(position);
                        //clickedCountryName=model.getConvite();
                        fundoEstiloConvite.setFundEstiloConvite(model.getConvite());
                        //clickedCountryName=clickedItem.getConvite();
                        //ImageView imageView=view.findViewById(R.id.image_flag);
                        // Drawable drawable=imageView.getDrawable();
                        //spinnerprincipal.setBackground(drawable);
                        // Toast.makeText(getApplicationContext(), clickedCountryName +" Selecionado", Toast.LENGTH_LONG).show();
                        if (!isFIRSTIMECLICK){
                            estilo_nome.setText(model.getNome());
                            //Picasso.get().load(model.getConvite()).into(estilo_convite);
                            Glide.with(getApplicationContext())
                                    .load(model.getConvite())
                                    .into(new SimpleTarget<Drawable>(){
                                        @Override
                                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                //rootContent.setBackground(resource);
                                                estilo_convite.setImageDrawable(resource);
                                            }
                                        }
                                    });
                            bottomSheetDialog.show();
                        }else {
                            isFIRSTIMECLICK=false;
                        }
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
