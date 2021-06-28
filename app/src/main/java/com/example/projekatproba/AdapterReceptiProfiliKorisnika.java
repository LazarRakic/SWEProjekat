package com.example.projekatproba;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdapterReceptiProfiliKorisnika extends RecyclerView.Adapter<ReceptProfiliHolder> {
    public static final String TAG = "AdapterReceptiProfiliKorisnika";
    Context ctx;
    List<Recept> receptList;
    FirebaseAuth baseAuth;
    FirebaseUser user;
    Boolean userPosetilac;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    DocumentReference documentRef;

    float rateValue;

    public AdapterReceptiProfiliKorisnika(Context ctx, List<Recept> receptList) {
        this.ctx = ctx;
        this.receptList = receptList;
    }
    @NonNull
    @Override
    public ReceptProfiliHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_grid_recept_profili, viewGroup, false);

        return new ReceptProfiliHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceptProfiliHolder receptProfiliHolder, int position) {

        Picasso.get().load(String.valueOf(receptList.get(position).getSlika())).into( receptProfiliHolder.imageView);
        receptProfiliHolder.mTitle.setText(receptList.get(position).getNaziv());
        receptProfiliHolder.mDescription.setText(receptList.get(position).getPriprema());
        if(receptList.get(position).getProsecnaOCENA().equals("0.0")) {
            String stringOcena = "OCENA";
            receptProfiliHolder.ocena.setText(stringOcena);
        }
        else{
            receptProfiliHolder.ocena.setText(receptList.get(position).getProsecnaOCENA());
        }

        receptProfiliHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ctx, ReceptDetaljActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ArrayList<String> lista= new ArrayList<>();
                lista.add(receptList.get(receptProfiliHolder.getAdapterPosition()).getSlika());
                lista.add(receptList.get(receptProfiliHolder.getAdapterPosition()).getPriprema());
                lista.add(receptList.get(receptProfiliHolder.getAdapterPosition()).getSastojci());
                lista.add(receptList.get(receptProfiliHolder.getAdapterPosition()).getNaziv());
                lista.add(receptList.get(receptProfiliHolder.getAdapterPosition()).getUsernameKorisnika());
                intent.putStringArrayListExtra("Lista", lista);
                ctx.startActivity(intent);
            }
        });

        baseAuth= FirebaseAuth.getInstance();
        userPosetilac=baseAuth.getCurrentUser().isAnonymous();

        if(userPosetilac ==true){
            receptProfiliHolder.ocenite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, "Morate se registrovati kako biste imali pristup! ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            receptProfiliHolder.ocenite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rateValue=receptProfiliHolder.ratingBar.getRating();
                    DocumentReference documentReference=docRef.collection("recepti").document(receptList.get(receptProfiliHolder.getAdapterPosition()).getIdRecepta());
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String[] revieweri = document.getString("reviewers").split(",",-1);
                                    String trenutniUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    DocumentReference documentRef=docRef.collection("korisnici").document(trenutniUser);
                                    documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                DocumentSnapshot doc = task.getResult();
                                                if(doc.exists()){
                                                    String trenutniUsername= doc.getString("username");

                                                    if(!document.getString("idPublishera").equals(trenutniUser))
                                                    {
                                                        int counter = 0;
                                                        for (int i = 0; i < revieweri.length; i++) {
                                                            if (trenutniUser.equals(revieweri[i])) {
                                                                break;
                                                            }
                                                            counter++;
                                                        }
                                                        if (counter == revieweri.length) {

                                                            try{
                                                                float br_merenja = Float.parseFloat(Objects.requireNonNull(document.getString("brojmerenja")));
                                                                float ocena_prosecna = Float.parseFloat(Objects.requireNonNull(document.getString("ocena")));
                                                                br_merenja++;
                                                                float konacna_ocena = (ocena_prosecna + rateValue) / br_merenja;
                                                                float konacna_ocena_dve_decimale = (float) (Math.round(konacna_ocena * 100.0) / 100.0);
                                                                receptProfiliHolder.ocena.setText(String.valueOf(konacna_ocena_dve_decimale));
                                                                String newReviewers =  document.getString("reviewers")+","+trenutniUser;
                                                                String newReviewersUsername =  document.getString("reviewersUsername")+","+trenutniUsername;
                                                                updateRecept(konacna_ocena_dve_decimale, br_merenja, receptList.get(receptProfiliHolder.getAdapterPosition()).getIdRecepta(),newReviewers, newReviewersUsername);
//                                                sendNotification(); //TODO SEND NOTIFICATION
                                                            }
                                                            catch (NullPointerException e){
                                                                Log.d(TAG,e.getMessage());
                                                            }
                                                        } else {
                                                            Toast.makeText(ctx, "Ne možete ostaviti recenziju više od jednog puta!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(ctx, "Ne možete ostaviti recenziju za vaš recept!", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                                else {
                                                    Log.d("TAG:", "No such document");
                                                }
                                            }
                                            else
                                            {
                                                Log.d("TAG", "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                                else {
                                    Log.d("TAG:", "No such document");
                                }
                            }
                            else
                            {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    });
                }
            });
        }



    }

//    //TODO SEND NOTIFICATION
//    public void sendNotification(){
//
//    }

    @Override
    public int getItemCount() {
        return receptList.size();
    }


    public void updateRecept(float konacna_ocena,float br_merenja,String document,String newReviewers, String newReviewersUsername)
    {
        DocumentReference receptiUpdate = docRef.collection("recepti").document(document);

        receptiUpdate
                .update("ocena", String.valueOf(konacna_ocena),"brojmerenja",String.valueOf(br_merenja),"reviewers",newReviewers,"reviewersUsername",newReviewersUsername)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

}

class ReceptProfiliHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mDescription;
    CardView mCardView;
    Button ocenite;
    TextView ocena;
    RatingBar ratingBar;

    public ReceptProfiliHolder(View itemView) {
        super(itemView);

        imageView= itemView.findViewById(R.id.slikaReceptaProfil);
        mTitle= itemView.findViewById(R.id.nazivReceptaProfil);
        mDescription= itemView.findViewById(R.id.opisReceptaProfil);
        ocenite=itemView.findViewById(R.id.button_recenzija);
        ocena=itemView.findViewById(R.id.prosecna_recenzija);
        ratingBar=itemView.findViewById(R.id.rating_bar);
        mCardView= itemView.findViewById(R.id.cardViewReceptProfil);
    }
}