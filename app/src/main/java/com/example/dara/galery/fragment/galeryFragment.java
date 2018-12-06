package com.example.dara.galery.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dara.galery.DatabaseHandler;
import com.example.dara.galery.Foto;
import com.example.dara.galery.FotoAdapter;
import com.example.dara.galery.R;

import java.util.ArrayList;
import java.util.List;


public class galeryFragment extends Fragment {

    RecyclerView rvListFoto;
    FotoAdapter fotoAdapter;

    DatabaseHandler handler;
    Foto dataFoto;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         handler = new DatabaseHandler(context);
         dataFoto = new Foto();
    }

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galery, container, false);

        fotoAdapter = new FotoAdapter();
        fotoAdapter.setDataFoto(getDataFoto(handler,dataFoto));

        rvListFoto = view.findViewById(R.id.list_galery);
        rvListFoto.setAdapter(fotoAdapter);


        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvListFoto.setLayoutManager(layoutManager);
        rvListFoto.setHasFixedSize(true);



        return view;
    }
    public ArrayList<Foto> getDataFoto(DatabaseHandler handler, Foto fotos){
        ArrayList<Foto> tempData = new ArrayList<>();
//        tempData.add(new Foto(1,"Foto 1","https://www.google.co.id/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwih-tum7ILfAhVNVH0KHauSCRoQjRx6BAgBEAU&url=https%3A%2F%2Fblog.tiket.com%2Fpemandangan-alam-terindah-di-indonesia%2F&psig=AOvVaw3b6H4ceXWJ1fEJZUQjvJV0&ust=1543898488600934","ini foto","lokasinya disini"));
//        tempData.add(new Foto(2,"Foto 2","https://www.google.co.id/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwih-tum7ILfAhVNVH0KHauSCRoQjRx6BAgBEAU&url=https%3A%2F%2Fblog.tiket.com%2Fpemandangan-alam-terindah-di-indonesia%2F&psig=AOvVaw3b6H4ceXWJ1fEJZUQjvJV0&ust=1543898488600934","ini foto","lokasinya disini"));
//        return tempData;

        List<Foto> list = handler.getAllDatas();
        for(Foto foto : list){
            String log = "ID : " + foto.getId() + "\n" +
                    "NAME : " + foto.getNama() + "\n" +
                    "DESKRIPSI : " + foto.getDeskripsi() + "\n" +
                    "PATH : " + foto.getPath_foto() + "\n" +
                    "LOKASI : " + foto.getLokasi();
            System.out.println(log);

            //add to list
            tempData.add(new Foto(foto.getId(),foto.getNama(),foto.getPath_foto(), foto.getDeskripsi(), foto.getLokasi()));

            //set to the model
            fotos.setId(fotos.getId());
            fotos.setNama(fotos.getNama());
            fotos.setDeskripsi(fotos.getDeskripsi());
            fotos.setPath_foto(fotos.getPath_foto());
            fotos.setLokasi(fotos.getLokasi());
        }
        return tempData;
    }
}
