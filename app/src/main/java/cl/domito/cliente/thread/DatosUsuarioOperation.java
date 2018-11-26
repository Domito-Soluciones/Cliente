package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

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
        String url = Utilidades.URL_BASE_USUARIO + "DatosUsuario.php?id="+usuario.getNick();
        try {
            JSONObject jsonObject = RequestUsuario.datosUsuario(url);
            context.get().runOnUiThread(ActivityUtils.mensajeError(context.get()));
            if(jsonObject != null) {
                usuario.setNombre(jsonObject.getString("usuario_nombre"));
                usuario.setCliente(jsonObject.getString("usuario_cliente"));
                usuario.setPassword(jsonObject.getString("usuario_password"));
                usuario.setCelular(jsonObject.getString("usuario_celular"));
                usuario.setDireccion(jsonObject.getString("usuario_direccion"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
