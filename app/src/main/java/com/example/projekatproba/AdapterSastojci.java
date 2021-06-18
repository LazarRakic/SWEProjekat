package com.example.projekatproba;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdapterSastojci extends RecyclerView.Adapter<SastojciHolder> {

    private static int lastClickedPosition = -1;
    private int selectedItem;

    Context ctx;
    List<Sastojak> sastojakList;
    List<String> nizSelektovanih;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    private int lastChecked = -1;

    public AdapterSastojci(Context ctx, List<Sastojak> sastojakList) {
        this.ctx = ctx;
        this.sastojakList = sastojakList;
        this.nizSelektovanih=new ArrayList<>();
        this.selectedItem=-1;
    }
    @NonNull
    @Override
    public SastojciHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_grid_layout, viewGroup, false);

        return new SastojciHolder(mView);
    }

    @Override
    

        Picasso.get().load(String.valueOf(sastojakList.get(position).getUrlSlike())).into(sastojciHolder.imageView);
        sastojciHolder.mTitle.setText(sastojakList.get(position).getIme());

        sastojciHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousItem = selectedItem;
                selectedItem = position;

                notifyItemChanged(previousItem);
                notifyItemChanged(position);

                Pattern p=Pattern.compile(sastojciHolder.mTitle.getText().toString());
                String s="";
                for(String pom : nizSelektovanih){
                    s+=pom;
                }
                Matcher m=p.matcher(s);
                if(!m.find()) {
                    nizSelektovanih.add(sastojciHolder.mTitle.getText().toString());
                    //sastojciHolder.mCardView.findViewById(R.id.constraintLayout2).setBackgroundColor(Color.parseColor("#008000"));
                    Log.d("TITLE SASTOJKA", sastojciHolder.mTitle.getText().toString());
                    Log.d("NIZ SELEKTOVANIH", nizSelektovanih.toString());
                }
                else{
                    for(int i=0;i<nizSelektovanih.size();i++ ){
                        if( nizSelektovanih.get(i).equals(sastojciHolder.mTitle.getText().toString())){
                            nizSelektovanih.remove(i);
                           // sastojciHolder.mCardView.findViewById(R.id.constraintLayout2).setBackgroundColor(Color.parseColor("#2196F3"));
                            break;
                        }
                    }
                    Log.d("NIZ SELEKTOVANIH", nizSelektovanih.toString());
                }

            }
        });

//        sastojciHolder.mCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Pattern p=Pattern.compile(sastojciHolder.mTitle.getText().toString());
//                String s="";
//                for(String pom : nizSelektovanih){
//                    s+=pom;
//                }
//                Matcher m=p.matcher(s);
//                if(!m.find()) {
//                    nizSelektovanih.add(sastojciHolder.mTitle.getText().toString());
//                    sastojciHolder.mCardView.findViewById(R.id.constraintLayout2).setBackgroundColor(Color.parseColor("#008000"));
//                    Log.d("TITLE SASTOJKA", sastojciHolder.mTitle.getText().toString());
//                    Log.d("NIZ SELEKTOVANIH", nizSelektovanih.toString());
//                }
//                else{
//                    for(int i=0;i<nizSelektovanih.size();i++ ){
//                        if( nizSelektovanih.get(i).equals(sastojciHolder.mTitle.getText().toString())){
//                            nizSelektovanih.remove(i);
//                            sastojciHolder.mCardView.findViewById(R.id.constraintLayout2).setBackgroundColor(Color.parseColor("#2196F3"));
//                            break;
//                        }
//                    }
//                    Log.d("NIZ SELEKTOVANIH", nizSelektovanih.toString());
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return sastojakList.size();
    }
}
//Ovo se menja :D
class SastojciHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle;
    CardView mCardView;


    public SastojciHolder(View itemView) {
        super(itemView);

        imageView= itemView.findViewById(R.id.imageView2);
        mTitle= itemView.findViewById(R.id.textView2);

        mCardView= itemView.findViewById(R.id.cardViewSastojak);
    }
}

