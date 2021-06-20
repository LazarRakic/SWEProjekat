package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PromenaRecepataActivity extends AppCompatActivity {

    Uri imageUri;
    String imageUrl;
    StorageReference storage;
    ImageView home;
    Button prikazi;
    Button promeni;
    ImageView slikaRecepta;
    EditText nazivRecepta;
    EditText pripremaRecepta;
    FirebaseAuth baseAuth;
    TextView sastojci;
    FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    AdapterSastojci adapterSastojci;
    List<String> titles;
    List<String> images;
    RecyclerView dataList;
    String idRecepta;
    List<Sastojak> sastojakList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promena_recepata);

        storage= FirebaseStorage.getInstance().getReference();
        prikazi=findViewById(R.id.prikaziSastojkePromena);
        home=findViewById(R.id.home_dugme_promena_recepta);
        sastojci= findViewById(R.id.sastojci);
        pripremaRecepta= findViewById(R.id.priprema);
        slikaRecepta= findViewById(R.id.receptSlika);
        nazivRecepta= findViewById(R.id.receptNaziv);
        promeni= findViewById(R.id.promenaRecept);
        titles= new ArrayList<>();
        images= new ArrayList<>();
        dataList= findViewById(R.id.listaSastojakaPromenaRecepta);
        baseAuth= FirebaseAuth.getInstance();


        sastojakList = new ArrayList<Sastojak>();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        ArrayList<String> resultList= getIntent().getExtras().getStringArrayList("Lista");
        pripremaRecepta.setText(resultList.get(1));
        sastojci.setText(resultList.get(2));
        nazivRecepta.setText(resultList.get(3));
        idRecepta=resultList.get(4);
        Log.d("TAG", "JANAAAAAAAAAAAAAA "+resultList.get(0));
        Picasso.get().load(String.valueOf(resultList.get(0))).into(slikaRecepta);

        prikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="";
                for(String s1 : adapterSastojci.nizSelektovanih)
                {
                    s+=s1+",";
                }
                sastojci.setText(s);
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

                                Sastojak sastojak = new Sastojak(document.get("naziv").toString(),document.get("sImgUrl").toString());

                                sastojakList.add(sastojak);
                            }

                            String[] array=sastojci.getText().toString().split(",",0);
                            List<String> lista= Arrays.asList(array);
                            List<String> lista1= new ArrayList<>(lista);
                            Log.d("TAG", "lista1 "+lista1);

                            Sastojak[] nizSastojak = new Sastojak[sastojakList.size()];
                            sastojakList.toArray(nizSastojak);
                            List<Sastojak> nizSelektovanihSastojaka = new ArrayList<Sastojak>();
                            for (Sastojak sastojak : nizSastojak) {

                                if (lista1.contains(sastojak.getIme())) {
                                    nizSelektovanihSastojaka.add(sastojak);
                                }
                            }

                            adapterSastojci =new AdapterSastojci(nizSastojak,lista1,nizSelektovanihSastojaka);
                            Log.d("TAG", "onComplete: NIFABIFHBWEIHFBWHIR" + adapterSastojci.nizSelektovanih);

                            Log.d("TAG", "onComplete:SASTOJCI SASTOJCI " + adapterSastojci.getSelectedSastojci());


                            GridLayoutManager gridLayoutManager = new GridLayoutManager(PromenaRecepataActivity.this, 4, GridLayoutManager.VERTICAL, false);
                            dataList.setLayoutManager(gridLayoutManager);
                            dataList.setAdapter(adapterSastojci);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });

        slikaRecepta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
        });

        promeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                izmeniPodatkeRecepta();
            }
        });

    }


    public void izmeniPodatkeRecepta(){
        String ss="";
        for(String s1 : adapterSastojci.nizSelektovanih)
        {
            ss+=s1+",";
        }
        DocumentReference documentReference= docRef.collection("recepti").document(idRecepta);
        documentReference.update(

                "Img", imageUrl,
                "naziv", nazivRecepta.getText().toString(),
                "priprema", pripremaRecepta.getText().toString(),
                "sastojci", ss
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Podaci uspešno ažurirani.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Greška prilikom ažuriranja podataka.", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if (resultCode == Activity.RESULT_OK) {
                imageUri=data.getData();
                slikaRecepta.setImageURI(imageUri);
                //mora se pamtiti u storage
                upload(imageUri);

            }
        }
    }

    public void upload(Uri imageUri)
    {

        int hours = new Time(System.currentTimeMillis()).getHours();
        //String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference fileref=storage.child("SlikeRecepata/").child(hours+".jpg");
        StorageTask task=fileref.putFile(imageUri);
        task.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull  Task task) throws Exception {
                if(!task.isSuccessful())
                    throw task.getException();
                return fileref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull  Task<Uri> task) {
                Uri downloaduri = task.getResult();
                imageUrl = downloaduri.toString();

            }

        });
    }






}