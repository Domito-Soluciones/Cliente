package cl.domito.cliente.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.SolicitarActivity;
import cl.domito.cliente.dominio.Usuario;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.thread.GetConductorOperation;
import cl.domito.cliente.thread.SolicitarServicioOperation;

public class SolicitarViajeService extends Service {

    int estadoServicio = 0;

    public static boolean TERMINAR = true;
    public static final String OCULTAR_LAYOUT_SERVICIO = "0";
    public static final String MOSTRAR_NOTIFICACION_SERVICIO= "1";
    public static final String CREAR_MARCADOR_MOVIL = "2";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                while (estadoServicio < 2) {
                    try {
                        System.out.println("esperando que el id sea mayor a 2");
                        System.out.println(intent.getExtras().get("idServicio"));
                        String idServicio = intent.getExtras().get("idServicio").toString();
                        SolicitarServicioOperation solicitarServicioOperation = new SolicitarServicioOperation();
                        jsonObject = solicitarServicioOperation.execute(idServicio).get();
                        estadoServicio = jsonObject.getInt("servicio_estado");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }  catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (estadoServicio == 2) {
                        sendMessage(MOSTRAR_NOTIFICACION_SERVICIO,null);
                        System.out.println("servicio asignado a el movil " + jsonObject.getString("servicio_movil") + " ahora debo cerrar el activity");
                        GetConductorOperation getConductorOperation = new GetConductorOperation();
                        Usuario.getInstance().setDatosConductor(getConductorOperation.execute(jsonObject.getString("servicio_movil")).get());
                        sendMessage(CREAR_MARCADOR_MOVIL,null);
                        sendMessage(OCULTAR_LAYOUT_SERVICIO,null);
                        estadoServicio = 0;
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return Service.START_STICKY ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("El servicio a Terminado");
        if(TERMINAR) {
            Intent broadcastIntent = new Intent(this, RestartBroadcastReceived.class);
            sendBroadcast(broadcastIntent);
        }
    }

    private void sendMessage(String message,String value) {
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", message);
        intent.putExtra("value", value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
