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
import com.fenix.wakonga.model.Festa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ActivityFesta extends AppCompatActivity {
    EditText  desc, salao;
    Button guardarDados;
   // EditText data;
    String Database_Path = "Convite";
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    int ano, mes, dia;

    int hour, minute;
    String am_pm;
    TimePicker horaFesta;
    DatePicker dataFesta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_festa);
        salao=(EditText)findViewById(R.id.salao);


        Calendar calendar=Calendar.getInstance();
        ano=calendar.get(Calendar.YEAR);
        mes=calendar.get(Calendar.MARCH);
        dia=calendar.get(Calendar.DAY_OF_MONTH);
        dataFesta= findViewById(R.id.data);

        horaFesta=findViewById(R.id.hora);
        desc=(EditText)findViewById(R.id.desc);
       ;


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

        salao.setError(null);
        //data.setError(null);
       // hora.setError(null);
        desc.setError(null);

        boolean cancel = false;
        View focusView = null;
        //Pattern pat = Pattern.compile ("^[A-Za-z]+$");


        if(salao.getText().toString().isEmpty())

        {
            focusView = salao;
            salao.setError("Por favor informe o nome do salão ");
            cancel = true;

        }/*else if(!salao.getText().toString().matches(pat.toString()))
        {
            focusView = salao;
            salao.setError("Nome Invalido");
            cancel = true;
        }*/
        if (Build.VERSION.SDK_INT>23){
            hour=horaFesta.getHour();
            minute=horaFesta.getMinute();
        }else {
            hour=horaFesta.getCurrentHour();
            minute=horaFesta.getCurrentMinute();
        }
        if (hour>12){
            am_pm="PM";
            hour=hour-12;
        }else {
            am_pm="AM";
        }
     /*   else if (data.getText().toString().isEmpty()||
                data.getText().toString().
                        equalsIgnoreCase("Data Copo da Água(Clique para selecionar uma data)")){
            focusView = data;
            data.setError("Por favor Informe a data!");
            cancel = true;
        }else if (hora.getText().toString().isEmpty()){
            focusView = hora;
            hora.setError("Por favor Informe a Hora!");
            cancel=true;
        }*/
        if (cancel) {
            Animation animation=AnimationUtils.loadAnimation(ActivityFesta.this, android.R.anim.fade_in);
            focusView.requestFocus();
            focusView.startAnimation(animation);
            // focusView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
 }else {
 Festa festa=new Festa();
        festa.setSalao(salao.getText().toString());
        festa.setData(dataFesta.getDayOfMonth()+"-"+ (dataFesta.getMonth() + 1)+"-"+dataFesta.getYear());
        festa.setHora(hour +":"+ minute+" "+am_pm);
        festa.setDesc(desc.getText().toString());
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();


        // Getting image upload ID.
        // String ImageUploadId = databaseReference.push().getKey();

        // Adding image upload id s child element into databaseReference.
        databaseReference.child(user.getUid()).child(festa.toString()).setValue(festa);
        //chamar a tela de listagem depois que a img for carregada
        Intent intent=new Intent(ActivityFesta.this, NoivosCasamento.class);
        startActivity(intent);
        finish();

        }
    }



    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.data==id){
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
            //data.setText(dia+"-"+(mes+1)+"-"+ano);
        }
    };
}

