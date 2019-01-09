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
    private EditText editTextPartida;
    private EditText editTextDestino;
    private EditText editTextDestino2;
    private EditText editTextDestino3;
    private EditText editTextDestino4;
    private Button botonIniciar;

    public AddressOperation(MapsActivity activity) {
            context = new WeakReference<MapsActivity>(activity);
        }

        @Override
        protected String doInBackground(String... strings) {
            botonIniciar = context.get().findViewById(R.id.buttonSolicitar);
            editTextACompletar = (EditText) Usuario.getInstance().getEditTextCompletar();
            if(editTextACompletar == null)
            {
                editTextACompletar = context.get().findViewById(R.id.editTextPartida);
            }
            context.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String cargando = "Cargando...";
                    editTextACompletar.setText(cargando);
                }
            });
            String address = ActivityUtils.getGeocoder(context.get(),strings[0],strings[1]);
            context.get().runOnUiThread(ActivityUtils.mensajeError(context.get()));
            return address;
    }

    @Override
    protected void onPostExecute(String s) {
        Usuario usuario = Usuario.getInstance();
        context.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editTextACompletar.setText(s);
                if(editTextACompletar.getId() == R.id.editTextPartida)
                {
                    usuario.setPlaceIdOrigen(s);
                    editTextDestino = context.get().findViewById(R.id.editTextDestino);
                    usuario.setEditTextCompletar(editTextDestino);
                    editTextDestino.requestFocus();
                }
                else if(editTextACompletar.getId() == R.id.editTextDestino)
                {
                    botonIniciar.setVisibility(View.VISIBLE);
                    usuario.getPlaceIdDestino()[0] = s;
                    editTextDestino2 = context.get().findViewById(R.id.editTextDestino2);
                    if(editTextDestino2.getVisibility() == View.VISIBLE) {
                        usuario.setEditTextCompletar(editTextDestino2);
                        editTextDestino2.requestFocus();
                    }
                }
                else if(editTextACompletar.getId() == R.id.editTextDestino2)
                {
                    usuario.getPlaceIdDestino()[1] = s;
                    editTextDestino3 = context.get().findViewById(R.id.editTextDestino3);
                    if(editTextDestino3.getVisibility() == View.VISIBLE) {
                        usuario.setEditTextCompletar(editTextDestino3);
                        editTextDestino3.requestFocus();
                    }
                }
                else if(editTextACompletar.getId() == R.id.editTextDestino3)
                {
                    usuario.getPlaceIdDestino()[2] = s;
                    editTextDestino4 = context.get().findViewById(R.id.editTextDestino4);
                    if(editTextDestino4.getVisibility() == View.VISIBLE) {
                        usuario.setEditTextCompletar(editTextDestino4);
                        editTextDestino4.requestFocus();
                    }
                }
                else if(editTextACompletar.getId() == R.id.editTextDestino4)
                {
                    usuario.getPlaceIdDestino()[3] = s;
                }
            }
        });
    }
}
