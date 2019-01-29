package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.http.Utilidades;

public class GetConductorOperation extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... strings) {
        JSONObject conductor = null;
        String movil = strings[0];
        String url = Utilidades.URL_BASE_MOVIL + "GetMovilConductor.php";
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("id",movil));
        conductor = Utilidades.enviarPost(url,params);
        return conductor;
    }

}
