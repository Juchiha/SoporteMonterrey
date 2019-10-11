package cf.josedavidgiron.soportemonterrey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText txtUsuario, txtPassword;
    private FirebaseAuth mAuth;
    private String email = "";
    private String passw = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        /*Validacion del permiso de internet*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        101);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        101);
            }
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        101);
            }
        }



        txtUsuario = findViewById(R.id.txtUsuarioRegistro);
        txtPassword = findViewById(R.id.txtPasswordRegistro);
        txtUsuario.requestFocus();

        Button btnEnviarLogin = findViewById(R.id.btnRegistroSoporte);
        btnEnviarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtUsuario.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()){
                    email = txtUsuario.getText().toString();
                    passw = txtPassword.getText().toString();
                    getLogin(email, passw);
                }else{
                    Toast.makeText(MainActivity.this, "TODOS LOS CAMPOS SON REQUERIDOS!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnRegistro = findViewById(R.id.btnRegistroApplicacion);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, Tickets_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getLogin(String usuario_p , String pass_p){
        final ProgressDialog mDialog = ProgressDialog.show(this, "", "Un momento por favor...", true, false );

        mAuth.signInWithEmailAndPassword(usuario_p, pass_p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mDialog.cancel();
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, Tickets_Activity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Usuario y/o contraseña errados!.",
                            Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    mDialog.dismiss();
                }
            }
        });
    }
}
