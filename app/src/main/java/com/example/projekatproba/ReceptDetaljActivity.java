package com.example.projekatproba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReceptDetaljActivity extends AppCompatActivity {

    TextView opisRecepta;
    TextView sastojci;
    TextView naziv;
    ImageView receptSlika;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recept_detalj);

        opisRecepta= (TextView) findViewById(R.id.opisReceptaDetaljan);
        receptSlika= (ImageView) findViewById(R.id.slikaReceptaDetaljna);
        sastojci= (TextView) findViewById(R.id.sastojciReceptaDetaljan);
        naziv= findViewById(R.id.nazivReceptaDetaljan);
        home=findViewById(R.id.home_dugme_recept_detalj);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        ArrayList<String> resultList= getIntent().getExtras().getStringArrayList("Lista");


            opisRecepta.setText(resultList.get(1));
            sastojci.setText(resultList.get(2));
            naziv.setText(resultList.get(3));
            Log.d("TAG", "JANAAAAAAAAAAAAAA "+resultList.get(0));
            Picasso.get().load(String.valueOf(resultList.get(0))).into(receptSlika);


    }

}
