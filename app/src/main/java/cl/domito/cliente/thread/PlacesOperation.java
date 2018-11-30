package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;

public class PlacesOperation extends AsyncTask<String, Void, Void> {

    private WeakReference<MapsActivity> context;
    ConstraintLayout constraintLayout;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    EditText editTextPartida;
    EditText editTextDestino;

    public PlacesOperation(MapsActivity activity) {
        context = new WeakReference<MapsActivity>(activity);
    }

    @Override
    protected Void doInBackground(String... strings) {
        String latitud = Usuario.getInstance().getLatitud() + "";
        String longitud = Usuario.getInstance().getLongitud() + "";
        MapsActivity mapsActivity = context.get();
        constraintLayout = mapsActivity.findViewById(R.id.constrainLayoutPlaces);
        textView1 = mapsActivity.findViewById(R.id.textViewRes1);
        textView2 = mapsActivity.findViewById(R.id.textViewRes2);
        textView3 = mapsActivity.findViewById(R.id.textViewRes3);
        textView4 = mapsActivity.findViewById(R.id.textViewRes4);
        editTextPartida = mapsActivity.findViewById(R.id.editTextPartida);
        editTextDestino = mapsActivity.findViewById(R.id.editTextDestino);
        JSONArray jsonArray = ActivityUtils.getPlaces(mapsActivity, latitud, longitud, strings[0]);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (i < 4) {
                        int finalI = i;
                        mapsActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String place = jsonObject.getString("description");
                                    String placeId = jsonObject.getString("place_id");
                                    Usuario usuario = Usuario.getInstance();
                                    switch (finalI) {
                                        case 0:
                                            textView1.setVisibility(View.VISIBLE);
                                            textView1.setText(place);
                                            break;
                                        case 1:
                                            textView2.setVisibility(View.VISIBLE);
                                            textView2.setText(place);
                                            break;
                                        case 2:
                                            textView3.setVisibility(View.VISIBLE);
                                            textView3.setText(place);
                                            break;
                                        case 3:
                                            textView4.setVisibility(View.VISIBLE);
                                            textView4.setText(place);
                                            break;
                                    }
                                    if(editTextPartida.isFocused())
                                    {
                                        usuario.setPlaceIdOrigen(placeId);
                                        usuario.setPlaceIdOrigenNombre(place);
                                    }
                                    else
                                    {
                                        usuario.setPlaceIdDestino(placeId);
                                        usuario.setPlaceIdDestinoNombre(place);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
