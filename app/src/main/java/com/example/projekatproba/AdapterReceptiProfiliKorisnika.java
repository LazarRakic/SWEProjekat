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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterReceptiProfiliKorisnika extends RecyclerView.Adapter<ReceptProfiliHolder> {

    Context ctx;
    List<Recept> receptList;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    DocumentReference documentRef;

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


    public ReceptProfiliHolder(View itemView) {
        super(itemView);

        imageView= itemView.findViewById(R.id.slikaReceptaProfil);
        mTitle= itemView.findViewById(R.id.nazivReceptaProfil);
        mDescription= itemView.findViewById(R.id.opisReceptaProfil);

        mCardView= itemView.findViewById(R.id.cardViewReceptProfil);
    }
}