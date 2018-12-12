package com.example.dara.galery.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dara.galery.DatabaseHandler;
import com.example.dara.galery.Foto;
import com.example.dara.galery.FotoAdapter;
import com.example.dara.galery.MainActivity;
import com.example.dara.galery.Model.FotoList;
import com.example.dara.galery.R;
import com.example.dara.galery.RetrofitClientInstance;
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


public class galeryFragment extends Fragment {

    RecyclerView rvListFoto;
    FotoAdapter fotoAdapter;

    DatabaseHandler handler;
    Foto dataFoto;
    ArrayList<Foto> fotoList;
    public static final  String urlData = "https://parit.store/galery/";


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


        fotoList = new ArrayList<>();
        if(((MainActivity)getActivity()).konekkah()){
//            TmdbClient service = RetrofitClientInstance.getRetrofitInstance().create(TmdbClient.class);
//            Call<FotoList> call = service.getAllGaleries();
//            Log.e("call", String.valueOf(call));
//            call.enqueue(new Callback<FotoList>() {
//                @Override
//                public void onResponse(Call<FotoList> call, Response<FotoList> response) {
//                    List<Foto> listFotoItem = response.body().results;
//                    fotoAdapter.setDataFoto(new ArrayList<Foto>(listFotoItem));
//                }
//
//                @Override
//                public void onFailure(Call<FotoList> call, Throwable t) {
//                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//
//                }
//            });
            getFoto();
        }else{
            fotoAdapter.setDataFoto(getDataFoto(handler,dataFoto));
        }
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

    private void getDataVolley(){

        final StringRequest request = new StringRequest(Request.Method.GET, urlData,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ambilData(response);
                    }
                },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                );
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void ambilData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);

            //ini toast untuk menampilkan pesan sukses dari json
            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            // ini utk mengambil attribute array yg ada di json (yaitu attribute data)
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            //looping utk array
            for(int i=0; i<jsonArray.length(); i++){
                //get json berdasarkan banyaknya data (index i)
                JSONObject objekFoto = jsonArray.getJSONObject(i);

                //get data berdasarkan attribte yang ada dijsonnya (harus sama)
                String nama = objekFoto.getString("nama");
                String deskripsi = objekFoto.getString("deskripsi");
                String path_foto = objekFoto.getString("path_foto");
                Double lat = Double.valueOf(objekFoto.getString("lat"));
                Double lng = Double.valueOf(objekFoto.getString("lng"));

                //add data ke modelnya
                Foto fotoModel = new Foto();
                fotoModel.setNama(nama);
                fotoModel.setDeskripsi(deskripsi);
                fotoModel.setPath_foto(path_foto);
                fotoModel.setLat(lat);
                fotoModel.setLng(lng);

                //add model ke list
                fotoList.add(fotoModel);

                //passing data list ke adapter
                fotoAdapter.setDataFoto(fotoList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFoto()  {

        if(((MainActivity)getActivity()).konekkah()){

            String API_BASE_URL = "https://galeryonline.herokuapp.com";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TmdbClient client = retrofit.create(TmdbClient.class);

            Call<FotoList> call = client.getAllGaleries();
            call.enqueue(new Callback<FotoList>() {
                @Override
                public void onResponse(Call<FotoList> call, Response<FotoList> response) {
                    Toast.makeText(getActivity(), "Berhasil", Toast.LENGTH_SHORT).show();
                    FotoList fotoList =response.body();
                    List<Foto> listFotoItem = fotoList.results;
                    if(listFotoItem == null){
                        Toast.makeText(getContext(), "Maaf, Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        saveToDb(listFotoItem);
                        Log.d("listFoto", String.valueOf(listFotoItem));
                        fotoAdapter.setDataFoto(new ArrayList<Foto>(listFotoItem));
                    }
                }

                @Override
                public void onFailure(Call<FotoList> call, Throwable t) {
                    Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }

                });
        }else{
                  fotoAdapter.setDataFoto(new ArrayList<Foto>(handler.getAllDatas()));
            }
        }

    public void saveToDb(List<Foto> fotoList){
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


}
