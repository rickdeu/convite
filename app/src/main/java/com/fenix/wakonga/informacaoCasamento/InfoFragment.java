package com.fenix.wakonga.informacaoCasamento;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.wakonga.R;
import com.fenix.wakonga.model.Conservatoria;
import com.fenix.wakonga.model.Dizeres;
import com.fenix.wakonga.model.Festa;
import com.fenix.wakonga.model.Igreja;
import com.fenix.wakonga.model.Noiva;
import com.fenix.wakonga.model.Noivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

public class InfoFragment extends Fragment {
    View v;
    TextView nomeNoivo, sobrenomeNoivo,dataNascimentoNoivo, telefoneNoivo, casaNoivo,bairroNoivo, emailNoivo, biNoivo, noivoTexto;
    ImageView fotoNoivo, fraseConvite, textocasamentoimagem, textocasmentoimagem1;
    TextView nomeNoiva, sobrenomeNoiva,dataNascimentoNoiva, telefoneNoiva, casaNoiva,bairroNoiva, emailNoiva, biNoiva, noivaTexto;
    TextView nomeConservatoria, dataConservatoria, horaConservatoria;
    TextView nomeIgreja, dataIgreja, horaIgreja;
    TextView nomeSalao, dataSalao, horaSalao;
    TextView cerimonia_civl, cerimonia_religiosa,copo_agua;
    TextView mensagemconvite, textoConvidar, nossoCasamento;

    TextView noivaConvite, noivoConvite;
    ImageView fotoNoiva;
    View   viewFraseconvite,textoConvite, nossoCasamentoView;
    EditText inserirFrase, inserirTextoCasamento, inserirTextoNossoCasamento;


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference noivo;
    DatabaseReference dizeres;
    DatabaseReference noiva;
    DatabaseReference civil;
    DatabaseReference igreja;
    DatabaseReference festa;



    private FirebaseAuth mAuth;
    private FoldingCell fc,fc2;

