package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;

public class AddressOperation extends AsyncTask<String, Void, String> {


    private WeakReference<MapsActivity> context;
    private EditText editTextACompletar;
    private EditText editTextDestino;
    private Button botonIniciar;

    public AddressOperation(MapsActivity activity) {
            context = new WeakReference<MapsActivity>(activity);
        }

        @Override
        protected String doInBackground(String... strings) {
            String tipo = strings[2];
            if(tipo.equals(Usuario.BUSCAR_PARTIDA+"")) {
                editTextACompletar = context.get().findViewById(R.id.editTextPartida);
                editTextDestino = context.get().findViewById(R.id.editTextDestino);
            }
        else if(tipo.equals(Usuario.BUSCAR_DESTINO+""))
        {
            botonIniciar = context.get().findViewById(R.id.buttonSolicitar);
            editTextACompletar = context.get().findViewById(R.id.editTextDestino);
        }
        context.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String cargando = "Cargando...";
                editTextACompletar.setText(cargando);
            }
        });
        String address = ActivityUtils.getAddress(context.get(),strings[0],strings[1],tipo);
        context.get().runOnUiThread(ActivityUtils.mensajeError(context.get()));
        return address;
    }

    @Override
    protected void onPostExecute(String s) {
        context.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editTextACompletar.setText(s);
                if(editTextDestino != null) {
                    //editTextDestino.requestFocus();
                }
                else
                {
                    botonIniciar.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
