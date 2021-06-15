package com.example.projekatproba;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReceptDetaljActivity extends AppCompatActivity {

    TextView opisRecepta;
    ImageView receptSlika;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recept_detalj);

        opisRecepta= (TextView) findViewById(R.id.opisReceptaDetaljan);
        receptSlika= (ImageView) findViewById(R.id.slikaReceptaDetaljna);

        ArrayList<String> resultList= getIntent().getExtras().getStringArrayList("Lista");


            opisRecepta.setText(resultList.get(1));
            Log.d("TAG", "JANAAAAAAAAAAAAAA "+resultList.get(0));
            Picasso.get().load(String.valueOf(resultList.get(0))).into(receptSlika);


    }

}
