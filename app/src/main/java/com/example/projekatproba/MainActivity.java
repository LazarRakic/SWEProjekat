package com.example.projekatproba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth baseAuth;

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

