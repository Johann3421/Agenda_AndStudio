package com.example.agenda_online;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
public class Registro extends AppCompatActivity {

    EditText NombreEt, CorreoEt, ContraseñaEt, ConfirmarContraseñaEt;
    Button RegistrarUsuario;
    TextView TengoUnaCuentaTXT;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Registrar");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        NombreEt = findViewById(R.id.NombreEt);
        CorreoEt = findViewById(R.id.CorreoEt);
        ContraseñaEt = findViewById(R.id.ContraseñaEt);
        ConfirmarContraseñaEt = findViewById(R.id.ConfirmarContraseñaEt);
        RegistrarUsuario = findViewById(R.id.RegistrarUsuario);
        TengoUnaCuentaTXT = findViewById(R.id.TengoUnaCuentaTXT);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

        TengoUnaCuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class));
            }
        });
    }

    private void validarDatos() {
        String nombre = NombreEt.getText().toString().trim();
        String correo = CorreoEt.getText().toString().trim();
        String contraseña = ContraseñaEt.getText().toString().trim();
        String confirmarContraseña = ConfirmarContraseñaEt.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(confirmarContraseña)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese una dirección de correo electrónico válida", Toast.LENGTH_SHORT).show();
        } else if (!contraseña.equals(confirmarContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else {
            crearCuenta(nombre, correo, contraseña);
        }
    }

    private void crearCuenta(final String nombre, final String correo, final String contraseña) {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        guardarInformacion(nombre, correo, contraseña);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Error al crear la cuenta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarInformacion(String nombre, String correo, String contraseña) {
        progressDialog.setMessage("Guardando su información...");

        String uid = firebaseAuth.getUid();

        if (uid != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(uid);

            HashMap<String, String> datos = new HashMap<>();
            datos.put("uid", uid);
            datos.put("correo", correo);
            datos.put("nombres", nombre);
            datos.put("password", contraseña);

            databaseReference.setValue(datos)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registro.this, MenuPrincipal.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this, "Error al guardar la información: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error al obtener el identificador de usuario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}









