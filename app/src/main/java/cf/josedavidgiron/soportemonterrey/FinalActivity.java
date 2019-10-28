package cf.josedavidgiron.soportemonterrey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FinalActivity extends AppCompatActivity {
    EditText txtMotivoVista, txtDescripcionTextoVista, txtDireccionSoporteVista, txtPrioridadTicket, txtEstadoTicket, txtNombreSolicitanteTicket, txtCorreoSolicitanteTicket, txtFechaTicket, txtSolucionTicket;
    Button btnCambiarEstadoTicket, btnRegresarTickets;
    ImageView imageViewTickets;
    private String strFoto, strFechaCre, strFechaO, strIdUsuario;
    private String strIdTiket;
    ProgressDialog mproProgressDialog;
    DatePickerDialog.OnDateSetListener setListener;
    private int intYear;
    private int intMont;
    private int intDays;
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
        txtFechaTicket = findViewById(R.id.txtFechaSoporte);
        txtSolucionTicket = findViewById(R.id.txtObservacion);
        imageViewTickets = findViewById(R.id.imageViewTickets);
        btnCambiarEstadoTicket = findViewById(R.id.btnCambiarEstadoTicket);
        btnRegresarTickets = findViewById(R.id.btnRegresarTickets);
        mproProgressDialog = new ProgressDialog(this);

        cargarDatosTicket(strIdTiket);

        Calendar calendar = Calendar.getInstance();
        intYear = calendar.get(Calendar.YEAR);
        intMont = calendar.get(Calendar.MONTH);
        intDays = calendar.get(Calendar.DAY_OF_MONTH);

        txtFechaTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        //android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        FinalActivity.this, setListener, intYear, intMont, intDays
                );
                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String mes = ""+month;
                if(month < 10 ){
                    mes = "0"+month;
                }
                String strDate = intYear+"-"+mes+"-"+intDays;
                txtFechaTicket.setText(strDate);
            }
        };

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
                map.put("strEstado", txtEstadoTicket.getText().toString());
                map.put("strFechaC", strFechaCre);
                map.put("strFechaO", strFechaO);
                map.put("strFechaS", NDate);
                map.put("strUsuario",  strIdUsuario);
                map.put("strDireccion", txtDireccionSoporteVista.getText().toString());
                map.put("strFechaTicket",  txtFechaTicket.getText().toString());
                map.put("strObservacion", txtSolucionTicket.getText().toString());

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

                    if(ds.child("strFechaTicket").getValue() != null){
                        txtFechaTicket.setText(ds.child("strFechaTicket").getValue().toString());
                    }

                    if(ds.child("strObservacion").getValue() != null){
                        txtSolucionTicket.setText(ds.child("strObservacion").getValue().toString());
                    }

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
