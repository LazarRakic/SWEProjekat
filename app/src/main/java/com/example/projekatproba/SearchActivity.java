package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    List<String> selektovaniSastojci;
    RecyclerView dataList;
    List<Sastojak> sastojakList;
    AdapterSastojci adapterSastojci;
    ImageView home;
    TextView pretrazi;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        home= findViewById(R.id.homeImageTool);

        dataList= findViewById(R.id.dataList);
        this.sastojakList=new ArrayList<Sastojak>();
        this.selektovaniSastojci=new ArrayList<String>();

        pretrazi = findViewById(R.id.pretraziPoSastojcima);

        Context sada = this;

        docRef.collection("sastojci")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Sastojak sastojak = new Sastojak(document.get("naziv").toString(),document.get("sImgUrl").toString());

                                sastojakList.add(sastojak);


                                Log.d("TAG", document.get("naziv") + " => " + document.get("sImgUrl"));
                            }
                            sastojakList.forEach(e ->Log.d("TAG","Sastojak"+e.getIme()) );
                            adapterSastojci = new AdapterSastojci(sada, sastojakList);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                            dataList.setLayoutManager(gridLayoutManager);
                            dataList.setAdapter(adapterSastojci);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selektovaniSastojci.addAll(adapterSastojci.nizSelektovanih);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                Log.d("NIZARA NIZARA ALE ALE",selektovaniSastojci.toString());
            }
        });
        pretrazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //TODO PRETRAGA
                String s="";
                for(String s1 : adapterSastojci.nizSelektovanih)
                {
                    s+=s1+",";
                }
                Intent intent = new Intent(getBaseContext(), SearchActivityResult.class);
                intent.putExtra("SASTOJCI",s);
                startActivity(intent);
            }
        });

    }

}