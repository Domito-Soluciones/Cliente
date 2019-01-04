package cl.domito.cliente.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.thread.AddressOperation;
import cl.domito.cliente.thread.AgregarServicioOperation;
import cl.domito.cliente.thread.DatosUsuarioOperation;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.thread.DetalleServicioOperation;
import cl.domito.cliente.thread.DirectionsOperation;
import cl.domito.cliente.thread.PlacesOperation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,LocationListener
{
    private GoogleMap mMap;
    private GoogleApiClient apiClient;
    private SupportMapFragment mapFragment;
    private ImageView imageButtonMenu;
    private ImageView imageButtonAtras;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView textViewInicio;
    private ImageView imageViewPoint;
    private ConstraintLayout constraintLayoutToolbar;
    private ConstraintLayout constrainLayoutIngresaViaje;
    private ConstraintLayout constrainLayoutInicioViaje;
    private ConstraintLayout constrainLayoutConfirmarViaje;
    private ConstraintLayout constrainLayoutPlaces;
    private Button buttonSolicitar;
    private Button buttonConfirmar;
    private TextView textViewDetalleOrigen;
    private TextView textViewDetalleDestino;
    private EditText editTextPartida;
    private EditText editTextDestino;
    private ImageView imageViewMas;
    private ProgressBar progressBar;
    private TextView textViewMapa;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private LocationManager locationManager;

    @Override
    protected void onResume() {
        super.onResume();
        if(mMap != null) {
            mMap.clear();
            constrainLayoutInicioViaje.setVisibility(View.VISIBLE);
            constrainLayoutConfirmarViaje.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imageButtonMenu = findViewById(R.id.imageViewMenu);
        imageButtonAtras = findViewById(R.id.imageViewAtras);
        navigationView = findViewById(R.id.nav_view);
        textViewInicio  = findViewById(R.id.textViewPartida);
        imageViewPoint = findViewById(R.id.imageViewPointer);
        buttonSolicitar = findViewById(R.id.buttonSolicitar);
        buttonConfirmar = findViewById(R.id.button);
        textViewDetalleOrigen = findViewById(R.id.detalleOrigenValor);
        textViewDetalleDestino = findViewById(R.id.detalleDestinoValor);
        editTextPartida = findViewById(R.id.editTextPartida);
        editTextDestino = findViewById(R.id.editTextDestino);
        imageViewMas = findViewById(R.id.imageViewMas);
        constraintLayoutToolbar = findViewById(R.id.constrainLayoutToolBar);
        constrainLayoutIngresaViaje = findViewById(R.id.constrainLayoutIngresaViaje);
        constrainLayoutInicioViaje = findViewById(R.id.constrainLayoutInicioViaje);
        constrainLayoutConfirmarViaje = findViewById(R.id.constrainLayoutConfirmarViaje);
        constrainLayoutPlaces = findViewById(R.id.constrainLayoutPlaces);
        progressBar = findViewById(R.id.progressBarGeneral);
        textViewMapa = findViewById(R.id.textViewMapa);
        textView1 = findViewById(R.id.textViewRes1);
        textView2 = findViewById(R.id.textViewRes2);
        textView3 = findViewById(R.id.textViewRes3);
        textView4 = findViewById(R.id.textViewRes4);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            DatosUsuarioOperation datosUsuarioOperation = new DatosUsuarioOperation(this);
        datosUsuarioOperation.execute();

        editTextPartida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_PLACES);
                    editTextPartida.clearFocus();
                    editTextPartida.requestFocus();
                }
            });
        editTextDestino.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_PLACES);
                editTextDestino.clearFocus();
                editTextDestino.requestFocus();
            }
        });

        editTextPartida.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                abrirNavegadorPlaces();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                obtenerPlaces(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextDestino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                abrirNavegadorPlaces();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                obtenerPlaces(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageViewMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarDestino();
            }
        });

        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMenuContextual();
            }
        });
        imageButtonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return getMenuContextual(menuItem);
            }
        });

        textViewInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirBuscadorServicios();
            }
        });

        buttonSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarViaje();
            }
        });

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarServicio();
            }
        });

        textViewMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarDestinoMapa(v);
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dibujarRutaDesdePlaces(v);
                solicitarViaje();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dibujarRutaDesdePlaces(v);
                solicitarViaje();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dibujarRutaDesdePlaces(v);
                solicitarViaje();
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dibujarRutaDesdePlaces(v);
                solicitarViaje();
            }
        });


        mapFragment.getMapAsync(this);
        navigationView.setItemIconTintList(null);
        Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_UBICACION);
        editTextPartida.setSelectAllOnFocus(true);
        editTextDestino.setSelectAllOnFocus(true);
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

    private void abrirBuscadorServicios()
    {
        Usuario.getInstance().setBuscaServicio(true);
        constraintLayoutToolbar.setVisibility(View.GONE);
        constrainLayoutIngresaViaje.setVisibility(View.VISIBLE);
        constrainLayoutInicioViaje.setVisibility(View.GONE);
        imageViewPoint.setVisibility(View.VISIBLE);
        Usuario usuario = Usuario.getInstance();
        AddressOperation addressOperation = new AddressOperation(this);
        addressOperation.execute(usuario.getLatitud()+"",usuario.getLongitud() + "",Usuario.BUSCAR_PARTIDA+"");
    }

    private void volver()
    {
        Usuario.getInstance().setBuscaServicio(false);
        constraintLayoutToolbar.setVisibility(View.VISIBLE);
        constrainLayoutIngresaViaje.setVisibility(View.GONE);
        constrainLayoutPlaces.setVisibility(View.GONE);
        constrainLayoutInicioViaje.setVisibility(View.VISIBLE);
        imageViewPoint.setVisibility(View.GONE);
    }

    private boolean getMenuContextual(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.servicio) {
            Intent mainIntent = new Intent(this,ServicioActivity.class);
            this.startActivity(mainIntent);
        }
        if (id == R.id.salir) {
            ActivityUtils.eliminarSharedPreferences(getSharedPreferences("preferencias",Context.MODE_PRIVATE),"idUsuario");
            Usuario.getInstance().setActivo(false);
            Intent mainIntent = new Intent(this,LoginActivity.class);
            this.startActivity(mainIntent);
            this.finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style));
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                agregarPuntoDestino();
            }
        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

            }
        });

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage((FragmentActivity) this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void agregarPuntoDestino() {
        ActivityUtils.hideSoftKeyBoard(this);
        if(Usuario.getInstance().isBuscaServicio())
        {
            String latitud = mMap.getCameraPosition().target.latitude+"";
            String longitud = mMap.getCameraPosition().target.longitude+"";
            AddressOperation addressOperation = new AddressOperation(this);
            if(editTextPartida.isFocused())
            {
                addressOperation.execute(latitud,longitud,Usuario.BUSCAR_PARTIDA+"");
            }
            else
            {
                addressOperation.execute(latitud,longitud,Usuario.BUSCAR_DESTINO+"");
            }
        }
    }

    private void solicitarViaje() {
        String partida = Usuario.getInstance().getPlaceIdOrigenNombre();
        String destino = Usuario.getInstance().getPlaceIdDestinoNombre();
        DirectionsOperation directionsOperation = new DirectionsOperation(this);
        directionsOperation.execute(mMap,partida,new String[]{destino});
    }

    private void agregarServicio() {
        AgregarServicioOperation agregarServicioOperation = new AgregarServicioOperation(this);
        agregarServicioOperation.execute();
        DetalleServicioOperation detalleServicioOperation = new DetalleServicioOperation(this);
        detalleServicioOperation.execute();
    }

    private void obtenerPlaces(String input) {
        Usuario usuario = Usuario.getInstance();
        if(usuario.getTipoBusqueda() == Usuario.SELECCIONAR_PLACES) {
            PlacesOperation placesOperation = new PlacesOperation(this);
            placesOperation.execute(input);
        }
    }

    private void seleccionarDestinoMapa(View view)
    {
        TextView editText = (TextView) view;
        Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_UBICACION);
        constrainLayoutPlaces.setVisibility(View.GONE);
    }

    private void abrirNavegadorPlaces()
    {
        Usuario usuario = Usuario.getInstance();
        if(usuario.getTipoBusqueda() == Usuario.SELECCIONAR_PLACES) {
            if(!usuario.isBusquedaRealizada()) {
                constrainLayoutPlaces.setVisibility(View.VISIBLE);
            }
        }
    }

    private void autoCompletarPlace(String texto,String tag,View v) {
        Usuario usuario = Usuario.getInstance();
        if(editTextPartida.isFocused())
        {
            editTextPartida.setText(texto);
            usuario.setPlaceIdOrigen(tag);
            usuario.setPlaceIdOrigenNombre(texto);
        }
        else if(editTextDestino.isFocused())
        {
            editTextDestino.setText(texto);
            usuario.setPlaceIdDestino(tag);
            usuario.setPlaceIdDestinoNombre(texto);
        }

    }

    private void dibujarRutaDesdePlaces(View v)
    {
        Usuario usuario = Usuario.getInstance();
        TextView textView = (TextView) v;
        if(editTextPartida.isFocused())
        {
            autoCompletarPlace(textView.getText().toString(),usuario.getPlaceIdOrigen(),v);
        }
        else if(editTextDestino.isFocused())
        {
            autoCompletarPlace(textView.getText().toString(),usuario.getPlaceIdDestino(),v);
        }
        constrainLayoutPlaces.setVisibility(View.GONE);
        ActivityUtils.hideSoftKeyBoard(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERROR", "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},0);
            return;
        }
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) getApplicationContext().getSystemService(this.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, this);
        Location lastLocation =
                LocationServices.FusedLocationApi.getLastLocation(apiClient);

        ActivityUtils.updateUI(this,mMap,lastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("","");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Activar Ubicación");
        dialog.setMessage("El acceso a la ubicacion le permite tener una mejor experiencia,¿Desea activar la ubicación?");
        dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirVentanaUbicacion();
            }
        });
        dialog.setNegativeButton(android.R.string.no, null);
        AlertDialog alertDialog = dialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(this.getResources().getColor(R.color.colorPrimary));
    }

    private void abrirVentanaUbicacion() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivity(intent);
    }
    private void agregarDestino() {
        EditText editText = new EditText(this);
        constrainLayoutIngresaViaje.addView(editText);
    }

}

