package com.example.projekatproba;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterSastojci extends RecyclerView.Adapter<AdapterSastojci.SastojciHolder1> {
    private Sastojak[] sSastojci;
    private List<Sastojak> selectedSastojci;
    List<String> nizSelektovanih;

    public AdapterSastojci(Sastojak[] sastojaks) {
        this.sSastojci = sastojaks;

        selectedSastojci = new ArrayList<>();

        this.nizSelektovanih=new ArrayList<>();
    }
    public AdapterSastojci(Sastojak[] sastojaks, List<String> nizSelektovanih, List<Sastojak> sastojakList)
    {
        this.sSastojci = sastojaks;

        selectedSastojci = new ArrayList<>();

        this.nizSelektovanih=new ArrayList<>();

        this.nizSelektovanih=nizSelektovanih;

        this.selectedSastojci=sastojakList;

    }

    @Override
    public SastojciHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid_layout, parent, false);

        SastojciHolder1 holder = new SastojciHolder1(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull SastojciHolder1 holder, @SuppressLint("RecyclerView") int position) {
        //holder.imvSong.setImageResource(R.drawable.standardartwork);
        holder.naziv.setText(sSastojci[position].getIme());
        Picasso.get().load(String.valueOf(sSastojci[position].getUrlSlike())).into(holder.imvSastojak);

        if (!selectedSastojci.contains(sSastojci[position])) {
            holder.mCardView.setBackgroundColor(Color.parseColor("#2196F3"));
        }
        else {
            holder.mCardView.setBackgroundColor(Color.parseColor("#008000"));
        }



        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (selectedSastojci.contains(sSastojci[pos])) {
                    selectedSastojci.remove(sSastojci[pos]);
                    holder.mCardView.setBackgroundColor(Color.parseColor("#2196F3"));
                    for(int i=0;i<nizSelektovanih.size();i++ ){
                        if( nizSelektovanih.get(i).equals(holder.naziv.getText().toString()))
                        {
                            nizSelektovanih.remove(i);
                            break;
                        }
                    }

                    Log.d("TAG", "NIZ IZBACEN  SELEKTOVANIH "+ nizSelektovanih);
                }
                else {
                    selectedSastojci.add(sSastojci[pos]);
                    holder.mCardView.setBackgroundColor(Color.parseColor("#008000"));
                    nizSelektovanih.add(holder.naziv.getText().toString());
                    Log.d("TAG", "NIZ SELEKTOVANIH "+ nizSelektovanih);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sSastojci != null ? sSastojci.length : 0;
    }

//    public Sastojak[] getselectedSastojci() {
//        Sastojak[] sastojci = new Sastojak[selectedSastojci.size()];
//
//        return selectedSastojci.toArray(sastojci);
//    }

    public Sastojak[] getsSastojci() {
        return sSastojci;
    }


    public List<Sastojak> getSelectedSastojci() {
        return selectedSastojci;
    }

    public class SastojciHolder1 extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        ImageView imvSastojak;
        TextView naziv;
        CardView mCardView;

        public SastojciHolder1(View layout) {
            super(layout);

            constraintLayout = (ConstraintLayout) layout;

            imvSastojak = (ImageView) layout.findViewById(R.id.imageView2);
            naziv = (TextView) layout.findViewById(R.id.textView2);

            mCardView= itemView.findViewById(R.id.cardViewSastojak);


        }
    }
}
