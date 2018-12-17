package com.example.dara.galery.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dara.galery.DatabaseHandler;
import com.example.dara.galery.DetailActivity;
import com.example.dara.galery.Foto;
import com.example.dara.galery.FotoAdapter;
import com.example.dara.galery.MainActivity;
import com.example.dara.galery.R;
import com.example.dara.galery.TmdbClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class galeryFragment extends Fragment implements FotoAdapter.OnItemClicked {

    static RecyclerView rvListFoto;
    static FotoAdapter fotoAdapter;
    static ProgressBar waiting;

    static DatabaseHandler handler;
    static Foto dataFoto;
    ArrayList<Foto> fotoList;
    static Activity activity;
    public static final  String urlData = "https://parit.store/galery/";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
         handler = new DatabaseHandler(context);
         dataFoto = new Foto();
    }

    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galery, container, false);
        waiting=view.findViewById(R.id.progressBar);
        fotoAdapter = new FotoAdapter();
        fotoAdapter.setDataFoto(getDataFoto(handler,dataFoto));
        fotoAdapter.setClickHandler(this);

        rvListFoto = view.findViewById(R.id.list_galery);
        rvListFoto.setAdapter(fotoAdapter);
        RecyclerView.LayoutManager layoutManager;

        rvListFoto.setVisibility(view.INVISIBLE);
        waiting.setVisibility(view.VISIBLE);

        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().
                heightPixels)
        {
            Toast.makeText(activity,"Screen switched to Landscape mode",Toast.LENGTH_SHORT).show();
            layoutManager = new GridLayoutManager(getContext(),2);
        }
        else
        {
            Toast.makeText(activity,"Screen switched to Portrait mode",Toast.LENGTH_SHORT).show();
            layoutManager = new LinearLayoutManager(getContext());
        }


        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvListFoto.setLayoutManager(layoutManager);
        rvListFoto.setHasFixedSize(true);

        if(((MainActivity)getActivity()).konekkah()){
             getFoto();
        }else{
//            fotoAdapter.setDataFoto(getDataFoto(handler,dataFoto));
        }

        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("status_fragment",1);
        editor.commit();
        return view;
    }


    public ArrayList<Foto> getDataFoto(DatabaseHandler handler, Foto fotos){
        ArrayList<Foto> tempData = new ArrayList<>();

        List<Foto> list = handler.getAllDatas();
        for(Foto foto : list){
            String log = "ID : " + foto.getId() + "\n" +
                    "NAME : " + foto.getNama() + "\n" +
                    "DESKRIPSI : " + foto.getDeskripsi() + "\n" +
                    "PATH : " + foto.getPath_foto() + "\n" +
                    "LAT: " + foto.getLat();
            System.out.println(log);

            //add to list
            tempData.add(new Foto(foto.getId(),foto.getNama(),foto.getPath_foto(), foto.getDeskripsi(), foto.getLat(), foto.getLng()));

            //set to the model
            fotos.setId(fotos.getId());
            fotos.setNama(fotos.getNama());
            fotos.setDeskripsi(fotos.getDeskripsi());
            fotos.setPath_foto(fotos.getPath_foto());
            fotos.setLat(fotos.getLat());
        }
        return tempData;
    }


    public static void getFoto()  {

        waiting.setVisibility(View.VISIBLE);
        rvListFoto.setVisibility(View.INVISIBLE);

        TmdbClient client = (new Retrofit.Builder()
                .baseUrl("http://parit.store/galery/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build())
                .create(TmdbClient.class);



        Call<List<Foto>> call = client.getAllGaleries();
        call.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call,  Response<List<Foto>> response) {
                waiting.setVisibility(View.INVISIBLE);
                rvListFoto.setVisibility(View.VISIBLE);

                Toast.makeText(activity, "Berhasil", Toast.LENGTH_SHORT).show();

                List<Foto> listFotoItem = response.body();
               if(listFotoItem == null){
                    Toast.makeText(activity, "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                }
                else{
                    saveToDb(listFotoItem);
                    fotoAdapter.setDataFoto(new ArrayList<Foto>(listFotoItem));
                }

            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {
                Toast.makeText(activity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void saveToDb(List<Foto> fotoList){
        for (Foto  m : fotoList){
            Foto record = new Foto();
            record.setNama(m.getNama());
            record.setDeskripsi(m.getDeskripsi());
            record.setPath_foto(m.getPath_foto());
            record.setLat(m.getLat());
            record.setLng(m.getLng());

            handler.tambah_foto(dataFoto);

        }
    }

    //klik foto

    @Override
    public void ItemClicked(Foto foto) {
        Toast.makeText(activity, "Item yang diklik adalah : " + foto.getNama(), Toast.LENGTH_SHORT).show();
        Intent detailIntent = new Intent(activity, DetailActivity.class);
        detailIntent.putExtra("key_foto_parcelable", foto);
        startActivity(detailIntent);
    }
}
