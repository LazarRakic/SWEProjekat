package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LogInActivity extends AppCompatActivity {

    TextView forgot;
    TextView register;
    Button login;
    EditText email;
    EditText password;
    ProgressBar bar;
    FirebaseAuth baseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgot=findViewById(R.id.forgot);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        email= findViewById(R.id.email);
        login=findViewById(R.id.login);
        bar= findViewById(R.id.progressBar2);
        baseAuth= FirebaseAuth.getInstance();
        

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegistryActivity.class));
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText (v.getContext());
                AlertDialog.Builder passwordResetDijalog = new AlertDialog.Builder(v.getContext());
                passwordResetDijalog.setTitle("Resetovati Lozinku?");
                passwordResetDijalog.setMessage("Unesite vašu email adresu da dobijete link za oporavak naloga!");
                passwordResetDijalog.setView(resetMail);

                passwordResetDijalog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = email.getText().toString();
                        baseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LogInActivity.this, "Link za oporavak naloga je poslat na vaš mail.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                Toast.makeText(LogInActivity.this, "Greška! Link za oporavak naloga nije poslat! ", Toast.LENGTH_SHORT).show();
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user= email.getText().toString().trim();
                String pass= password.getText().toString().trim();

                if(TextUtils.isEmpty(user)){
                    email.setError("Polje za email ne sme biti prazno!");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Polje za lozinku mora biti popunjeno!");
                    return;
                }
                if (pass.length() < 6) {
                    password.setError("Vaša lozinka mora imati 6 ili više karaktera!");
                    return;
                }
                bar.setVisibility(View.VISIBLE);

                baseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LogInActivity.this, "Uspešno prijavljivanje.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            Toast.makeText(LogInActivity.this, "Greška prilikom prijavljivanja!", Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
    }
}