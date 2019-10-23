package cf.josedavidgiron.soportemonterrey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cf.josedavidgiron.soportemonterrey.clases.AdapterTickets;
import cf.josedavidgiron.soportemonterrey.clases.AdapterTicketsUsers;
import cf.josedavidgiron.soportemonterrey.clases.FirebaseTickets;

public class Tickets_Activity extends AppCompatActivity {
    private String strPrioridad = null;
    private FirebaseAuth mAuth;
    private StorageReference storage;
    private int numeroTicket = 0;
    private static final int GALLERY_INTENT = 1;
    private Uri uri = null;
    ProgressDialog mproProgressDialog;
    private String strNombreFoto;
    private String strTipoUsuario = "Usuario";
    private RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        recycler = findViewById(R.id.idReciclerViewTIckets);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        /*Cargo el recicler View*/
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getTipoUsuario();
        storage = FirebaseStorage.getInstance().getReference();
        mproProgressDialog = new ProgressDialog(this);



        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButtonNewTicket);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Tickets_Activity.this, "Presionado", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder( Tickets_Activity.this);
                LayoutInflater inflater = getLayoutInflater();
                View vista = inflater.inflate(R.layout.new_ticket_layout, null);
                final EditText strMotivo = vista.findViewById(R.id.txtMotivoVista);
                final EditText strDescripcion = vista.findViewById(R.id.txtDescripcionTextoVista);
                final RadioButton radioPrioridad6 = vista.findViewById(R.id.radioButton6);
                final RadioButton radioProoridad7 = vista.findViewById(R.id.radioButton7);
                final EditText strDireccionSoporte = vista.findViewById(R.id.txtDireccionSoporteVista);
                strPrioridad = null;

                radioPrioridad6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                       // radioProoridad7.setChecked(false);
                        //radioPrioridad6.setChecked(true);
                        strPrioridad = "Prioridad Normal";
                    }
                });

                radioProoridad7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        //radioPrioridad6.setChecked(false);
                        //radioProoridad7.setChecked(true);
                        strPrioridad = "Prioridad Urgente";
                    }
                });

                Button btnImagenLayout = vista.findViewById(R.id.btnImagenLayout);
                btnImagenLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_INTENT);
                    }
                });

                builder.setView(vista)
                    .setPositiveButton("Crear Ticket", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                                if(strMotivo.getText().length() > 1 && strDescripcion.getText().length() > 1 && strDireccionSoporte.getText().length() > 1){
                                    if(strPrioridad.equals(null) || strPrioridad.equals("")){
                                        Toast.makeText(Tickets_Activity.this, "Es necesaria la prioridad del ticket!", Toast.LENGTH_LONG).show();
                                    }else{
                                        mproProgressDialog.setTitle("Creando Ticket...");
                                        mproProgressDialog.setMessage("Subiendo Información de Ticket");
                                        mproProgressDialog.setCancelable(false);
                                        mproProgressDialog.show();
                                        numeroTicket = numeroTicket + 1;
                                        if(uri == null){
                                            strNombreFoto = "https://firebasestorage.googleapis.com/v0/b/soporteapp-69767.appspot.com/o/Soportes%2F175096072?alt=media&token=7972d642-3df7-42f8-a172-ccd27bf52015";
                                            enviarDatos(strMotivo.getText().toString(),
                                                    strDescripcion.getText().toString(),
                                                    strPrioridad,
                                                    strNombreFoto,
                                                    mAuth.getCurrentUser().getUid(),
                                                    strDireccionSoporte.getText().toString(),
                                                    numeroTicket,
                                                    dialog);
                                        }else{
                                            StorageReference filepath = storage.child("Soportes").child(uri.getLastPathSegment());
                                            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Task<Uri> uriDescargarFoto = taskSnapshot.getStorage().getDownloadUrl();
                                                    while(!uriDescargarFoto.isComplete());
                                                    Uri url = uriDescargarFoto.getResult();

                                                    strNombreFoto = url.toString();


                                                    //Enviar informacion al sistema
                                                    enviarDatos(strMotivo.getText().toString(),
                                                            strDescripcion.getText().toString(),
                                                            strPrioridad,
                                                            strNombreFoto,
                                                            mAuth.getCurrentUser().getUid(),
                                                            strDireccionSoporte.getText().toString(),
                                                            numeroTicket,
                                                            dialog);
                                                }
                                            });
                                        }

                                    }
                                }else{
                                    Toast.makeText(Tickets_Activity.this, "Son necesarios el motivo, la descripción y la direccion para crear un ticket!", Toast.LENGTH_LONG).show();
                                }
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

        private void chargeRecicler(DataSnapshot dataSnapshot, String strTipo){
            ArrayList<FirebaseTickets> firebaseTickets = new ArrayList<>();
            numeroTicket = 0;
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                String strFoto = "https://firebasestorage.googleapis.com/v0/b/soporteapp-69767.appspot.com/o/Soportes%2F175096072?alt=media&token=7972d642-3df7-42f8-a172-ccd27bf52015";
                if(ds.child("strFoto__").getValue() != null){
                    strFoto = ds.child("strFoto__").getValue().toString();
                }
                firebaseTickets.add( new FirebaseTickets(
                        ds.getKey(),
                        ds.child("strMotivo").getValue().toString(),
                        ds.child("strDesc__").getValue().toString(),
                        strFoto,
                        ds.child("strPrio__").getValue().toString(),
                        ds.child("strDesc__").getValue().toString(),
                        ds.child("strEstado").getValue().toString(),
                        ds.child("strFechaC").getValue().toString(),
                        ds.child("strFechaS").getValue().toString(),
                        ds.child("strDireccion").getValue().toString()
                ));
                numeroTicket = numeroTicket + 1;
            }

            Collections.reverse(firebaseTickets);
            if(strTipo.equals("1")){
                AdapterTickets adapterTickets = new AdapterTickets(firebaseTickets, Tickets_Activity.this);
                recycler.setAdapter(adapterTickets);
            }else{
                AdapterTicketsUsers adapterTickets = new AdapterTicketsUsers(firebaseTickets, Tickets_Activity.this);
                recycler.setAdapter(adapterTickets);
            }


        }
    private void enviarDatos(String strMotivo, String strDescripcion, String strPrioridad, String strNombreFoto, String strId, String strDireccionSoporte, int numeroTicket, final DialogInterface dialog){
        /*Procedemos a insertar*/



        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        String NDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cDate);

        Map<String, String> map = new HashMap<>();
        map.put("strMotivo", strMotivo);
        map.put("strDesc__", strDescripcion);
        map.put("strPrio__", strPrioridad);
        map.put("strFoto__", strNombreFoto);
        map.put("strEstado", "Abierto");
        map.put("strFechaC", fDate);
        map.put("strFechaO", NDate);
        map.put("strFechaS", "NA");
        map.put("strUsuario",  mAuth.getCurrentUser().getUid());
        map.put("strDireccion", strDireccionSoporte);
        Log.e("foto Ticket ::> ", "strNombreFoto ::> "+strNombreFoto);

        Log.e("numero Ticket ::> ", "J ::> "+numeroTicket);
        String id = mAuth.getCurrentUser().getUid()+"_TN_"+ numeroTicket;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Tickets").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if(task2.isSuccessful()){

                    mproProgressDialog.cancel();
                    mproProgressDialog.dismiss();
                    Toast.makeText(Tickets_Activity.this, "Ticket Creado con exito!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            /* Validar que este perfecto */
            uri = data.getData();
            /*mproProgressDialog.setTitle("");
            mproProgressDialog.setMessage("Un momento por favor....");
            mproProgressDialog.setCancelable(false);
            /*StorageReference filepath = storage.child("Soportes").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriDescargarFoto = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriDescargarFoto.isComplete());
                    Uri url = uriDescargarFoto.getResult();

                    strNombreFoto = url.toString();

                    mproProgressDialog.cancel();
                    mproProgressDialog.dismiss();
                }
            });*/
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_salir:
                /*Codigo*/
                mAuth.signOut();
                Intent intent = new Intent(Tickets_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getTipoUsuario(){
        /*Firebase*/
        Log.e("Esta ::>", "seguimiento");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String id = mAuth.getCurrentUser().getUid();
        Log.e("Esta ::>", "seguimiento ::> "+ id);
        dbRef.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //String nombr_ = dataSnapshot.child("nombre").getValue().toString();
                    strTipoUsuario = dataSnapshot.child("Rol").getValue().toString();
                    Log.e("Esta ::>", "seguimiento ::> "+ strTipoUsuario);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(strTipoUsuario.equals("Usuario")) {
                        mDatabase.child("Tickets").orderByChild("strUsuario").equalTo(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    /* Aqui limpiamos cada que ejecutamos para que se recargue */
                                    chargeRecicler(dataSnapshot, "2");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if(strTipoUsuario.equals("Administrador")){
                        mDatabase.child("Tickets").orderByChild("strFechaO").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    /* Aqui limpiamos cada que ejecutamos para que se recargue */
                                    chargeRecicler(dataSnapshot, "1");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
