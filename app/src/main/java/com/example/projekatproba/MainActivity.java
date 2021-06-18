package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth baseAuth;
    Button posetilac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        androidx.constraintlayout.widget.ConstraintLayout relativeLayoutMain = (androidx.constraintlayout.widget.ConstraintLayout) findViewById(R.id.constraintlay);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        relativeLayoutMain.startAnimation(alphaAnimation);

        baseAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = baseAuth.getCurrentUser();
        posetilac= findViewById(R.id.posetilac);



        posetilac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseAuth.signInAnonymously()
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInAnonymously:USPESNO");
                                    FirebaseUser user = baseAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    Log.w("TAG", "signInAnonymously:neuspesnoooooo", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });
    }

    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"Uspešno povezivanje.",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,HomeActivity.class));

        }else {
            Toast.makeText(this,"Neuspešno povezivanje.",Toast.LENGTH_LONG).show();
        }

    }

    public void onUser(View v) {

        if (baseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(getBaseContext(), LogInActivity.class));
        }

    }
}

