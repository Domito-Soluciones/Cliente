package cl.domito.cliente.thread;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;

public  class AsignacionThread extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        String url = Utilidades.URL_BASE_SERVICIO + "ServiciosAsignado.php?user="+Utilidades.USER;
        String urlDes = Utilidades.URL_BASE_SERVICIO + "DesAsignarServicio.php";
        while(true) {
            if(Utilidades.USUARIO_ACTIVO)
            {
                Log.i("EJECUTANDO THREAD","EJECUTANDO THREAD");
                try {

                    JSONObject servicio = RequestUsuario.obtenerServicioAsignado(url);
                    if(!servicio.getString("servicio_id").equals("")) {
                        Log.i("Servicio asignado",servicio.getString("servicio_id"));
                        if(Utilidades.TIEMPO_ESPERA == 0)
                        {
                            RequestUsuario.desAsignarServicio(urlDes,servicio.getString("servicio_id"));
                            MapsActivity.mapsActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MapsActivity.servicioLayout.setVisibility(View.GONE);
                                    Utilidades.GONE = true;
                                }
                            });
                            Utilidades.TIEMPO_ESPERA = 30;
                        }
                        else {
                            ActivityUtils.enviarNotificacion();
                            if(Utilidades.GONE) {
                                MapsActivity.mapsActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MapsActivity.servicioLayout.setVisibility(View.VISIBLE);
                                        Utilidades.GONE = false;
                                        try {
                                            MapsActivity.textViewNServicio.setText(servicio.getString("servicio_id"));
                                            MapsActivity.textViewOrigen.setText(servicio.getString("servicio_partida"));
                                            MapsActivity.textViewDestino.setText(servicio.getString("servicio_destino"));
                                            MapsActivity.textViewTipo.setText(servicio.getString("servicio_tipo"));
                                            MapsActivity.textViewNombre.setText(servicio.getString("servicio_pasajero"));
                                            MapsActivity.textViewDireccion.setText(servicio.getString("servicio_pasajero_direccion"));
                                            MapsActivity.textViewCelular.setText(servicio.getString("servicio_pasajero_celular"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            Utilidades.TIEMPO_ESPERA--;
                            Log.i("tirmpo restante",Utilidades.TIEMPO_ESPERA+"");
                        }
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                break;
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {

    }
    @Override
    protected void onCancelled() {

    }
}