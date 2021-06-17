package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DodavanjeRecepataActivity extends AppCompatActivity {

    ImageView slika;
    EditText priprema;
    TextView sastojci;
    Uri imageUri;
    String imageUrl;
    StorageReference storage;
    FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    FirebaseAuth baseAuth;
    List<String> selektovaniSastojci;
    RecyclerView dataList;
    List<String> titles;
    List<String> images;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_recepata);

        slika = findViewById(R.id.imageView3);
        priprema = findViewById(R.id.pripremaPlainText);
        sastojci = findViewById(R.id.sastojciPlanText);

        dataList= findViewById(R.id.listaSastojaka);

        titles= new ArrayList<>();
        images= new ArrayList<>();
        this.selektovaniSastojci=new ArrayList<String>();

        slika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
        });
        Context sada=this;
        docRef.collection("sastojci")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                titles.add(document.get("naziv").toString());
                                images.add(document.get("sImgUrl").toString());


                                Log.d("TAG", document.get("naziv") + " => " + document.get("sImgUrl"));
                            }

                            Log.d("TAG", titles.toString() + " => " + images.toString());
                            adapter = new Adapter(sada, titles, images);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(DodavanjeRecepataActivity.this, 4, GridLayoutManager.VERTICAL, false);
                            dataList.setLayoutManager(gridLayoutManager);
                            dataList.setAdapter(adapter);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }


                });

        dataList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selektovaniSastojci=adapter.nizSelektovanih;
                Log.d("NIZARA NIZARA ALE ALE",selektovaniSastojci.toString());
                sastojci.setText(selektovaniSastojci.toString());
            }
        });

    }



}