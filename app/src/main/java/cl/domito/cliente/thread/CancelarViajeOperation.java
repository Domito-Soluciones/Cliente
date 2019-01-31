package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.service.SolicitarViajeService;

public class CancelarViajeOperation extends AsyncTask<Object, Void, Void> {

    private WeakReference<MapsActivity> context;
    GoogleMap map;

    public CancelarViajeOperation() {
    }

    public CancelarViajeOperation(MapsActivity mapsActivity) {
        context = new WeakReference<MapsActivity>(mapsActivity);
    }

    @Override
    protected Void doInBackground(Object... objects) {
        map = (GoogleMap) objects[0];
        String url = Utilidades.URL_BASE_SERVICIO + "DelServicio.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Usuario usuario = Usuario.getInstance();
        params.add(new BasicNameValuePair("app", "app"));
        params.add(new BasicNameValuePair("id",usuario.getIdViaje()));
        JSONObject servicio = Utilidades.enviarPost(url,params);
        if(servicio != null)
        {
            usuario.setIdViaje(null);
            usuario.setBusquedaRealizada(false);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        Button buttonCancelar = context.get().findViewById(R.id.buttonCancelar);
        buttonCancelar.setText("Cancelando...");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(context != null) {
            Usuario.getInstance().setEnProceso(false);
            ConstraintLayout constrainLayoutConductor = context.get().findViewById(R.id.constrainLayoutConductor);
            ConstraintLayout constraintLayoutInicio = context.get().findViewById(R.id.constrainLayoutInicioViaje);
            constrainLayoutConductor.setVisibility(View.GONE);
            map.clear();
            constraintLayoutInicio.setVisibility(View.VISIBLE);
            EditText editTextPartida = context.get().findViewById(R.id.editTextPartida);
            EditText editTextDestino = context.get().findViewById(R.id.editTextDestino);
            EditText editTextDestino2 = context.get().findViewById(R.id.editTextDestino2);
            EditText editTextDestino3 = context.get().findViewById(R.id.editTextDestino3);
            EditText editTextDestino4 = context.get().findViewById(R.id.editTextDestino4);
            Usuario.getInstance().setEditTextCompletar(null);
            editTextPartida.requestFocus();
            editTextPartida.setText("");
            editTextDestino.setText("");
            editTextDestino2.setText("");
            editTextDestino3.setText("");
            editTextDestino4.setText("");
            SolicitarViajeService.NOT_LLEGANDO = true;
            SolicitarViajeService.NOT_LLEGO = true;
        }
    }
}
