package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;

public class PlacesOperation extends AsyncTask<String, Void, Void> {

    private WeakReference<MapsActivity> context;
    private TextView textViewError;

    public PlacesOperation(MapsActivity activity) {
        context = new WeakReference<MapsActivity>(activity);
    }

    @Override
    protected Void doInBackground(String... strings) {
        JSONArray places = ActivityUtils.getPlaces(context.get(),strings[0],strings[1]);
        context.get().runOnUiThread(ActivityUtils.mensajeError(context.get()));
        for(int i = 0 ; i < places.length() ; i++)
        {
            try {
                JsonObject object =(JsonObject) places.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
