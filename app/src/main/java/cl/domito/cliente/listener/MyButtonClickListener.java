package cl.domito.cliente.listener;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.http.Utilidades;

public class MyButtonClickListener implements View.OnClickListener {

    Activity activity;

    public MyButtonClickListener(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button)
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = Utilidades.URL_BASE_SERVICIO + "AddServicio.php";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("app", "app"));
                    params.add(new BasicNameValuePair("partida", MyMapReadyCallBack.placeOrigin));
                    params.add(new BasicNameValuePair("partidaId", MyMapReadyCallBack.placeOriginID));
                    params.add(new BasicNameValuePair("destino", MyMapReadyCallBack.placeDestiny));
                    params.add(new BasicNameValuePair("destinoId", MyMapReadyCallBack.placeDestinyID));
                    params.add(new BasicNameValuePair("cliente", Utilidades.CLIENTE));
                    params.add(new BasicNameValuePair("usuario", Utilidades.USER));
                    Utilidades.enviarPost(url,params);
                }
            });
            thread.start();
            ConstraintLayout layout = activity.findViewById(R.id.relativeLayout4);
            layout.setVisibility(View.GONE);
        }
        else if(v.getId() == R.id.button2)
        {

        }
    }
}
