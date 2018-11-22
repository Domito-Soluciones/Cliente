package cl.domito.cliente.usuario;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.MapsActivity;

/**
 * Created by elsan on 01-05-2018.
 */

public class MyMapReadyCallBack implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    Activity activity;
    GoogleApiClient apiClient;
    public static String placeOrigin = null;
    public static String placeDestiny = null;
    public static String placeOriginID = null;
    public static String placeDestinyID = null;
    public static LatLng a = null;
    public static LatLng b = null;

    public MyMapReadyCallBack(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsActivity.mMap = googleMap;
        MapsActivity.mMap.getUiSettings().setMyLocationButtonEnabled(false);
        boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this.activity, R.raw.map_style));

        apiClient = new GoogleApiClient.Builder(this.activity)
                .enableAutoManage((FragmentActivity) this.activity, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

       /* PlaceAutocompleteFragment autocompleteOrigen = (PlaceAutocompleteFragment)
                this.activity.getFragmentManager().findFragmentById(R.id.place_autocomplete_origen);

        autocompleteOrigen.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                placeOrigin = place.getAddress().toString();
                placeOriginID = place.getId();
                Log.i("TAG", "Place: " + place.getName());
                LatLng latLng = place.getLatLng();
                a = latLng;
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                MapsActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                MapsActivity.mMap.addMarker(markerOptions)
                //.setIcon(bitmapDescriptorFromVector(activity.getApplicationContext(),R.drawable.inicio))
                ;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        PlaceAutocompleteFragment autocompleteDestino = (PlaceAutocompleteFragment)
                this.activity.getFragmentManager().findFragmentById(R.id.place_autocomplete_destino);

        autocompleteDestino.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placeDestiny = place.getAddress().toString();
                placeDestinyID = place.getId();
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName());
                LatLng latLng = place.getLatLng();
                b = latLng;
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                MapsActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                MapsActivity.mMap.addMarker(markerOptions)
                //.setIcon(bitmapDescriptorFromVector(activity.getApplicationContext(),R.drawable.fin))
                ;
                ActivityUtils.dibujarRuta(placeOrigin,placeDestiny);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });

*/
    }


    private void updateUI(Location loc) {
        if (loc != null) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            MapsActivity.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng);
            MapsActivity.mMap.addMarker(markerOptions).setTitle("Ubicacion");

        } else {
            Toast.makeText(this.activity, "mal", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERROR", "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation =
                LocationServices.FusedLocationApi.getLastLocation(apiClient);

        updateUI(lastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
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
}
