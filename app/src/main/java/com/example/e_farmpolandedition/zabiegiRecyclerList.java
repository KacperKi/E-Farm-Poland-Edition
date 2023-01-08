package com.example.e_farmpolandedition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class zabiegiRecyclerList extends RecyclerView.Adapter<zabiegiRecyclerList.ViewHolderZabiegi> {

    public class ViewHolderZabiegi extends RecyclerView.ViewHolder{
        TextView ID, nazwaZabieguLista, dataPlanowanegoZabiegu;

        public ViewHolderZabiegi(@NonNull View itemView) {
            super(itemView);

            ID = itemView.findViewById(R.id.ID);
            nazwaZabieguLista = itemView.findViewById(R.id.nazwaZabieguLista);
            dataPlanowanegoZabiegu = itemView.findViewById(R.id.dataPlanowanegoZabiegu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                        zabieg w = listaZabiegow.get(position);
                        String i = "Nazwa zabiegu: " + w.getNazwaZabiegu() +
                                "\nData zabiegu: " + w.getDataRozpoczecia() +
                                "\nOkres karencji: " + w.getOkresKarencji() +
                                "\nUprawa wiązana: " + w.getUprawaUsera() +
                                "\nDawka: " + w.getDawka() +
                                "\nKoszt: " + w.getKoszt() +
                                "\nŚrodek: " + w.getRodzajSrodka();

                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Informacje!")
                                .setMessage(i)
                                .setPositiveButton("Zamknij", null)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                }
            });
        }
    }

    private ArrayList<zabieg> listaZabiegow;

    public zabiegiRecyclerList(ArrayList<zabieg> zabiegLista) {
        this.listaZabiegow = zabiegLista;
    }

    @NonNull
    @Override
    public ViewHolderZabiegi onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insert_zabieg_row,parent,false);
        return new ViewHolderZabiegi(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull zabiegiRecyclerList.ViewHolderZabiegi holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),
                android.R.anim.slide_out_right);

        holder.ID.setText(String.valueOf(position+1));
        holder.nazwaZabieguLista.setText(listaZabiegow.get(position).getNazwaZabiegu());
        holder.dataPlanowanegoZabiegu.setText(listaZabiegow.get(position).getDataRozpoczecia());

        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return listaZabiegow.size();
    }

    public void updateArrayList(ArrayList<zabieg> t){
        this.listaZabiegow = t;
    }
}
