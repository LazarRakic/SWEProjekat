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

public class FragmentLatest extends Fragment {

    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    Recept recept;
    RecyclerView recyclerView;
    List<Recept> receptList;
    AdapterReceptiProfiliKorisnika adapterReceptiProfiliKorisnika;
    List<Recept> novaSortiranaLista ;
    TimeUtility timeUtility;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_latest, container, false);

        recyclerView= view.findViewById(R.id.receptiNaLatest);
        receptList=new ArrayList<>();
        novaSortiranaLista = new ArrayList<>();
        timeUtility = new TimeUtility();


        CollectionReference receptRef= docRef.collection("recepti");
        receptRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recept =new Recept(document.getId(), document.getString("naziv"),document.getString("priprema"),document.getString("sastojci"),Long.parseLong( document.get("datum").toString()),document.getString("idPublishera"),document.getString("Img"),document.getString("ocena"),document.getString("usernameKorisnika"));
                                receptList.add(recept);

                            }
                            Recept receptiArray[] = new Recept[receptList.size()];

                            for(int i=0;i<receptList.size();i++)
                            {
                                receptiArray[i]=receptList.get(i);
                                Log.d("TAG","NIZ Lista item datum "+ timeUtility.convertLongToLocalDateTime( receptList.get(i).getDatum()).toString()+" Naziv "+receptList.get(i).getNaziv() );
                            }
                            sort(receptiArray);


                            for (int i = receptiArray.length-1; i >= 0;i--) {
                                    novaSortiranaLista.add(receptiArray[i]);

                            }

                            GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(), 1);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            adapterReceptiProfiliKorisnika= new AdapterReceptiProfiliKorisnika(getContext(), novaSortiranaLista);
                            recyclerView.setAdapter(adapterReceptiProfiliKorisnika);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });



        return view;
    }

    void sort(Recept recepti[])
    {
        int n = recepti.length;

        for (int i = 0; i < n-1; i++)
        {
            int min_idx = i;
            for (int j = i+1; j < n; j++)
            {
                if (recepti[j].getDatum() < recepti[min_idx].getDatum())
                {
                    min_idx = j;
                }
            }


            Recept temp = recepti[min_idx];
            recepti[min_idx] = recepti[i];
            recepti[i] = temp;
        }
    }


}
