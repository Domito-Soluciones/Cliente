package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.dominio.Usuario;

public class DatosUsuarioOperation  extends AsyncTask<Void, Void, Void> {

    private WeakReference<MapsActivity> context;
    TextView textViewError;

    public DatosUsuarioOperation(MapsActivity activity) {
        context = new WeakReference<MapsActivity>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Usuario usuario = Usuario.getInstance();
        String url = Utilidades.URL_BASE_USUARIO + "GetPasajero.php";
        try {
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("id",usuario.getNick()));
            JSONObject jsonObject = RequestUsuario.datosUsuario(url,params);
            context.get().runOnUiThread(ActivityUtils.mensajeError(context.get()));
            if(jsonObject != null) {
                usuario.setId(jsonObject.getString("pasajero_id"));
                String nombre = jsonObject.getString("pasajero_nombre") + " " + jsonObject.getString("pasajero_papellido")
                        + " " +  jsonObject.getString("pasajero_mapellido");
                usuario.setNombre(nombre);
                usuario.setMail(jsonObject.getString("pasajero_mail"));
                usuario.setCliente(jsonObject.getString("pasajero_cliente"));
                usuario.setCelular(jsonObject.getString("pasajero_celular"));
                usuario.setDireccion(jsonObject.getString("pasajero_direccion"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        context.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = context.get().findViewById(R.id.textViewNombreUsuario);
                textView.setText(Usuario.getInstance().getNombre());
            }
        });
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