    public InfoFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.informacao_fragment_casal, container, false);








        //Dados do noivo
        nomeNoivo=v.findViewById(R.id.nomeNoivo);
        fotoNoivo=v.findViewById(R.id.fotoNoivo);
        sobrenomeNoivo=v.findViewById(R.id.sobrenomeNoivo);
        dataNascimentoNoivo=v.findViewById(R.id.data_nascimento_noivo);
        telefoneNoivo=v.findViewById(R.id.telefone_noivo);
        casaNoivo=v.findViewById(R.id.casa_noivo);
        bairroNoivo=v.findViewById(R.id.morada_noivo);
        emailNoivo=v.findViewById(R.id.email_noivo);
        biNoivo=v.findViewById(R.id.bilhete_noivo);
        noivoTexto=v.findViewById(R.id.noivo);


        //Dados da noiva
        nomeNoiva=v.findViewById(R.id.nomeNoiva);
        fotoNoiva=v.findViewById(R.id.fotoNoiva);
        sobrenomeNoiva=v.findViewById(R.id.sobrenomeNoiva);
        dataNascimentoNoiva=v.findViewById(R.id.data_nascimento_noiva);
        telefoneNoiva=v.findViewById(R.id.telefone_noiva);
        casaNoiva=v.findViewById(R.id.casa_noiva);
        bairroNoiva=v.findViewById(R.id.morada_noiva);
        emailNoiva=v.findViewById(R.id.email_noiva);
        biNoiva=v.findViewById(R.id.bilhete_noiva);
        noivaTexto=v.findViewById(R.id.noiva);


        //Dados conservatoria
        nomeConservatoria=v.findViewById(R.id.nome_conservatoria);
        dataConservatoria=v.findViewById(R.id.data_conservatoria);
        horaConservatoria=v.findViewById(R.id.hora_conservatoria);
        cerimonia_civl=v.findViewById(R.id.cerimonia_civl);

        //Dados Igreja
        nomeIgreja=v.findViewById(R.id.nome_igreja);
        dataIgreja=v.findViewById(R.id.data_igreja);
        horaIgreja=v.findViewById(R.id.hora_igreja);
        cerimonia_religiosa=v.findViewById(R.id.cerimonia_religiosa);


        //Dados Salao
        nomeSalao=v.findViewById(R.id.nome_salao);
       // dataSalao=v.findViewById(R.id.data_igreja);
        horaSalao=v.findViewById(R.id.hora_salao);
        copo_agua=v.findViewById(R.id.copo_agua);


        noivoConvite=v.findViewById(R.id.noivo_convite);
        noivaConvite=v.findViewById(R.id.noiva_convite);
        fraseConvite=v.findViewById(R.id.fraseconvite);
        textocasamentoimagem=v.findViewById(R.id.textocasamentoimagem);
        textocasmentoimagem1=v.findViewById(R.id.textocasamentoimagem1);
        //dizeres convite
        mensagemconvite=v.findViewById(R.id.mensagemconvite);
        textoConvidar=v.findViewById(R.id.textoConvidar);
        nossoCasamento=v.findViewById(R.id.nossocasamento);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        //verifica se um usuario esta logado
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){
            noivo=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Noivo");
            noiva=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Noiva");
            civil=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Conservatoria");
            igreja=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Igreja");
            festa=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Festa");
            dizeres=mFirebaseDatabase.getReference("Convite/"+user.getUid()+"/Dizeres");

        }

        dadosNoivo();
        dadosNoiva();
        dadosConservatoria();
        dadosIgreja();
        dadosSalao();
        dizeresConvite();
        fc=(FoldingCell)v.findViewById(R.id.folding_cell);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fc.toggle(false);
                    fc.fold(true);
                    fc.unfold(false);
                    fc.animate();

            }
        });
        fc2=(FoldingCell)v.findViewById(R.id.folding_cell2);
        fc2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fc2.toggle(false);
                fc2.fold(true);
                fc2.unfold(false);
                fc2.animate();

            }
        });
     /*   fraseConvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fraseConviteInserir();
            }
        });
        textocasamentoimagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInserirTextoCasamento();
            }
        });
        textocasmentoimagem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInserirTextoNossoCasamento();
            }
        });
*/
        return v;
    }
    private void dizeresConvite(){
        dizeres.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dizeres dizeres=dataSnapshot.getValue(Dizeres.class);
                if (dizeres!=null){
                  //  mensagemconvite.setText(dizeres.getMensagem());
                   // textoConvidar.setText(dizeres.getConvidar());
                   // nossoCasamento.setText(dizeres.getHonra());
               }
                else {
                   // Toast.makeText(getContext(), "Preencha os dados do convite", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
/*
    private void fraseConviteInserir(){
      //  viewFraseconvite=inflater.inflate(R.layout.fraseconvite, container, false);
        View viewFraseconvite=LayoutInflater.from(getContext())
                .inflate(R.layout.fraseconvite,null);
        inserirFrase=viewFraseconvite.findViewById(R.id.textoConvite);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(viewFraseconvite);
        builder.setTitle("Editar Frase Convite");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dizeres dizer=new Dizeres();
                dizer.setMensagem(inserirFrase.getText().toString());
                dizeres.child(dizer.toString()).setValue(dizer);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
}
    private void setInserirTextoCasamento() {
        View textoConvite=LayoutInflater.from(getContext())
                .inflate(R.layout.textocasamento,null);
        inserirTextoCasamento=textoConvite.findViewById(R.id.textocasamento);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(textoConvite);
        builder.setTitle("Editar Frase Convite");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dizeres dizer=new Dizeres();
                dizer.setConvidar(inserirTextoCasamento.getText().toString());
                dizeres.child(dizer.toString()).setValue(dizer);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    private void setInserirTextoNossoCasamento() {
        View textoConvite=LayoutInflater.from(getContext())
                .inflate(R.layout.nossocasamento,null);
        inserirTextoNossoCasamento=textoConvite.findViewById(R.id.nossocasamento);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(textoConvite);
        builder.setTitle("Editar Frase Convite");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dizeres dizer=new Dizeres();
                dizer.setHonra(inserirTextoNossoCasamento.getText().toString());
                dizeres.child(dizer.toString()).setValue(dizer);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }

  */
    public void dadosNoivo(){
        noivo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Noivo noivo=dataSnapshot.getValue(Noivo.class);
                if (noivo!=null){
                    nomeNoivo.setText(noivo.getNome());
                    sobrenomeNoivo.setText(noivo.getSobrenome());
                    telefoneNoivo.setText(noivo.getTelefone());
                    casaNoivo.setText(noivo.getCasa());
                    bairroNoivo.setText(noivo.getBairro());
                    emailNoivo.setText(noivo.getEmail());
                    biNoivo.setText(noivo.getBi());
                    noivoTexto.setText(noivo.toString());
                    dataNascimentoNoivo.setText(noivo.getDataNascimento());
                    noivoConvite.setText(noivo.getNome()+" "+noivo.getSobrenome()+" & ");
                    if (noivo.getFoto()!=null){
                        Picasso.get().load(noivo.getFoto()).into(fotoNoivo);
                    }else {
                        Picasso.get().load(R.drawable.logoeditado).into(fotoNoivo);
                    }
                }else {
                    Toast.makeText(getContext(), "Dados do Noivo em falta", Toast.LENGTH_LONG).show();
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
                    sobrenomeNoiva.setText(noiva.getSobrenome());
                    telefoneNoiva.setText(noiva.getTelefone());
                    casaNoiva.setText(noiva.getCasa());
                    bairroNoiva.setText(noiva.getBairro());
                    emailNoiva.setText(noiva.getEmail());
                    biNoiva.setText(noiva.getBi());
                    dataNascimentoNoiva.setText(noiva.getDataNascimento().toString());
                    noivaConvite.setText(noiva.getNome()+" "+noiva.getSobrenome());
                    noivaTexto.setText(noiva.toString());
                    if (noiva.getFoto()!=null){
                        Picasso.get().load(noiva.getFoto()).into(fotoNoiva);
                    }else {
                        Picasso.get().load(R.drawable.logoeditado).into(fotoNoiva);
                    }
                }else {
                    Toast.makeText(getContext(), "Dados da Noiva em falta", Toast.LENGTH_LONG).show();
                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void dadosConservatoria(){
        civil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Conservatoria conservatoria=dataSnapshot.getValue(Conservatoria.class);
                if (conservatoria!=null){
                    nomeConservatoria.setText(conservatoria.getNome());
                    dataConservatoria.setText("Dia "+conservatoria.getData());
                    horaConservatoria.setText("às "+conservatoria.getHora());
                    cerimonia_civl.setText("Cerimônia Cívil");
                }else {
                    Toast.makeText(getContext(), "Dados da Conservatoria em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void dadosIgreja(){
        igreja.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Igreja igreja=dataSnapshot.getValue(Igreja.class);
                if (igreja!=null){
                    nomeIgreja.setText(igreja.getNome());
                    dataIgreja.setText("Dia "+igreja.getData());
                    horaIgreja.setText("às "+igreja.getHora());
                    cerimonia_religiosa.setText("Cerimônia Religiosa");
                }else {
                    Toast.makeText(getContext(), "Dados da Igreja em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public void dadosSalao(){
        festa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Festa festa=dataSnapshot.getValue(Festa.class);
                if (festa!=null){
                    nomeSalao.setText(festa.getSalao());
                  //  dataIgreja.setText("Dia "+igreja.getData());
                    horaSalao.setText("às "+festa.getHora());
                    copo_agua.setText("Copo de Água");
                }else {
                    Toast.makeText(getContext(), "Dados do Salao em falta", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.activar_edicao){
            LayoutInflater  editar=LayoutInflater.from(getContext());
            View activarEdicao=editar.inflate(R.layout.informacao_fragment_casal, null);
            ImageView editar1, editar2,editar3,editar4;

            editar1=(ImageView)activarEdicao.findViewById(R.id.editar1);
            editar2=(ImageView)activarEdicao.findViewById(R.id.editar2);
            editar3=(ImageView)activarEdicao.findViewById(R.id.editar3);
            editar4=(ImageView)activarEdicao.findViewById(R.id.editar4);

            editar1.setEnabled(true);
            editar2.setEnabled(true);
            editar3.setEnabled(true);
            editar4.setEnabled(true);

            editar1.setVisibility(View.VISIBLE);
            editar2.setVisibility(View.VISIBLE);
            editar3.setVisibility(View.VISIBLE);
            editar4.setVisibility(View.VISIBLE);

        }


        return super.onOptionsItemSelected(item);
    }*/

    //verifica se tem conexao a internet
    private boolean haveNetworkConnection(){
        boolean haveConnectionWifi=false;
        boolean haveConnectionMobile=false;
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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
}
