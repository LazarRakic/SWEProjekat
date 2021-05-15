package com.example.projekatproba;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistryActivity extends AppCompatActivity {

    EditText name;
    EditText surname;
    EditText date;
    EditText username, password;
    EditText email;
    Button registry;
    FirebaseAuth baseAuth;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        name= findViewById(R.id.editTextName);
        surname= findViewById(R.id.editTextSurname);
        date= findViewById(R.id.editTextDate);
        username= findViewById(R.id.editTextUsername);
        password= findViewById(R.id.editTextPassword);
        email= findViewById(R.id.editTextEmail);
        registry= findViewById(R.id.buttonRegistry);
        bar= findViewById(R.id.progressBar);

        baseAuth= FirebaseAuth.getInstance();

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaiL= email.getText().toString().trim();
                String passworD= password.getText().toString().trim();

                if(TextUtils.isEmpty(emaiL)){
                    email.setError("Polje za email ne sme biti prazno!");
                    return;
                }
                if (TextUtils.isEmpty(passworD)) {
                    password.setError("Polje za lozinku mora biti popunjeno!");
                    return;
                }
                if (passworD.length() < 6) {
                    password.setError("Vaša lozinka mora imati 6 ili više karaktera!");
                    return;
                }

                bar.setVisibility(View.VISIBLE);

                baseAuth.createUserWithEmailAndPassword(emaiL, passworD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistryActivity.this, "Nalog je kreiran.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            Toast.makeText(RegistryActivity.this, "Greška prilikom kreiranja naloga!", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });
    }
}