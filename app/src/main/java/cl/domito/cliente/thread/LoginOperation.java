package cl.domito.cliente.thread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.LoginActivity;
import cl.domito.cliente.activity.MapsActivity;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.dominio.Usuario;

public class LoginOperation extends AsyncTask<String, Void, Void> {

    WeakReference<LoginActivity> context;
    TextView textViewError;

    public LoginOperation(LoginActivity activity) {
        context = new WeakReference<LoginActivity>(activity);
    }

    @Override
    protected Void doInBackground(String... strings) {
        Usuario usuario = Usuario.getInstance();
        usuario.setConectado(true);
        LoginActivity loginActivity = context.get();
        ProgressBar progressBar = loginActivity.findViewById(R.id.login_progress);
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        });
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("usuario",strings[0]));
        params.add(new BasicNameValuePair("password",strings[1]));
        boolean login = RequestUsuario.loginUsuario(Utilidades.URL_BASE_USUARIO + "LoginPasajero.php",params);
        loginActivity.runOnUiThread(ActivityUtils.mensajeError(loginActivity));
        if (login) {
            usuario.setActivo(true);
            usuario.setNick(strings[0]);
            if(usuario.isRecordarSession()) {
                SharedPreferences pref = loginActivity.getApplicationContext().getSharedPreferences
                (loginActivity.getString(R.string.sharedPreferenceFile),Context.MODE_PRIVATE);
                ActivityUtils.guardarSharedPreferences(pref,loginActivity.getString(
                        R.string.sharedPreferenceKeyUser),usuario.getNick());
            }
            String url = Utilidades.URL_BASE_USUARIO + "ModEstadoPasajero.php";
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("usuario", usuario.getNick()));
            params2.add(new BasicNameValuePair("estado", Usuario.CONECTADO+""));
            Utilidades.enviarPost(url,params2);
            Intent mainIntent = new Intent(loginActivity, MapsActivity.class);
            loginActivity.startActivity(mainIntent);
            loginActivity.finish();
        } else {
            loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast t = Toast.makeText(loginActivity, "Usuario y/o contraseņa no coinciden", Toast.LENGTH_SHORT);
                    t.show();
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            });
        }
        return null;
    }


}
