package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ChangeProfileDataActivity extends AppCompatActivity {

    Button save;
    Button discard;
    EditText newPassword;
    EditText newUsername;
    ProgressBar bar;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_data);

        save=findViewById(R.id.buttonSave);
        discard= findViewById(R.id.buttonDiscard);
        newPassword=findViewById(R.id.editTextNewPassword);
        newUsername= findViewById(R.id.editTextNewUsername);
        bar= findViewById(R.id.progressBar3);
        FirebaseUser user=baseAuth.getInstance().getCurrentUser();
        storage= FirebaseStorage.getInstance().getReference();
        String userIdValue=user.getUid();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordN = newPassword.getText().toString().trim();
                String usernameN= newUsername.getText().toString().trim();

                if (TextUtils.isEmpty(passwordN)) {
                    newPassword.setError("Polje za lozinku mora biti popunjeno!");
                    return;
                }
                if (passwordN.length() < 6) {
                    newPassword.setError("Vaša lozinka mora imati 6 ili više karaktera!");
                    return;
                }
                if (TextUtils.isEmpty(usernameN)) {
                    newUsername.setError("Polje za korisničko ime ne sme biti prazno!");
                    return;
                }

                bar.setVisibility(View.VISIBLE);
                DocumentReference documentReference= docRef.collection("korisnici").document(baseAuth.getInstance().getCurrentUser().getUid());

                String userName=newUsername.getText().toString();
                String passWord=newPassword.getText().toString();
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
                        if(task.getResult().size()==0) {
                            documentReference.update(
                                    "username", userName,
                                    "password", passWord
                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                            newUsername.setError("Korisnicko ime vec postoji!");
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