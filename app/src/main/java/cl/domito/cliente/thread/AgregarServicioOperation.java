package cl.domito.cliente.thread;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.ServicioActivity;
import cl.domito.cliente.activity.SplashScreenActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.http.Utilidades;

public class AgregarServicioOperation extends AsyncTask<String, Void, Void> {

    private WeakReference<MapsActivity> context;
    private TextView textViewError;

    public AgregarServicioOperation(MapsActivity activity) {
        context = new WeakReference<MapsActivity>(activity);
    }

    @Override
    protected Void doInBackground(String... strings) {
        Intent mainIntent = new Intent(context.get(),ServicioActivity.class);
        context.get().startActivity(mainIntent);
        String url = Utilidades.URL_BASE_SERVICIO + "AddServicio.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Usuario usuario = Usuario.getInstance();
        params.add(new BasicNameValuePair("app", "app"));
        params.add(new BasicNameValuePair("partida", usuario.getPlaceIdOrigen()));
        params.add(new BasicNameValuePair("destino", usuario.getPlaceIdDestino()[usuario.getCantidadDestinos()-1]));
        params.add(new BasicNameValuePair("cliente", usuario.getCliente()));
        params.add(new BasicNameValuePair("usuario", usuario.getNick()));
        JSONObject servicio = Utilidades.enviarPost(url,params);
        try {
            usuario.setIdViaje(servicio.getString("servicio_id"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        context.get().runOnUiThread(ActivityUtils.mensajeError(context.get()));
        return null;
    }

    @Override
    protected void onPostExecute(Void s) {

    }
}
