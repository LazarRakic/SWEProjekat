package com.example.projekatproba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    TextView username;
    TextView email;
    ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        ArrayList<String> profileLista=getIntent().getExtras().getStringArrayList("List");

        username=findViewById(R.id.userName);
        email=findViewById(R.id.eMail);
        profileImage=findViewById(R.id.profile_image);

        username.setText(profileLista.get(4));
        email.setText(profileLista.get(0));
        Picasso.get().load(profileLista.get(3)).into(profileImage);



    }
}