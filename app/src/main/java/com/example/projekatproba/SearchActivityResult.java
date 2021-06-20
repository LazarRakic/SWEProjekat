package com.example.projekatproba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivityResult extends AppCompatActivity {

    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    Recept recept;
    RecyclerView recyclerView;
    List<Recept> receptList;
    List<Recept> novaListaRecepata;
    AdapterReceptiProfiliKorisnika adapterReceptiProfiliKorisnika;
    ImageView slikaNemaRecepta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        String sastojci = getIntent().getStringExtra("SASTOJCI");

        receptList=new ArrayList<>();
        novaListaRecepata=new ArrayList<>();
        recyclerView=findViewById(R.id.pretragaRecepti);
        slikaNemaRecepta=findViewById(R.id.nemaRecepta);
        slikaNemaRecepta.setVisibility(View.INVISIBLE);

        Log.d("TAG","Svi sastojci koji smo preneli na novi activity"+sastojci);

        CollectionReference receptRef= docRef.collection("recepti");
        receptRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recept =new Recept(document.getId(), document.getString("naziv"),document.getString("priprema"),document.getString("sastojci"),Long.parseLong( document.get("datum").toString()),document.getString("idPublishera"),document.getString("Img"),document.getString("ocena"));
                                receptList.add(recept);
//
//                                GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(), 1);
//                                recyclerView.setLayoutManager(gridLayoutManager);
//
//                                recept =new Recept(document.getId(), document.getString("naziv"),document.getString("priprema"),document.getString("sastojci"),Long.parseLong( document.get("datum").toString()),document.getString("idPublishera"),document.getString("Img"),document.getString("ocena"));
//                                Log.d("TAG", "onComplete:"+recept.getNaziv());
//                                receptList.add(recept);
//                                adapterReceptiProfiliKorisnika= new AdapterReceptiProfiliKorisnika(getContext(), receptList);
//                                recyclerView.setAdapter(adapterReceptiProfiliKorisnika);
                            }
                            String [] nizPrimljenihSastojaka;
                            boolean daLiJeNadjenRecept=false;
                            nizPrimljenihSastojaka=sastojci.split("," ,0);
                            for(int i=0; i<receptList.size();i++)
                            {
                                String temp=receptList.get(i).getSastojci();
                                Log.d("TAG","Sastojak logovan "+temp);
                                boolean bool=false;
                                for(int j=0; j<nizPrimljenihSastojaka.length;j++)
                                {
                                    Pattern p=Pattern.compile(nizPrimljenihSastojaka[j]);
                                    Matcher m=p.matcher(temp);
                                    if(m.find())
                                    {
                                        Log.d("TAG","Niz primljenih sastojaka "+ nizPrimljenihSastojaka[j]);
                                        bool=true;
                                        daLiJeNadjenRecept=true;
                                    }
                                }
                                if(bool)
                                {
                                    novaListaRecepata.add(receptList.get(i));
                                }

                                //svaki recept ponaosob uporedjujemo sa stringom sastojaka
                            }

                            if(!daLiJeNadjenRecept) {
                                slikaNemaRecepta.setVisibility(View.VISIBLE);
                                daLiJeNadjenRecept=false;
                            }

                            GridLayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(), 1);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            adapterReceptiProfiliKorisnika= new AdapterReceptiProfiliKorisnika(getApplicationContext(), novaListaRecepata);

                            recyclerView.setAdapter(adapterReceptiProfiliKorisnika);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}