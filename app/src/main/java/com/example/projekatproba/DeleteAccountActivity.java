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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteAccountActivity extends AppCompatActivity {
    Button delete;
    Button dontDelete;
    FirebaseUser user;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        delete=findViewById(R.id.buttonDeleteProfile);
        dontDelete= findViewById(R.id.buttonDontDeleteProfile);
        baseAuth= FirebaseAuth.getInstance();
        user= baseAuth.getCurrentUser();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert= new AlertDialog.Builder(v.getContext());
                alert.setTitle("Da li ste sigurni da želite da obrišete nalog:");
                alert.setMessage("Brisanjem u potpunosti uklanjate nalog iz sistema.");
                alert.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                          //          DocumentReference documentReference= docRef.collection("korisnici").document(baseAuth.getInstance().getCurrentUser().getUid());

                                    docRef.collection("korisnici").document(user.getUid().toString())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error deleting documentAAAAAAAAAAAAAAAAAAAAAAAAA", e);
                                                }
                                            });

                                    Toast.makeText(v.getContext(), "Nalog je obrisan", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                                }
                                else {
                                    Toast.makeText(v.getContext(), "Greška prilikom brisanja naloga." + user.getUid(), Toast.LENGTH_LONG).show();
                                    Log.w("TAG", "Error deleting documentAAAAAAAAAAAAAAAAAAAAAAAAA");
                                }
                            }
                        });
                    }
                });

                alert.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= alert.create();
                alertDialog.show();
            }
        });



        dontDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }
}