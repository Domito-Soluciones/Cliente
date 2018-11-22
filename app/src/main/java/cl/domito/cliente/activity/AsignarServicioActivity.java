package cl.domito.cliente.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import cl.domito.cliente.R;

public class AsignarServicioActivity extends AppCompatActivity {

    ImageView imageViewAtras;
    ImageView imageViewMas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** INICIALIZACION ACTIVITY **/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_servicio);
        /** INICIALIZACION VIEW **/
        imageViewAtras = findViewById(R.id.imageViewAtras);
        imageViewMas = findViewById(R.id.imageViewMas);
        /** INICIALIZACION EVENTO BOTON DE ATRAS **/
        imageViewMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
        /** INICIALIZACION EVENTO BOTON MAS **/
        imageViewMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarDestino();
            }
        });
    }


    private void volver() {
        this.finish();
    }

    private void agregarDestino() {

    }

}
