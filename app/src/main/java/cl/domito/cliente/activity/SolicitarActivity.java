package cl.domito.cliente.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cl.domito.cliente.R;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.thread.CancelarViajeOperation;

public class SolicitarActivity extends AppCompatActivity {

    Button buttonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);
        this.getSupportActionBar().hide();
        buttonCancelar = findViewById(R.id.button3);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarViaje();
            }
        });
    }

    private void cancelarViaje() {
        CancelarViajeOperation cancelarViajeOperation = new CancelarViajeOperation();
        cancelarViajeOperation.execute();
        this.finish();
    }

}