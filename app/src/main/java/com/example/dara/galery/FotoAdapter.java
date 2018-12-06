package com.example.dara.galery;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoHolder>{
    private ArrayList<Foto> dataFoto;
    public void setDataFoto(ArrayList<Foto> data){
        dataFoto = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mengambil data dalam bentuk objek
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_galery, parent, false);
        FotoHolder holder = new FotoHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FotoHolder holder, int position) {
        Foto foto = dataFoto.get(position);
        holder.judul.setText(String.valueOf(foto.judul));
//        Glide.with()
//                .load(foto.foto)
//                .override(350, 550)
//                .into(holder.imgFoto);

    }

    @Override
    public int getItemCount() {
        //mengembalikan jumlah data yang dimiliki
        if(dataFoto!=null){
            return dataFoto.size();
        }else {
            return 0;
        }
    }

    // Inner CLASS
    public class FotoHolder extends RecyclerView.ViewHolder{
        TextView judul;
        ImageView imgFoto;

        public FotoHolder(@NonNull View itemView){
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            imgFoto = itemView.findViewById(R.id.foto);
        }

    }
}
