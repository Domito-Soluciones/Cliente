package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.view.View;
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

public class CancelarViajeOperation extends AsyncTask<Object, Void, Void> {

    private WeakReference<MapsActivity> context;
    GoogleMap map;

    public CancelarViajeOperation(MapsActivity mapsActivity) {
    }

    public CancelarViajeOperation(WeakReference<MapsActivity> context) {
        this.context = context;
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
    protected void onPostExecute(Void aVoid) {
        System.out.println("");
        if(context != null) {
            Usuario.getInstance().setEnProceso(false);
            ConstraintLayout constrainLayoutConductor = context.get().findViewById(R.id.constrainLayoutConductor);
            TextView textViewInicio = context.get().findViewById(R.id.textViewPartida);
            constrainLayoutConductor.setVisibility(View.GONE);
            map.clear();
            textViewInicio.setVisibility(View.VISIBLE);
        }
    }
}
