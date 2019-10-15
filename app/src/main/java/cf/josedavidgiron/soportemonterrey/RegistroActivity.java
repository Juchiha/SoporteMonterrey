package cf.josedavidgiron.soportemonterrey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.HashMap;
import java.util.Map;


public class RegistroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText passwordEditText, correoEditText, nombresEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        passwordEditText = findViewById(R.id.txtPasswordRegistro);
        correoEditText   = findViewById(R.id.txtUsuarioRegistro);
        nombresEditText  = findViewById(R.id.txtNombre);

        mAuth = FirebaseAuth.getInstance();

        Button btnRegistro = findViewById(R.id.btnRegistroSoporte);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(correoEditText.getText().length() > 1 && passwordEditText.getText().length() > 4 && nombresEditText.getText().length() > 1){
                    final FirebaseAuth mAuth =  FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(correoEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Map<String, String> map = new HashMap<>();
                                map.put("Nombre", nombresEditText.getText().toString());
                                map.put("Password", passwordEditText.getText().toString());
                                map.put("Correo", correoEditText.getText().toString());
                                map.put("Rol", "Usuario");
                                String id = mAuth.getCurrentUser().getUid();

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task2) {
                                        if(task2.isSuccessful()){
                                            Toast.makeText(RegistroActivity.this, "Usuario registrado con exito!", Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegistroActivity.this, "Los campos son necesarios", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
