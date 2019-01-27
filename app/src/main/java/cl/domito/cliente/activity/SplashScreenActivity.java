package cl.domito.cliente.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import cl.domito.cliente.R;
import cl.domito.cliente.dominio.Usuario;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        Usuario.getInstance().setConectado(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getApplicationContext().getSharedPreferences
                        ("preferencias", Context.MODE_PRIVATE);
                String idUsuario = pref.getString("idUsuario",
                        "");
                if(!idUsuario.equals(""))
                {
                    Usuario.getInstance().setNick(idUsuario);
                    Intent mainIntent = new Intent(SplashScreenActivity.this,MapsActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                    handler.removeCallbacksAndMessages(null);
                }
                else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                    handler.removeCallbacksAndMessages(null);
                }
            }
        }, 2000);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }



}
