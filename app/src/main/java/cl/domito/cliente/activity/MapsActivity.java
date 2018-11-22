package cl.domito.cliente.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import cl.domito.cliente.R;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.listener.MyButtonClickListener;
import cl.domito.cliente.listener.MyImageButtonClickListener;
import cl.domito.cliente.listener.MyMapReadyCallBack;
import cl.domito.cliente.listener.MyNavigationItemSelectedListener;

public class MapsActivity extends FragmentActivity   {

    public static GoogleMap mMap;
    public static MapsActivity mapsActivity;
    public static View servicioLayout;
    public static TextView textViewNServicio;
    public static TextView textViewOrigen;
    public static TextView textViewDestino;
    public static TextView textViewTipo;
    public static TextView textViewNombre;
    public static TextView textViewDireccion;
    public static TextView textViewCelular;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Utilidades.CONTEXT = getApplicationContext();
        mapsActivity = this;

        ImageView imageButton = findViewById(R.id.imageView2);
        MyImageButtonClickListener myImageButtonClickListener = new MyImageButtonClickListener(this);
        imageButton.setOnClickListener(myImageButtonClickListener);

        NavigationView  navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyNavigationItemSelectedListener(this));
        navigationView.setItemIconTintList(null);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MyMapReadyCallBack(this));
        LatLng southwestLatLng = new LatLng(-33.645081, -70.890807);
        LatLng northeastLatLng = new LatLng(-33.221453, -70.425000);
        PlaceAutocompleteFragment autocompleteOrigen = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_origen);
        autocompleteOrigen.setBoundsBias(new LatLngBounds(southwestLatLng, northeastLatLng));
        PlaceAutocompleteFragment autocompleteDestino = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_destino);
        autocompleteDestino.setBoundsBias(new LatLngBounds(southwestLatLng, northeastLatLng));
        EditText editTextOrigen = ((EditText)autocompleteOrigen.getView().findViewById(R.id.place_autocomplete_search_input));
        EditText editTextDestino = ((EditText)autocompleteDestino.getView().findViewById(R.id.place_autocomplete_search_input));
        editTextOrigen.setTextSize(15.0f);
        editTextOrigen.setHint(getString(R.string.origen));
        editTextDestino.setTextSize(15.0f);
        editTextDestino.setHint(getString(R.string.destino));

        MyButtonClickListener myButtonClickListener = new MyButtonClickListener(this);

        Button button1 = findViewById(R.id.button);
        button1.setOnClickListener(myButtonClickListener);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(myButtonClickListener);
        final String url = Utilidades.URL_BASE_USUARIO + "NombreUsuario.php?nick="+Utilidades.USER;
        final JSONObject[] jsonObject = {null};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonObject[0] = RequestUsuario.datosUsuario(url);
                    Utilidades.NOMBRE = jsonObject[0].getString("nombre");
                    Utilidades.CLIENTE = jsonObject[0].getString("cliente");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
