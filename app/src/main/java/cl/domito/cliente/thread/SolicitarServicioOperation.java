package cl.domito.cliente.thread;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.http.Utilidades;

public class SolicitarServicioOperation extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... strings) {
        String url = Utilidades.URL_BASE_SERVICIO + "GetServicio.php";
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("id",strings[0]));
        JSONObject jsonObject = Utilidades.enviarPost(url,params);
        return jsonObject;
    }
}
