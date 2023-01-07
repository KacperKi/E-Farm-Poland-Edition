package com.example.e_farmpolandedition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class uprawyRecyclerList extends RecyclerView.Adapter<uprawyRecyclerList.ViewHolderUprawy> {

    public class ViewHolderUprawy extends RecyclerView.ViewHolder{
        TextView idUprawy, nazwaUprawy, nazwaRosliny, dataRozpoczecia, powierzchniaUprawy;
        LinearLayout rowLayout;

        public ViewHolderUprawy(@NonNull View itemView) {
            super(itemView);
            idUprawy = itemView.findViewById(R.id.idUprawy);
            nazwaUprawy = itemView.findViewById(R.id.nazwaUprawy);
            nazwaRosliny = itemView.findViewById(R.id.nazwaRosliny);
            dataRozpoczecia = itemView.findViewById(R.id.dataRozpoczecia);
            powierzchniaUprawy = itemView.findViewById(R.id.powierzchniaUprawy);
            rowLayout = itemView.findViewById(R.id.main_lay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION) {
                        Toast.makeText(
                                view.getContext(),
                                String.valueOf(position),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private ArrayList<uprawa> uprawaLista;

    public uprawyRecyclerList(ArrayList<uprawa> uprawaLista) {
        this.uprawaLista = uprawaLista;
    }

    @NonNull
    @Override
    public ViewHolderUprawy onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recycler_uprawa,parent,false);
        return new ViewHolderUprawy(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUprawy holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),
                android.R.anim.slide_out_right);

        String nazwaUprawy = uprawaLista.get(position).getName();
        String nazwaRosliny = uprawaLista.get(position).getPlantName();
        String dataRozpoczecia = uprawaLista.get(position).getDataRozpoczecia();
        String powierzchniaUprawy = uprawaLista.get(position).getSurface();

        uprawa pojedynczaUprawa = uprawaLista.get(position);

        holder.idUprawy.setText(String.valueOf(position));
        holder.nazwaUprawy.setText(nazwaUprawy);
        holder.nazwaRosliny.setText(nazwaRosliny);
        holder.dataRozpoczecia.setText(dataRozpoczecia);
        holder.powierzchniaUprawy.setText(powierzchniaUprawy);

//        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //after click lay on recycler
//                Toast.makeText(view.getContext(),
//                        uprawaLista.get(Integer.parseInt(holder.idUprawy.getText().toString())).getDescription()
//                        ,Toast.LENGTH_LONG).show();
//            }
//        });
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return uprawaLista.size();
    }
}
