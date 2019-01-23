package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.http.Utilidades;

public class DetalleServicioOperation extends AsyncTask<Void, Void, Void> {


    private WeakReference<MapsActivity> context;

    public DetalleServicioOperation(MapsActivity activity) {
        context = new WeakReference<MapsActivity>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
    List<LatLng> lista = Usuario.getInstance().getLatLngs();
    StringBuilder lat = new StringBuilder();
    StringBuilder lon = new StringBuilder();
    for(int i = 0 ; i < lista.size() ; i++)
    {
        LatLng latLng = lista.get(i);
        lat.append(latLng.latitude);
        lat.append(",");
        lon.append(latLng.longitude);
        lon.append(",");
    }
    String url = Utilidades.URL_BASE_SERVICIO + "AddServicioDetalle.php";
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("lat", lat.toString()));
    params.add(new BasicNameValuePair("lon", lon.toString()));
    params.add(new BasicNameValuePair("id",Usuario.getInstance().getIdViaje()));
    Utilidades.enviarPost(url,params);
    return null;
}

}
