package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ChangePasswordActivity extends AppCompatActivity {
    Button change;
    Button dontChange;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    FirebaseUser user;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        email= findViewById(R.id.textViewproba);

        change= findViewById(R.id.buttonChange);
        dontChange= findViewById(R.id.buttonDontChange);
        baseAuth= FirebaseAuth.getInstance();

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=baseAuth.getCurrentUser();
                String user1=user.getUid();

                DocumentReference documentReference=docRef.collection("korisnici").document(user1);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                email.setText(document.get("email").toString());
                                Log.d("TAG:", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("TAG:", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

                EditText resetMail = new EditText (v.getContext());
                AlertDialog.Builder passwordResetDijalog = new AlertDialog.Builder(v.getContext());
                passwordResetDijalog.setTitle("Resetovati Lozinku?");
                passwordResetDijalog.setMessage("Unesite vašu email adresu da dobijete link za oporaka naloga!");
                passwordResetDijalog.setView(resetMail);

                passwordResetDijalog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = email.getText().toString();
                        baseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ChangePasswordActivity.this, "Link za oporaka naloga je poslat na vaš mail.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), LogInActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChangePasswordActivity.this, "Greška! Link za oporaka naloga nije poslat! ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDijalog.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDijalog.create().show();
            }
        });

        dontChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });


    }
}