package com.example.projekatproba;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterReceptiProfiliKorisnika extends RecyclerView.Adapter<ReceptProfiliHolder> {

    Context ctx;
    List<Recept> receptList;
    FirebaseAuth baseAuth;
    FirebaseUser user;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    DocumentReference documentRef;
    String stringgg, stringgg1;
    Double lng=5.0;
    Double lng1=4.0, lng2=10.0;
    Double konacno;

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


        baseAuth= FirebaseAuth.getInstance();
        receptProfiliHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ctx, ReceptDetaljActivity.class);
                ArrayList<String> lista= new ArrayList<>();
                lista.add(receptList.get(receptProfiliHolder.getAdapterPosition()).getSlika());
                lista.add(receptList.get(receptProfiliHolder.getAdapterPosition()).getPriprema());
                intent.putStringArrayListExtra("Lista", lista);
                //   intent.putExtra("Recept2", receptList.get(receptHolder.getAdapterPosition()).getSlika());
                //   intent.putExtra("Opis", receptList.get(receptHolder.getAdapterPosition()).getPriprema());
                ctx.startActivity(intent);
                //    ctx.startActivity(new Intent(ctx, ReceptDetaljActivity.class));
            }
        });

        receptProfiliHolder.ocenite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference=docRef.collection("recepti").document(receptList.get(receptProfiliHolder.getAdapterPosition()).getIdRecepta());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                stringgg=document.getString("ocena"); //ocena koja postoji vec u bazi
                                stringgg1=document.getString("brojmerenja");
                                String s= String.valueOf(receptProfiliHolder.ratingBar.getRating()); //nova ocena koju daje korisnik
                                Log.d("TAG:", "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGA " + stringgg);
                                if(stringgg.equals("")){
                                    stringgg="0";
                                    Log.d("TAG:", "DAAAAAAAAAAAAAAAAAA " + document.getData());
                                }
                                else
                                {
                                    lng=Double.parseDouble(stringgg);
                                }
                                if(stringgg1.equals("")){
                                    stringgg1="0";
                                }
                                else
                                {
                                    lng1=Double.parseDouble(stringgg1);
                                    Log.d("TAG:", "DAAAAAAAAAAAAAAAAAA " + document.getData());
                                }

                                Log.d("TAG:", "BBBBBBBBBBBBBBBBBBBB " + lng.toString());
                                lng2=Double.parseDouble(s);
                                Double novo=lng1 +1;
                                konacno= (lng+ lng2)/novo;

                                receptProfiliHolder.ocena.setText(konacno.toString());
                                Toast.makeText(ctx, "Vaša ocena je" +konacno.toString()+".", Toast.LENGTH_SHORT).show();
                                

                                Log.d("TAG:", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("TAG:", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return receptList.size();
    }
}
//Ovo se menja :D
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