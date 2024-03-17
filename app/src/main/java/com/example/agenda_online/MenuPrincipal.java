package com.example.agenda_online;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuPrincipal extends AppCompatActivity {


    Button CerrarSession;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);

        CerrarSession = findViewById(R.id.CerrarSession);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        CerrarSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalirAplication();
            }
        });

    }

    private void SalirAplication() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this,MainActivity.class));
        Toast.makeText(this, "Cerraste session Correctamente", Toast.LENGTH_SHORT).show();
    }
}


