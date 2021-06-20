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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ChangeUsernameActivity extends AppCompatActivity {
    Button save;
    Button discard;
    EditText newPassword;    EditText newUsername;
    ProgressBar bar;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        discard= findViewById(R.id.buttonDiscard);
        newPassword=findViewById(R.id.editTextNewPassword);
  		save= findViewById(R.id.buttonSave);
  		newUsername= findViewById(R.id.editTextNewUsername);
        baseAuth= FirebaseAuth.getInstance();
        bar= findViewById(R.id.progressBar3);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=newUsername.getText().toString();
                CollectionReference usersRef= docRef.collection("korisnici");
                Query query=usersRef.whereEqualTo("username",userName);
                DocumentReference documentReference= docRef.collection("korisnici").document(baseAuth.getInstance().getCurrentUser().getUid());

 				if (TextUtils.isEmpty(userName)) {
 				    newUsername.setError("Polje za korisničko ime ne sme biti prazno!");
                    return;
                }

                bar.setVisibility(View.VISIBLE);
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
                            documentReference.update(

                                    "username", userName                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Podaci uspešno ažurirani.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Greška prilikom ažuriranja podataka.", Toast.LENGTH_SHORT).show();
                                        bar.setVisibility(View.GONE);
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {

                                }
                            });

                        }
                        else
                        {
                            newUsername.setError("Korisničko ime već postoji!");
                            bar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });



        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

    }
}