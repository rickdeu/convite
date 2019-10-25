package com.fenix.wakonga.cerimonias;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.fenix.wakonga.R;
import com.fenix.wakonga.informacaoCasamento.NoivosCasamento;
import com.fenix.wakonga.model.Igreja;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ActivityIgreja extends AppCompatActivity {
    EditText nomeCons, descCons;
    Button guardarDados;
    String Database_Path = "Convite";
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    int ano, mes, dia;

    int hour, minute;
    String am_pm;
    TimePicker horaCons;
    DatePicker dataCons;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_igreja);
        nomeCons=(EditText)findViewById(R.id.nomeIgreja);


        Calendar calendar=Calendar.getInstance();

        ano=calendar.get(Calendar.YEAR);
        mes=calendar.get(Calendar.MARCH);
        dia=calendar.get(Calendar.DAY_OF_MONTH);
        dataCons=findViewById(R.id.dataIgreja);
        horaCons=findViewById(R.id.horaIgreja);
        descCons=(EditText)findViewById(R.id.descIgreja);
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

        nomeCons.setError(null);
        //dataCons.setError(null);
        //horaCons.setError(null);
        descCons.setError(null);

        boolean cancel = false;
        View focusView = null;
        //Pattern pat = Pattern.compile ("^[A-Za-z]+$");


        if(nomeCons.getText().toString().isEmpty())

        {
            focusView = nomeCons;
            nomeCons.setError("Por favor informe o nome da Conservatoria ");
            cancel = true;

        }/*else if(!nomeCons.getText().toString().matches(pat.toString()))
        {
            focusView = nomeCons;
            nomeCons.setError("Nome Invalido");
            cancel = true;
        }*/
        if (Build.VERSION.SDK_INT>23){
            hour=horaCons.getHour();
            minute=horaCons.getMinute();
        }else {
            hour=horaCons.getCurrentHour();
            minute=horaCons.getCurrentMinute();
        }
        if (hour>12){
            am_pm="PM";
            hour=hour-12;
        }else {
            am_pm="AM";
        }
       /* else if (dataCons.getText().toString().isEmpty()||
                dataCons.getText().toString().
                        equalsIgnoreCase("Data Cerimonia Civil(Clique para selecionar data)")){
            focusView = dataCons;
            dataCons.setError("Por favor Informe a data!");
            cancel = true;
        }else if (horaCons.getText().toString().isEmpty()){
            focusView = horaCons;
            horaCons.setError("Por favor Informe a Hora!");
            cancel=true;
        }*/
        if (cancel) {
            Animation animation=AnimationUtils.loadAnimation(ActivityIgreja.this, android.R.anim.fade_in);
            focusView.requestFocus();
            focusView.startAnimation(animation);
            // focusView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else {
        Igreja igreja = new Igreja();
        igreja.setNome(nomeCons.getText().toString());
        igreja.setData(dataCons.getDayOfMonth()+"-"+ (dataCons.getMonth() + 1)+"-"+dataCons.getYear());
        igreja.setHora(hour +":"+ minute+" "+am_pm);
        igreja.setDesc(descCons.getText().toString());
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child(igreja.toString()).setValue(igreja);
        Intent intent = new Intent(ActivityIgreja.this, NoivosCasamento.class);
        startActivity(intent);
        finish();
    }
    }

    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.dataIgreja==id){
            return new DatePickerDialog(this, listener, ano,mes,dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            ano=year;
            mes=month;
            dia=dayOfMonth;
            //dataCons.setText(dia+"-"+(mes+1)+"-"+ano);
        }
    };
}
