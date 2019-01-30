package cl.domito.cliente.thread;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.ServicioActivity;
import cl.domito.cliente.activity.SolicitarActivity;
import cl.domito.cliente.activity.SplashScreenActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.service.SolicitarViajeService;

public class AgregarServicioOperation extends AsyncTask<String, Void, Void> {

    private WeakReference<MapsActivity> context;
    private TextView textViewError;

    public AgregarServicioOperation(MapsActivity activity) {
        context = new WeakReference<MapsActivity>(activity);
    }

    @Override
    protected Void doInBackground(String... strings) {
        Intent mainIntent = new Intent(context.get(),SolicitarActivity.class);
        context.get().startActivity(mainIntent);
        String url = Utilidades.URL_BASE_SERVICIO + "AddServicio.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Usuario usuario = Usuario.getInstance();
        String destFinal = "";
        String destInt1 = "";
        String destInt2 = "";
        String destInt3 = "";
        String destInt4 = "";
        params.add(new BasicNameValuePair("app", "app"));
        try {
            params.add(new BasicNameValuePair("partida", new String(usuario.getPlaceIdOrigen().getBytes(), "ISO-8859-1")));
            obtenerDestinoFinal(destFinal,destInt1,destInt2,destInt3,usuario.getPlaceIdDestino());
            params.add(new BasicNameValuePair("destInt1", new String(destInt1.getBytes(), "ISO-8859-1")));
            params.add(new BasicNameValuePair("destInt2", new String(destInt2.getBytes(), "ISO-8859-1")));
            params.add(new BasicNameValuePair("destInt3", new String(destInt3.getBytes(), "ISO-8859-1")));
            params.add(new BasicNameValuePair("destFinal", new String(destFinal.getBytes(), "ISO-8859-1")));
            params.add(new BasicNameValuePair("cliente", usuario.getCliente()));
            params.add(new BasicNameValuePair("usuario", usuario.getNombre()));
            JSONObject servicio = Utilidades.enviarPost(url, params);
            usuario.setIdViaje(servicio.getString("servicio_id"));
            Intent i = new Intent(context.get(), SolicitarViajeService.class);
            i.putExtra("idServicio",usuario.getIdViaje());
            context.get().startService(i);
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
        Usuario usuario = Usuario.getInstance();
        Usuario.getInstance().setBuscaServicio(false);
    }

    public void obtenerDestinoFinal(String destFinal,String destInt1,String destInt2,String destInt3,Map<String,String> destinos)
    {
        if(destinos.size() == 1)
        {
            destFinal = destinos.get("destino");
        }
        else if(destinos.size() == 2)
        {
            destInt1 = destinos.get("destino");
            destFinal = destinos.get("destino2");
        }
        else if(destinos.size() == 3)
        {
            destInt1 = destinos.get("destino");
            destInt2 =  destinos.get("destino2");
            destFinal = destinos.get("destino3");
        }
        else if(destinos.size() == 4)
        {
            destInt1 = destinos.get("destino");
            destInt2 =  destinos.get("destino2");
            destInt3 = destinos.get("destino3");
            destFinal = destinos.get("destino4");
        }
    }
}
