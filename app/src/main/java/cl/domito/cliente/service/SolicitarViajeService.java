package cl.domito.cliente.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;

public class SolicitarViajeService extends Service {

    int estadoServicio = 0;

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
        JSONObject jsonObject = null;
        while (estadoServicio < 2) {
            try {
                System.out.println("esperando que el id sea mayor a 2");
                String url = Utilidades.URL_BASE_SERVICIO + "GetServicio.php";
                List<NameValuePair> params = new ArrayList();
                params.add(new BasicNameValuePair("id",intent.getExtras().get("idServicio").toString()));
                jsonObject = Utilidades.enviarPost(url,params);
                estadoServicio = jsonObject.getInt("servicio_estado");
                this.stopSelf();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }  catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println("servicio asignado a el movil " + jsonObject.getString("servicio_movil")+ " ahora debo cerrar el activity");
            Activity activity = (Activity) this.getApplicationContext();
            activity.finish();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return Service.START_STICKY ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("El servicio a Terminado");
    }
}
