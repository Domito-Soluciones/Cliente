package cl.domito.cliente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cl.domito.cliente.R;
import cl.domito.cliente.activity.utils.ActivityUtils;
import cl.domito.cliente.service.SolicitarViajeService;
import cl.domito.cliente.thread.LoginOperation;
import cl.domito.cliente.dominio.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserView;
    private EditText mPasswordView;
    private Button mEmailSignInButton;
    private CheckBox checkBoxRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        mUserView = findViewById(R.id.usuario);
        mPasswordView = findViewById(R.id.password);
        mEmailSignInButton = findViewById(R.id.login_button);
        checkBoxRec = findViewById(R.id.checkBox);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            loginUsuario();
        }
        });
        checkBoxRec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                recordarInicioSesion();
            }
        });

        Intent i = new Intent(this, SolicitarViajeService.class);
        startService(i);
    }

    private void recordarInicioSesion() {
        Usuario.getInstance().setRecordarSession(true);
    }

    private void loginUsuario() {
        String usuario = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        if(!usuario.equals("") && !password.equals(""))
        {
            LoginOperation loginOperation = new LoginOperation(this);
            loginOperation.execute(usuario,password);
            ActivityUtils.hideSoftKeyBoard(this);
        }
        else
        {
            Toast t = Toast.makeText(this, "Ingrese tanto usuario como password", Toast.LENGTH_SHORT);
            t.show();
        }
    }

}