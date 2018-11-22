package cl.domito.cliente.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import cl.domito.cliente.R;
import cl.domito.cliente.http.RequestUsuario;
import cl.domito.cliente.http.Utilidades;
import cl.domito.cliente.thread.LoginOperation;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserView;
    private EditText mPasswordView;
    private ProgressBar progressBar;
    private CheckBox checkBoxRec;

    public static boolean RECORDAR = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        mUserView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        Button mEmailSignInButton = findViewById(R.id.login_button);
        checkBoxRec = findViewById(R.id.checkBox);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        checkBoxRec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                recordar();
            }
        });
        progressBar = findViewById(R.id.login_progress);
    }

    private void recordar() {
        if(checkBoxRec.isChecked())
        {
                RECORDAR = true;
    }
        else
    {
        RECORDAR = false;
    }
}

    private void login() {
        LoginOperation loginOperation = new LoginOperation(this);
        String usuario = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        loginOperation.execute(usuario,password);
    }

}