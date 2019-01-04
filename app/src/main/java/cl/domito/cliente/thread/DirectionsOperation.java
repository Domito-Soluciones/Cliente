package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.dominio.Usuario;

public class DirectionsOperation extends AsyncTask<Object, Void, Void> {


    private WeakReference<MapsActivity> context;
    MapsActivity mapsActivity;
    private ProgressBar progressBar;
    private TextView textViewDetalleOrigen;
    private TextView textViewDetalleDestino;
    private ConstraintLayout constrainLayoutConfirmarViaje;

    public DirectionsOperation(MapsActivity activity) {
        context = new WeakReference<MapsActivity>(activity);
        mapsActivity = context.get();
        progressBar = mapsActivity.findViewById(R.id.progressBarGeneral);
        textViewDetalleOrigen = mapsActivity.findViewById(R.id.detalleOrigenValor);
        textViewDetalleDestino = mapsActivity.findViewById(R.id.detalleDestinoValor);
        constrainLayoutConfirmarViaje = mapsActivity.findViewById(R.id.constrainLayoutConfirmarViaje);
    }

    @Override
    protected Void doInBackground(Object... objects) {

        Button buttonSolicitar = mapsActivity.findViewById(R.id.buttonSolicitar);
        ImageView imageViewPoint = mapsActivity.findViewById(R.id.imageViewPointer);
        ConstraintLayout constraintLayoutToolbar = mapsActivity.findViewById(R.id.constrainLayoutToolBar);
        ConstraintLayout constrainLayoutIngresaViaje = mapsActivity.findViewById(R.id.constrainLayoutIngresaViaje);
        context.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                Usuario.getInstance().setBuscaServicio(false);
                Usuario.getInstance().setBusquedaRealizada(true);
                buttonSolicitar.setVisibility(View.GONE);
                imageViewPoint.setVisibility(View.GONE);
                constraintLayoutToolbar.setVisibility(View.VISIBLE);
                constrainLayoutIngresaViaje.setVisibility(View.GONE);
            }
        });
        GoogleMap mMap = (GoogleMap) objects[0];
        String partida = (String) objects[1];
        String[] destinos = (String[]) objects[2];
        List<LatLng> latLngs = ActivityUtils.getDirections(context.get(),mMap,partida,destinos);
        Usuario.getInstance().setLatLngs(latLngs);
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        mapsActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    textViewDetalleOrigen.setText(Usuario.getInstance().getPlaceIdOrigenNombre());
                    textViewDetalleDestino.setText(Usuario.getInstance().getPlaceIdDestinoNombre());
                    constrainLayoutConfirmarViaje.setVisibility(View.VISIBLE);
                }
            }
        );
    }

}
