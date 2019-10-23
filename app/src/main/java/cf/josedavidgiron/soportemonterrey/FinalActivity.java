package cf.josedavidgiron.soportemonterrey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FinalActivity extends AppCompatActivity {
    EditText txtMotivoVista, txtDescripcionTextoVista, txtDireccionSoporteVista, txtPrioridadTicket, txtEstadoTicket, txtNombreSolicitanteTicket, txtCorreoSolicitanteTicket;
    Button btnCambiarEstadoTicket, btnRegresarTickets;
    ImageView imageViewTickets;
    private String strFoto, strFechaCre, strFechaO, strIdUsuario;
    private String strIdTiket;
    ProgressDialog mproProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizarticketslayout);

        Intent intent = getIntent();
        strIdTiket = intent.getStringExtra("FireTickets");

        txtMotivoVista = findViewById(R.id.txtMotivoVista);
        txtDescripcionTextoVista = findViewById(R.id.txtDescripcionTextoVista);
        txtDireccionSoporteVista = findViewById(R.id.txtDireccionSoporteVista);
        txtPrioridadTicket = findViewById(R.id.txtPrioridadTicket);
        txtNombreSolicitanteTicket = findViewById(R.id.txtNombreSolicitanteTicket);
        txtEstadoTicket = findViewById(R.id.txtEstadoTicket);
        txtCorreoSolicitanteTicket = findViewById(R.id.txtCorreoSolicitanteTicket);
        imageViewTickets = findViewById(R.id.imageViewTickets);
        btnCambiarEstadoTicket = findViewById(R.id.btnCambiarEstadoTicket);
        btnRegresarTickets = findViewById(R.id.btnRegresarTickets);
        mproProgressDialog = new ProgressDialog(this);

        cargarDatosTicket(strIdTiket);

        /*Regresar*/
        btnRegresarTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inet = new Intent(FinalActivity.this, Tickets_Activity.class);
                startActivity(inet);
                finish();
            }
        });

        btnCambiarEstadoTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mproProgressDialog.setTitle("Actualizando Ticket...");
                mproProgressDialog.setMessage("Subiendo Información de Ticket");
                mproProgressDialog.setCancelable(false);
                mproProgressDialog.show();

                Date cDate = new Date();
                String NDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cDate);

                Map<String, String> map = new HashMap<>();
                map.put("strMotivo", txtMotivoVista.getText().toString());
                map.put("strDesc__", txtDescripcionTextoVista.getText().toString());
                map.put("strPrio__", txtPrioridadTicket.getText().toString());
                map.put("strFoto__", strFoto);
                map.put("strEstado", "Cerrado");
                map.put("strFechaC", strFechaCre);
                map.put("strFechaO", strFechaO);
                map.put("strFechaS", NDate);
                map.put("strUsuario",  strIdUsuario);
                map.put("strDireccion", txtDireccionSoporteVista.getText().toString());

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Tickets").child(strIdTiket).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task2) {
                        if(task2.isSuccessful()){

                            mproProgressDialog.cancel();
                            mproProgressDialog.dismiss();
                            Toast.makeText(FinalActivity.this, "Ticket Actalizado con exito!", Toast.LENGTH_LONG).show();
                            Intent inet = new Intent(FinalActivity.this, Tickets_Activity.class);
                            startActivity(inet);
                            finish();
                        }
                    }
                });
            }
        });
    }

    void cargarDatosTicket(String strIdTicket){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Tickets").child(strIdTicket).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if(ds.exists()){
                    strFoto = ds.child("strFoto__").getValue().toString();
                    strFechaCre= ds.child("strFechaC").getValue().toString();
                    strFechaO= ds.child("strFechaO").getValue().toString();
                    strIdUsuario= ds.child("strUsuario").getValue().toString();

                    txtMotivoVista.setText(ds.child("strMotivo").getValue().toString());
                    txtDescripcionTextoVista.setText(ds.child("strDesc__").getValue().toString());
                    txtDireccionSoporteVista.setText(ds.child("strDireccion").getValue().toString());
                    txtPrioridadTicket.setText(ds.child("strPrio__").getValue().toString());
                    txtEstadoTicket.setText(ds.child("strEstado").getValue().toString());
                    Glide.with(FinalActivity.this)
                            .load(ds.child("strFoto__").getValue().toString())
                            .fitCenter()
                            .centerCrop()
                            .into(imageViewTickets);
                    cargarDatosUsuario(ds.child("strUsuario").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void cargarDatosUsuario(String strIdUsuario){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Users").child(strIdUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    txtNombreSolicitanteTicket.setText(dataSnapshot.child("Nombre").getValue().toString());
                    txtCorreoSolicitanteTicket.setText(dataSnapshot.child("Correo").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
