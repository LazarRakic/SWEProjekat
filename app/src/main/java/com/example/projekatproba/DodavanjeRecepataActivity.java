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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DodavanjeRecepataActivity extends AppCompatActivity {
    int idDokumenta=2;
    double nula= 0.0;
    Button prikazi;
    Button objavi;
    ImageView slika;
    EditText priprema;
    EditText naziv;
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
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    TimeUtility timeUtility;
    LocalDateTime now ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_recepata);


        now=LocalDateTime.now();
        timeUtility=new TimeUtility();
        prikazi=findViewById(R.id.prikaziSastojke);
        objavi=findViewById(R.id.objaviRecept);
        slika = findViewById(R.id.imageView3);
        naziv=findViewById(R.id.naziv_recepta);
        priprema = findViewById(R.id.pripremaPlainText);
        sastojci = findViewById(R.id.sastojciPlanText);
        storage= FirebaseStorage.getInstance().getReference();
        dataList= findViewById(R.id.listaSastojaka);


        titles= new ArrayList<>();
        images= new ArrayList<>();
        this.selektovaniSastojci=new ArrayList<String>();

        docRef.collection("recepti")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                    idDokumenta++;

                            }



                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }


                });

        objavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put("Img", imageUrl);
                dataToSave.put("brojmerenja",Double.toString(nula));
                dataToSave.put("datum",Long.toString(timeUtility.convertLocalDateTimeToLong(now)));
                dataToSave.put("idPublishera", uid);
                dataToSave.put("naziv", naziv.getText().toString());
                dataToSave.put("ocena",Double.toString(nula));
                dataToSave.put("priprema",priprema.getText().toString());
                dataToSave.put("sastojci",sastojci.getText().toString());
                dataToSave.put("reviewers","");
                //napravimo i da moze bez prikaza liste sastojaka da nam se sastojci ucitavaju (samo oni na koje je kliknuto)
                docRef.collection("recepti").document(Integer.toString(idDokumenta)).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("TAG", "Recept  je sačuvan! ");
                        Toast.makeText(DodavanjeRecepataActivity.this, "Recept je uspesno dodat ", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        //intent da predje na home activiti
                        //i dodaj toast

                        idDokumenta++;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DodavanjeRecepataActivity.this, "Recept nije dodat pokusajte ponovo! ", Toast.LENGTH_LONG).show();
                        Log.w("TAG", "Recept nije sačuvan u bazi! ", e);
                    }
                });
            }
        });
        prikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="";
                for(String s1 : adapter.nizSelektovanih)
                {
                    s+=s1+",";
                }
                sastojci.setText(s);
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if (resultCode == Activity.RESULT_OK) {
                imageUri=data.getData();
                slika.setImageURI(imageUri);
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


                /*/dodavanje novog sastojka klikom na dugme

                Map<String, Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put("Img", imageUrl);
                //dataToSave.put("datum", surnameVal);
                dataToSave.put("idPublishera", uid);
                //dataToSave.put("naziv", priprema.getText());
                //dataToSave.put("sastojci",sastojci.getText());
                docRef.collection("recepti").document(Integer.toString(idDokumenta)).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Recept  je sačuvan! ");
                        idDokumenta++;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.w("TAG", "Recept nije sacuvan u bazi! ", e);
                    }
                });*/




            }

        });
    }



}