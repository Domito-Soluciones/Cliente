package cl.domito.cliente.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.service.SolicitarViajeService;
import cl.domito.cliente.thread.AddressOperation;
import cl.domito.cliente.thread.AgregarServicioOperation;
import cl.domito.cliente.thread.CancelarViajeOperation;
import cl.domito.cliente.thread.DatosUsuarioOperation;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.thread.DetalleServicioOperation;
import cl.domito.cliente.thread.DirectionsOperation;
import cl.domito.cliente.thread.PlacesOperation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,LocationListener {
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
    private ConstraintLayout constraintLayoutIngresaViaje;
    private ConstraintLayout constrainLayoutInicioViaje;
    private ConstraintLayout constrainLayoutConfirmarViaje;
    private ConstraintLayout constrainLayoutPlaces;
    private ConstraintLayout constrainLayoutConductor;
    private Button buttonSolicitar;
    private Button buttonConfirmar;
    private TextView textViewDetalleOrigen;
    private TextView textViewDetalleDestino;
    private EditText editTextPartida;
    private EditText editTextDestino;
    private EditText editTextDestino2;
    private EditText editTextDestino3;
    private EditText editTextDestino4;
    private ImageView imageViewMas;
    private ImageView imageViewMenos;
    private ProgressBar progressBar;
    private TextView textViewMapa;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private LocationManager locationManager;
    private ImageView imageViewLlamar;
    private TextView textViewPatente;
    private TextView textViewConductor;
    private Button buttonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imageButtonMenu = findViewById(R.id.imageViewMenu);
        imageButtonAtras = findViewById(R.id.imageViewAtras);
        navigationView = findViewById(R.id.nav_view);
        textViewInicio = findViewById(R.id.textViewPartida);
        imageViewPoint = findViewById(R.id.imageViewPointer);
        buttonSolicitar = findViewById(R.id.buttonSolicitar);
        buttonConfirmar = findViewById(R.id.button);
        textViewDetalleOrigen = findViewById(R.id.detalleOrigenValor);
        textViewDetalleDestino = findViewById(R.id.detalleDestinoValor);
        editTextPartida = findViewById(R.id.editTextPartida);
        editTextDestino = findViewById(R.id.editTextDestino);
        editTextDestino2 = findViewById(R.id.editTextDestino2);
        editTextDestino3 = findViewById(R.id.editTextDestino3);
        editTextDestino4 = findViewById(R.id.editTextDestino4);
        imageViewMas = findViewById(R.id.imageViewMas);
        imageViewMenos = findViewById(R.id.imageViewMenos);
        constraintLayoutToolbar = findViewById(R.id.constrainLayoutToolBar);
        constraintLayoutIngresaViaje = findViewById(R.id.constrainLayoutIngresaViaje);
        constrainLayoutInicioViaje = findViewById(R.id.constrainLayoutInicioViaje);
        constrainLayoutConfirmarViaje = findViewById(R.id.constrainLayoutConfirmarViaje);
        constrainLayoutPlaces = findViewById(R.id.constrainLayoutPlaces);
        constrainLayoutConductor = findViewById(R.id.constrainLayoutConductor);
        progressBar = findViewById(R.id.progressBarGeneral);
        textViewMapa = findViewById(R.id.textViewMapa);
        textView1 = findViewById(R.id.textViewRes1);
        textView2 = findViewById(R.id.textViewRes2);
        textView3 = findViewById(R.id.textViewRes3);
        textView4 = findViewById(R.id.textViewRes4);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        imageViewLlamar = findViewById(R.id.imageViewLlamar);
        textViewPatente = findViewById(R.id.textViewPatente);
        textViewConductor = findViewById(R.id.textViewConductor);
        buttonCancelar = findViewById(R.id.buttonCancelar);
        DatosUsuarioOperation datosUsuarioOperation = new DatosUsuarioOperation(this);
        datosUsuarioOperation.execute();

        editTextPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_PLACES);
                Usuario.getInstance().setEditTextCompletar(v);
                editTextPartida.clearFocus();
                editTextPartida.requestFocus();
            }
        });
        editTextDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_PLACES);
                Usuario.getInstance().setEditTextCompletar(v);
                editTextDestino.clearFocus();
                editTextDestino.requestFocus();
            }
        });

        editTextDestino2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_PLACES);
                Usuario.getInstance().setEditTextCompletar(v);
                editTextDestino.clearFocus();
                editTextDestino.requestFocus();
            }
        });

        editTextDestino3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_PLACES);
                Usuario.getInstance().setEditTextCompletar(v);
                editTextDestino.clearFocus();
                editTextDestino.requestFocus();
            }
        });

        editTextDestino4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario.getInstance().setTipoBusqueda(Usuario.SELECCIONAR_PLACES);
                Usuario.getInstance().setEditTextCompletar(v);
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

        editTextDestino2.addTextChangedListener(new TextWatcher() {
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

        editTextDestino3.addTextChangedListener(new TextWatcher() {
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

        editTextDestino4.addTextChangedListener(new TextWatcher() {
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

        imageViewMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitarDestino();
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

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelarViajeOperation cancelarViajeOperation = new CancelarViajeOperation();
                cancelarViajeOperation.execute();
                Usuario.getInstance().setEnProceso(false);
                constrainLayoutConductor.setVisibility(View.GONE);
            }
        });

        imageViewLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    llamar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void llamar()
    {
        String numero = null;
        try {
            numero = Usuario.getInstance().getDatosConductor().getJSONObject("movil_conductor").getString("conductor_celular");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ActivityUtils.llamar(this,numero);
    }

    private void abrirBuscadorServicios()
    {
        Usuario.getInstance().setBuscaServicio(true);
        constraintLayoutToolbar.setVisibility(View.GONE);
        constraintLayoutIngresaViaje.setVisibility(View.VISIBLE);
        constraintLayoutIngresaViaje.bringToFront();
        constrainLayoutInicioViaje.setVisibility(View.GONE);
        imageViewPoint.setVisibility(View.VISIBLE);
        Usuario usuario = Usuario.getInstance();
        AddressOperation addressOperation = new AddressOperation(this);
        addressOperation.execute(usuario.getLatitud()+"",usuario.getLongitud() + "");
    }

    private void volver()
    {
        Usuario.getInstance().setBuscaServicio(false);
        constraintLayoutToolbar.setVisibility(View.VISIBLE);
        constraintLayoutIngresaViaje.setVisibility(View.GONE);
        constrainLayoutPlaces.setVisibility(View.GONE);
        constrainLayoutInicioViaje.setVisibility(View.VISIBLE);
        imageViewPoint.setVisibility(View.GONE);
        editTextDestino2.setVisibility(View.INVISIBLE);
        editTextDestino3.setVisibility(View.INVISIBLE);
        editTextDestino4.setVisibility(View.INVISIBLE);
        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) constraintLayoutIngresaViaje.getLayoutParams();
        newLayoutParams.height = ActivityUtils.dpToPx(120,this);
        constraintLayoutIngresaViaje.setLayoutParams(newLayoutParams);
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
                addressOperation.execute(latitud,longitud);
            }
            else
            {
                addressOperation.execute(latitud,longitud);
            }
        }
    }

    private void solicitarViaje() {
        String partida = Usuario.getInstance().getPlaceIdOrigen();
        List<String> destinos = Usuario.getInstance().getPlaceIdDestino();
        DirectionsOperation directionsOperation = new DirectionsOperation(this);
        directionsOperation.execute(mMap,partida,destinos);
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
            usuario.setPlaceIdOrigen(texto);
        }
        else
        {
            editTextDestino.setText(texto);
           // usuario.setPlaceIdDestino(texto);
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
            autoCompletarPlace(textView.getText().toString(),usuario.getPlaceIdDestino().get(usuario.getCantidadDestinos()-1),v);
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
        Usuario.getInstance().setUbicacion(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()));

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
    protected void onResume() {
        Usuario usuario = Usuario.getInstance();
        super.onResume();
        if(mMap != null) {
            //mMap.clear();
            if(Usuario.getInstance().isEnProceso())
            {
                constrainLayoutInicioViaje.setVisibility(View.GONE);
                mMap.clear();
                LatLng ubicacionUsuario = usuario.getUbicacion();
                LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
                latLngBounds.include(usuario.getUbicacionConductor()).include(ubicacionUsuario);
                mMap.addMarker(new MarkerOptions().position(usuario.getUbicacionConductor()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 300));
                JSONObject conductor = Usuario.getInstance().getDatosConductor();
                try {
                    String patente = conductor.getString("movil_patente");
                    String marca = conductor.getString("movil_marca");
                    String modelo = conductor.getString("movil_modelo");
                    JSONObject datosConductor = conductor.getJSONObject("movil_conductor");
                    String nombre = datosConductor.getString("conductor_nombre") + " " + datosConductor.getString("conductor_papellido");
                    String celular = datosConductor.getString("conductor_celular");
                    textViewPatente.setText(patente+" " + marca +" " + modelo);
                    textViewConductor.setText(nombre+" " + celular);
                }
                catch(JSONException e)
                {
                    e.printStackTrace();

                }
                constrainLayoutConductor.setVisibility(View.VISIBLE);
            }
            else
            {
                constrainLayoutInicioViaje.setVisibility(View.VISIBLE);
            }
            constrainLayoutConfirmarViaje.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        Usuario usuario = Usuario.getInstance();
        usuario.setCantidadDestinos(usuario.getCantidadDestinos()+1);
        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) constraintLayoutIngresaViaje.getLayoutParams();
        if(usuario.getCantidadDestinos() == 2)
        {
            editTextDestino2.setVisibility(View.VISIBLE);
            newLayoutParams.height = ActivityUtils.dpToPx(160,this);
        }
        else if(usuario.getCantidadDestinos() == 3)
        {
            editTextDestino3.setVisibility(View.VISIBLE);
            newLayoutParams.height = ActivityUtils.dpToPx(200,this);
        }
        else if(usuario.getCantidadDestinos() == 4)
        {
            editTextDestino4.setVisibility(View.VISIBLE);
            newLayoutParams.height = ActivityUtils.dpToPx(250,this);
        }
        constraintLayoutIngresaViaje.setLayoutParams(newLayoutParams);

    }

    private void quitarDestino() {
        Usuario usuario = Usuario.getInstance();
        usuario.setCantidadDestinos(usuario.getCantidadDestinos()-1);
        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) constraintLayoutIngresaViaje.getLayoutParams();
        if(usuario.getCantidadDestinos() == 4)
        {
            editTextDestino2.setVisibility(View.INVISIBLE);
            newLayoutParams.height = ActivityUtils.dpToPx(200,this);
        }
        else if(usuario.getCantidadDestinos() == 3)
        {
            editTextDestino3.setVisibility(View.INVISIBLE);
            newLayoutParams.height = ActivityUtils.dpToPx(160,this);
        }
        else if(usuario.getCantidadDestinos() == 2)
        {
            editTextDestino4.setVisibility(View.INVISIBLE);
            newLayoutParams.height = ActivityUtils.dpToPx(120,this);
        }
        constraintLayoutIngresaViaje.setLayoutParams(newLayoutParams);

    }


    private void crearMarcador()
    {
        LatLng sydney = new LatLng(-33.852, 151.211);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



}

