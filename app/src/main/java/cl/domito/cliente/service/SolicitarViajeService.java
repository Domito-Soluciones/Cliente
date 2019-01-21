package cl.domito.cliente.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SolicitarViajeService extends Service {
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
        for (int i = 0 ; i < 10 ; i ++) {
            System.out.println("El servicio a Comenzado");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.stopSelf();
        return Service.START_STICKY ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("El servicio a Terminado");
    }
}
