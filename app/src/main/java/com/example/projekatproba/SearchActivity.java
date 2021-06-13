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
    List<String> titles;
    List<String> images;
    Adapter adapter;
    ImageView home;
TextView pretrazi;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        home= findViewById(R.id.homeImageTool);



        dataList= findViewById(R.id.dataList);

        titles= new ArrayList<>();
        images= new ArrayList<>();
        this.selektovaniSastojci=new ArrayList<String>();


        Context sada = this;

           /* docRef.collection("meso")
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
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setAdapter(adapter);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            docRef.collection("povrce")
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
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setAdapter(adapter);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            docRef.collection("ostalo")
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
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setAdapter(adapter);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            docRef.collection("zacini")
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
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setAdapter(adapter);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            docRef.collection("voce")
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
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                                dataList.setLayoutManager(gridLayoutManager);
                                dataList.setAdapter(adapter);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }

                        }
                    });*/




         //sel ektovaniSastojci.addAll(adapter.nizSelektovanih);







         //sel ektovaniSastojci.addAll(adapter.nizSelektovanih);


        //PROBA PROBA PROBA
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
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                            dataList.setLayoutManager(gridLayoutManager);
                            dataList.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });*/


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selektovaniSastojci.addAll(adapter.nizSelektovanih);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                Log.d("NIZARA NIZARA ALE ALE",selektovaniSastojci.toString());
            }
        });



    }

}