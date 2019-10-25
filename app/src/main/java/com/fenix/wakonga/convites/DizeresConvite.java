package com.fenix.wakonga.convites;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.fenix.wakonga.R;
import com.fenix.wakonga.informacaoCasamento.NoivosCasamento;
import com.fenix.wakonga.model.Dizeres;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class DizeresConvite extends AppCompatActivity {

    EditText text1, text2, mensagem3;
    Button guardarDados;
    String Database_Path = "Convite";
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizeres_convite);

        text1=(EditText)findViewById(R.id.texto1);


        text2=findViewById(R.id.texto2);
        mensagem3=(EditText)findViewById(R.id.mensagem3);
        guardarDados=(Button)findViewById(R.id.guardarDados);

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarDados();
            }
        });
    }

    private void carregarDados() {

        text1.setError(null);
        text2.setError(null);
        mensagem3.setError(null);

        boolean cancel = false;
        View focusView = null;
        Pattern pat = Pattern.compile ("^[A-Za-z\\s]{1,}[A-Za-z\\p{L}][\\.]{0,1}[A-Za-z\\s]{0,}$");


      /*  if(text1.getText().toString().isEmpty())        {
            focusView = text1;
            text1.setError("Texto não pode ser vazio");
            cancel = true;

        }else if(!text1.getText().toString().matches(pat.toString()))        {
            focusView = text1;
            text1.setError("Texto Invalido");
            cancel = true;
        }
        else
        if(text2.getText().toString().isEmpty())        {
            focusView = text2;
            text1.setError("Texto não pode ser vazio");
            cancel = true;

        }else if(!text2.getText().toString().matches(pat.toString()))        {
            focusView = text2;
            text2.setError("Texto Invalido");
            cancel = true;
        }

        else*/
        if(mensagem3.getText().toString().isEmpty())        {
            focusView = mensagem3;
            mensagem3.setError("Texto não pode ser vazio");
            cancel = true;

        }else if(!mensagem3.getText().toString().matches(pat.toString()))        {
            focusView = mensagem3;
            mensagem3.setError("Mensagem Invalida");
            cancel = true;
        }
        if (cancel) {
            Animation animation= AnimationUtils.loadAnimation(DizeresConvite.this, android.R.anim.fade_in);
            focusView.requestFocus();
            focusView.startAnimation(animation);
            // focusView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else {
            Dizeres dizeres = new Dizeres();
            //dizeres.setTexto1(text1.getText().toString());
           // dizeres.setTexto2(text2.getText().toString());
            dizeres.setMensagem3(mensagem3.getText().toString());

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            databaseReference.child(user.getUid()).child(dizeres.toString()).setValue(dizeres);
            Intent intent = new Intent(DizeresConvite.this, NoivosCasamento.class);
            startActivity(intent);
            finish();
        }
    }
    }

