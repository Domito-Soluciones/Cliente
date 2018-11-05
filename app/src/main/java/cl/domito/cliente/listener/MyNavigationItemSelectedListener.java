package cl.domito.cliente.listener;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.LoginActivity;
import cl.domito.cliente.activity.ServicioActivity;
import cl.domito.cliente.http.Utilidades;

/**
 * Created by elsan on 01-05-2018.
 */

public class MyNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    Activity activity;

    public MyNavigationItemSelectedListener(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.servicio) {
            Intent mainIntent = new Intent(this.activity,ServicioActivity.class);
            this.activity.startActivity(mainIntent);
        }
        if (id == R.id.salir) {
            Utilidades.USUARIO_ACTIVO = false;
            Intent mainIntent = new Intent(this.activity,LoginActivity.class);
            this.activity.startActivity(mainIntent);
            this.activity.finish();
        }

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
