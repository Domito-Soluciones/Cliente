package cl.domito.cliente.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import cl.domito.cliente.R;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.usuario.MyButtonClickListener;
import cl.domito.cliente.usuario.MyMapReadyCallBack;
import cl.domito.cliente.usuario.Usuario;

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

    private ImageView imageButton;
    private NavigationView  navigationView;
    private DrawerLayout drawerLayout;
    private TextView textViewInicio;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** INICIALIZACION ACTIVITY **/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /** INICIALIZACION VIEW **/
        imageButton = findViewById(R.id.imageViewMenu);
        navigationView = findViewById(R.id.nav_view);
        textViewInicio  = findViewById(R.id.textViewPartida);
        /** INICIALIZACION EVENTO BOTON DE MENU **/
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMenuContextual();
            }
        });
        /** INICIALIZACION DE PROPIEDADES **/
        navigationView.setItemIconTintList(null);
        /** INICIALIZACION EVENTO MENU DE NAVEGACION **/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return getMenuContextual(menuItem);
            }
        });
        /** INICIALIZACION EVENTO TEXTVIEW PARTIDA **/
        textViewInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MyMapReadyCallBack(this));
        //LatLng southwestLatLng = new LatLng(-33.645081, -70.890807);
        //LatLng northeastLatLng = new LatLng(-33.221453, -70.425000);
        //PlaceAutocompleteFragment autocompleteOrigen = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_origen);
        //autocompleteOrigen.setBoundsBias(new LatLngBounds(southwestLatLng, northeastLatLng));
        //PlaceAutocompleteFragment autocompleteDestino = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_destino);
        //autocompleteDestino.setBoundsBias(new LatLngBounds(southwestLatLng, northeastLatLng));
        //EditText editTextOrigen = ((EditText)autocompleteOrigen.getView().findViewById(R.id.place_autocomplete_search_input));
        //EditText editTextDestino = ((EditText)autocompleteDestino.getView().findViewById(R.id.place_autocomplete_search_input));
        //editTextOrigen.setTextSize(15.0f);
        //editTextOrigen.setHint(getString(R.string.origen));
        //editTextDestino.setTextSize(15.0f);
        //editTextDestino.setHint(getString(R.string.destino));

        MyButtonClickListener myButtonClickListener = new MyButtonClickListener(this);

        //Button button1 = findViewById(R.id.button);
        //button1.setOnClickListener(myButtonClickListener);
        //Button button2 = findViewById(R.id.button2);
        //button2.setOnClickListener(myButtonClickListener);
        final String url = Utilidades.URL_BASE_USUARIO + "NombreUsuario.php?nick="+Usuario.getInstance().getId();
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


    private void abrirMenuContextual() {
        drawerLayout = this.findViewById(R.id.drawer_layout);
        navigationView = this.findViewById(R.id.nav_view);
        drawerLayout.openDrawer(Gravity.LEFT);
        navigationView.bringToFront();
        drawerLayout.requestLayout();
    }

    private boolean getMenuContextual(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.servicio) {
            Intent mainIntent = new Intent(this,ServicioActivity.class);
            this.startActivity(mainIntent);
        }
        if (id == R.id.salir) {
            Usuario.getInstance().setActivo(false);
            Intent mainIntent = new Intent(this,LoginActivity.class);
            this.startActivity(mainIntent);
            this.finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
