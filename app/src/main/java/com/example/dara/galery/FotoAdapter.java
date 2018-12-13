package com.example.dara.galery;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoHolder>{
    private ArrayList<Foto> dataFoto;
    OnItemClicked clickHandler;


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
        holder.judul.setText(String.valueOf(foto.nama));
        holder.viewLokasi.setText(String.valueOf(foto.lat+" "+ foto.lng));
        String url = "http://parit.store/galery/public/foto/" + foto.getPath_foto();

        Glide.with(holder.itemView)
                .load(url)
                .into(holder.imgFoto);

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
        TextView viewLokasi;

        public FotoHolder(@NonNull View itemView){
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            imgFoto = itemView.findViewById(R.id.foto);
            viewLokasi = itemView.findViewById(R.id.viewLokasi);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Foto foto= dataFoto.get(getAdapterPosition());
                    clickHandler.ItemClicked(foto);
                }
            });
        }

    }

    public interface OnItemClicked{
        void ItemClicked(Foto foto);

    }


    public void setClickHandler(OnItemClicked clickHandler) {
        this.clickHandler = clickHandler;
    }

}
