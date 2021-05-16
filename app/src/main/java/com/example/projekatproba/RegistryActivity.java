package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegistryActivity extends AppCompatActivity {

    EditText name;
    EditText surname;
    EditText date;
    EditText username, password;
    EditText email;
    Button registry;
    FirebaseAuth baseAuth;
    ProgressBar bar;

    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();


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
                String emaiL = email.getText().toString().trim();
                String passworD = password.getText().toString().trim();

                if (TextUtils.isEmpty(emaiL)) {
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


                String userName=username.getText().toString();
                CollectionReference usersRef= docRef.collection("korisnici");
                Query query=usersRef.whereEqualTo("username",userName);

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        String pom = document.getString("username");


                                        if(pom.equals(userName))
                                        {
                                            Log.d("TAG","user exist");

                                        }
                                    }
                                }
                                if(task.getResult().size()==0)
                                {
                                    Log.d("TAG","User not exist");
                                    baseAuth.createUserWithEmailAndPassword(emaiL, passworD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                String nameVal = name.getText().toString();
                                                String surnameVal = surname.getText().toString();
                                                String dateVal = date.getText().toString();
                                                String usernameVal = username.getText().toString();
                                                String passwordVal = password.getText().toString();
                                                String emailVal = email.getText().toString();


                                                Map<String, Object> dataToSave = new HashMap<String, Object>();
                                                dataToSave.put("name", nameVal);
                                                dataToSave.put("surname", surnameVal);
                                                dataToSave.put("date", dateVal);
                                                dataToSave.put("username", usernameVal);
                                                dataToSave.put("password", passwordVal);
                                                dataToSave.put("email", emailVal);

                                                Toast.makeText(RegistryActivity.this, "Nalog je kreiran.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                                docRef.collection("korisnici").document(baseAuth.getCurrentUser().getUid()).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("TAG", "Dokument je sačuvan! ");

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Log.w("TAG", "Nije sačuvano u bazi! ", e);
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(RegistryActivity.this, "Greška prilikom kreiranja naloga!", Toast.LENGTH_SHORT).show();
                                                bar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    username.setError("Korisnicko ime vec postoji!");
                                    bar.setVisibility(View.GONE);
                                }

                            }
                        });




            }
        });
    }
}