package com.example.projekatproba;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    Recept recept;
    RecyclerView recyclerView;
    List<Recept> receptList;
    AdapterReceptiProfiliKorisnika adapterReceptiProfiliKorisnika;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView= view.findViewById(R.id.receptiNaHome);
        receptList=new ArrayList<>();



        CollectionReference receptRef= docRef.collection("recepti");
        receptRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(), 1);
                                recyclerView.setLayoutManager(gridLayoutManager);

                                recept =new Recept(document.getId(), document.getString("naziv"),document.getString("priprema"),document.getString("sastojci"),Long.parseLong( document.get("datum").toString()),document.getString("idPublishera"),document.getString("Img"),document.getString("ocena"));
                                Log.d("TAG", "onComplete:"+recept.getNaziv());
                                receptList.add(recept);
                                adapterReceptiProfiliKorisnika= new AdapterReceptiProfiliKorisnika(getContext(), receptList);
                                recyclerView.setAdapter(adapterReceptiProfiliKorisnika);


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });



        return view;
    }
}
