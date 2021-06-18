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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterRecepti extends RecyclerView.Adapter<ReceptHolder> {

    Context ctx;
    List<Recept> receptList;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    DocumentReference documentRef;

    public AdapterRecepti(Context ctx, List<Recept> receptList) {
        this.ctx = ctx;
        this.receptList = receptList;
    }
    @NonNull
    @Override
    public ReceptHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_grid_recept, viewGroup, false);

        return new ReceptHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceptHolder receptHolder, int position) {


        Picasso.get().load(String.valueOf(receptList.get(position).getSlika())).into( receptHolder.imageView);
        receptHolder.mTitle.setText(receptList.get(position).getNaziv());
        receptHolder.mDescription.setText(receptList.get(position).getPriprema());

        baseAuth= FirebaseAuth.getInstance();

        receptHolder.deleteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert= new AlertDialog.Builder(v.getContext());
                alert.setTitle("Da li ste sigurni da želite da obrišete recept?");
                alert.setMessage("Brisanjem u potpunosti uklanjate recept iz sistema.");
                alert.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        docRef.collection("recepti").document(receptList.get(receptHolder.getAdapterPosition()).getIdRecepta()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    docRef.collection("recepti").document(receptList.get(receptHolder.getAdapterPosition()).getIdRecepta())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error deleting documentAAAAAAAAAAAAAAAAAAAAAAAAA", e);
                                                }
                                            });

                                    receptList.remove(receptList.get(receptHolder.getAdapterPosition()));

                                    Toast.makeText(v.getContext(), "Recept je obrisan", Toast.LENGTH_LONG).show();
                                    Intent intent= new Intent(ctx, HomeActivity.class);
                                    ctx.startActivity(intent);
                                }
                                else {
                                    Toast.makeText(v.getContext(), "Greška prilikom brisanja recepta.", Toast.LENGTH_LONG).show();
                                    Log.w("TAG", "Error deleting documentAAAAAAAAAAAAAAAAAAAAAAAAA");
                                }
                            }
                        });
                    }
                });

                alert.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= alert.create();
                alertDialog.show();
            }
        });

        receptHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ctx, ReceptDetaljActivity.class);
                ArrayList<String> lista= new ArrayList<>();
                lista.add(receptList.get(receptHolder.getAdapterPosition()).getSlika());
                lista.add(receptList.get(receptHolder.getAdapterPosition()).getPriprema());
                intent.putStringArrayListExtra("Lista", lista);
                ctx.startActivity(intent);
            }
        });

        receptHolder.ocenite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= String.valueOf(receptHolder.ratingBar.getRating());
                Toast.makeText(ctx.getApplicationContext(), s+"Star", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return receptList.size();
    }
}
//Ovo se menja :D
class ReceptHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mDescription;
    CardView mCardView;
    Button deleteButt;
    Button updateButt;
    RatingBar ratingBar;
    Button ocenite;


    public ReceptHolder(View itemView) {
        super(itemView);

        imageView= itemView.findViewById(R.id.slikaRecepta);
        mTitle= itemView.findViewById(R.id.nazivRecepta);
        mDescription= itemView.findViewById(R.id.opisRecepta);
        deleteButt= itemView.findViewById(R.id.obrisi_recept);
        updateButt=itemView.findViewById(R.id.promeni_recept);
        ratingBar=itemView.findViewById(R.id.rating_bar);
        ocenite=itemView.findViewById(R.id.button_recenzija);
        mCardView= itemView.findViewById(R.id.cardViewRecept);
    }
}
