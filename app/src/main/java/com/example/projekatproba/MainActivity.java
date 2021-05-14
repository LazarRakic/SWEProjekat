package com.example.projekatproba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        androidx.constraintlayout.widget.ConstraintLayout relativeLayoutMain = (androidx.constraintlayout.widget.ConstraintLayout) findViewById(R.id.constraintlay);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        relativeLayoutMain.startAnimation(alphaAnimation);

        //  setContentView(R.layout.activity_home);

    }

    public void onUser(View v) {
        startActivity(new Intent(getBaseContext(), LogInActivity.class));
    }
}

