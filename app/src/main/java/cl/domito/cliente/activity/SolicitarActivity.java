package cl.domito.cliente.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.service.SolicitarViajeService;
import cl.domito.cliente.thread.CancelarViajeOperation;

public class SolicitarActivity extends AppCompatActivity {

    Button buttonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar);
        this.getSupportActionBar().hide();
        buttonCancelar = findViewById(R.id.button3);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarViaje();
                SolicitarViajeService.TERMINAR = false;
            }
        });
    }

    private void cancelarViaje() {
        CancelarViajeOperation cancelarViajeOperation = new CancelarViajeOperation();
        cancelarViajeOperation.execute();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        Intent i = new Intent(this, SolicitarViajeService.class);
        stopService(i);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, new IntentFilter("custom-event-name"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        bManager.unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String value = intent.getStringExtra("value");
            switch (message) {
                case SolicitarViajeService.OCULTAR_LAYOUT_SERVICIO:
                    finalizar();
                break;
                case SolicitarViajeService.MOSTRAR_NOTIFICACION_SERVICIO:
                    notificar("Servicio En camino","");
                break;
                case SolicitarViajeService.CREAR_MARCADOR_MOVIL:
                        crearMarcador();
                    break;
            }
        }
    };

    private void finalizar()
    {
        this.finish();
    }

    private void notificar(String titulo,String valor)
    {
        ActivityUtils.enviarNotificacion(this,titulo,valor, R.drawable.furgoneta);
    }

    private void crearMarcador()
    {
        Usuario.getInstance().setEnProceso(true);
    }

}