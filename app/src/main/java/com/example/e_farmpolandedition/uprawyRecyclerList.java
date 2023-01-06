package com.example.e_farmpolandedition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        }
    }

    private ArrayList<uprawa> uprawaLista;

    public uprawyRecyclerList(ArrayList<uprawa> uprawaLista) {
        this.uprawaLista = uprawaLista;
    }

    @NonNull
    @Override
    public ViewHolderUprawy onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recycler_uprawa,parent,false);
        return new ViewHolderUprawy(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUprawy holder, int position) {
        String nazwaUprawy = uprawaLista.get(position).getName();
        String nazwaRosliny = uprawaLista.get(position).getPlantName();
        String dataRozpoczecia = uprawaLista.get(position).getDataRozpoczecia();
        String powierzchniaUprawy = uprawaLista.get(position).getSurface();

        holder.idUprawy.setText(String.valueOf(position));
        holder.nazwaUprawy.setText(nazwaUprawy);

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //after click lay on recycler
                Toast.makeText(view.getContext(), "H", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uprawaLista.size();
    }
}
