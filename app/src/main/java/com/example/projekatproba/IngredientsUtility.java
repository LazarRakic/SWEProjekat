package com.example.projekatproba;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsUtility {
    
    public IngredientsUtility() {
    }

    public Adapter getIngredients(RecyclerView recyclerView, String collection, FirebaseFirestore firebaseFirestore, Context sada, List<String> titles, List<String> images )
    {
        final Adapter[] adapter = {null};
        firebaseFirestore.collection(collection)
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

                            adapter[0] = new Adapter(sada, titles, images);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(sada, 4, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(adapter[0]);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());

                        }

                    }
                });
        return adapter[0];
        }
    }

