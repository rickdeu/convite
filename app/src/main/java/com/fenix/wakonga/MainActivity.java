package com.fenix.wakonga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fenix.wakonga.cerimonias.ActivityConservatoria;
import com.fenix.wakonga.cerimonias.ActivityFesta;
import com.fenix.wakonga.cerimonias.ActivityIgreja;
import com.fenix.wakonga.convites.DizeresConvite;
import com.fenix.wakonga.convites.EstilosConvite;
import com.fenix.wakonga.login.LoginActivity;
import com.fenix.wakonga.noivos.ActivityAdicionarNoiva;
import com.fenix.wakonga.noivos.ActivityAdicionarNoivo;
import com.fenix.wakonga.noivos.FotoCapa;
import com.fenix.wakonga.scannerFragment.ScanActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));

        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.id_opcoes) {
        }
        if (id == R.id.scan_convidado) {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.id_home) {
            Intent intent = new Intent( MainActivity.this , MainActivity.class);
            startActivity ( intent ) ;
        }
        if (id == R.id.estilo_convite) {
            Intent intent = new Intent( MainActivity.this , EstilosConvite.class);
            startActivity ( intent ) ;
        }

        else if (id == R.id.editar_foto_capa) {
            Intent intent = new Intent( MainActivity.this , FotoCapa.class);
            startActivity ( intent ) ;

        }else if (id == R.id.textoConvite) {
            Intent intent = new Intent( MainActivity.this , DizeresConvite.class);
            startActivity ( intent ) ;
        } else if (id == R.id.id_dados_noivo) {
            Intent intent = new Intent( MainActivity.this , ActivityAdicionarNoivo.class);
            startActivity ( intent ) ;
        }
        else if (id == R.id.id_dados_noiva) {
            Intent intent = new Intent( MainActivity.this , ActivityAdicionarNoiva.class);
            startActivity ( intent ) ;
        }
        else if (id == R.id.id_cerimonia_civil) {
            Intent intent = new Intent( MainActivity.this , ActivityConservatoria.class);
            startActivity ( intent ) ;

        }
        else if (id == R.id.id_cerimonia_religiosa) {
            Intent intent = new Intent( MainActivity.this , ActivityIgreja.class);
            startActivity ( intent ) ;

        }

        else if (id == R.id.id_copo_agua) {
            Intent intent = new Intent( MainActivity.this , ActivityFesta.class);
            startActivity ( intent ) ;
        }

        else if (id==R.id.nav_sair){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

}
