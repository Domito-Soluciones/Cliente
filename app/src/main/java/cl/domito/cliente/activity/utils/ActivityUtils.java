package cl.domito.cliente.activity.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.LoginActivity;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.dominio.Usuario;

public class ActivityUtils {

    public static String URL_GEOCODER =
            "https://maps.googleapis.com/maps/api/geocode/json?";
    public static String URL_PLACES =
            "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

    public static void hideSoftKeyBoard(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getAddress(Activity activity,String latitud,String longitud,String tipo) {
        String addressComponents = "";
        try {
            String url = URL_GEOCODER + "latlng="+latitud+","+longitud+"&sensor=true&key="+activity.getString(R.string.api_key);
            JSONObject json = Utilidades.obtenerJsonObject(url);
            String status = json.getString("status");
            if (status.equalsIgnoreCase("OK")) {
                JSONArray results = json.getJSONArray("results");
                JSONObject zero = results.getJSONObject(0);
                addressComponents = zero.getString("formatted_address");
                String placeId = zero.getString("place_id");
                if(tipo.equals(Usuario.BUSCAR_PARTIDA))
                {
                    Usuario.getInstance().setPlaceIdOrigen(placeId);
                    Usuario.getInstance().setPlaceIdOrigenNombre(addressComponents);
                }
                else if(tipo.equals(Usuario.BUSCAR_DESTINO))
                {
                    Usuario.getInstance().setPlaceIdDestino(placeId);
                    Usuario.getInstance().setPlaceIdDestinoNombre(addressComponents);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressComponents;
    }

    public static JSONArray getPlaces(Activity activity, String latitud, String longitud,String input) {
        JSONArray results = null;
        try {
            String url = URL_PLACES + "input="+URLEncoder.encode(input, "utf8")+"&location="+latitud+","+longitud+"&sensor=true&radius=1000&key="+activity.getString(R.string.api_key);
            JSONObject json = Utilidades.obtenerJsonObject(url);
            String status = json.getString("status");
            if (status.equalsIgnoreCase("OK")) {
                results = json.getJSONArray("predictions");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void updateUI(Activity activity,GoogleMap googleMap,Location loc) {
        if (loc != null) {
            Usuario.getInstance().setLatitud(loc.getLatitude());
            Usuario.getInstance().setLongitud(loc.getLongitude());
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            Toast.makeText(activity, "mal", Toast.LENGTH_LONG);
        }
    }

    public static void guardarSharedPreferences(SharedPreferences sharedPreferences,String llave,String valor)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(llave, valor);
        editor.commit();
    }

    public static void eliminarSharedPreferences(SharedPreferences sharedPreferences,String key)
    {
        sharedPreferences.edit().remove(key).commit();
    }

    public static void enviarNotificacion(Activity activity,String titulo,String contenido,int smallIcon)
    {
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(activity, MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(activity)
                .setContentIntent(pendingIntent)
                .setContentTitle(titulo)
                .setSmallIcon(smallIcon)
                .setContentText(contenido)
                .setVibrate(new long[]{100, 250, 100, 500})
                .setAutoCancel(true)
                .setSound(soundUri);
        mNotifyMgr.notify(1, mBuilder.build());
    }

    public static void dibujarRuta(Activity activity,GoogleMap mMap,String origen,String destino) {
        List<LatLng> path = new ArrayList();
        GeoApiContext context = new GeoApiContext.Builder().apiKey(activity.getString(R.string.api_key)).build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, "place_id:"+origen, "place_id:"+destino);
        try {
            DirectionsResult res = req.await();
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("TAG", ex.getLocalizedMessage());
        }
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLACK).width(15);
            mMap.addPolyline(opts);
        }

        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        latLngBounds.include(path.get(0)).include(path.get(path.size()-1));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(),300));
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context,vectorDrawableResourceId);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static Runnable mensajeError(Activity activity)
    {
        TextView textViewError = null;
        String nombre = activity.getComponentName().getClassName();
        if(nombre.equals("cl.domito.cliente.activity.MapsActivity"))
        {
            textViewError = activity.findViewById(R.id.textViewError);
        }
        else if(nombre.equals("cl.domito.cliente.activity.LoginActivity"))
        {
            textViewError = activity.findViewById(R.id.textViewError2);
        }
        TextView finalTextViewError = textViewError;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(Usuario.getInstance().isConectado())
                {
                    finalTextViewError.setVisibility(View.GONE);
                }
                else if(!Usuario.getInstance().isConectado())
                {
                    finalTextViewError.setVisibility(View.VISIBLE);
                }
            }
        };
        return runnable;
    }
}
