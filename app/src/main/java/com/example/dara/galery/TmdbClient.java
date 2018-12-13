package com.example.dara.galery;

import com.example.dara.galery.Model.FotoList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbClient {
    //mengambil data yang sdg tayang
    @GET("/galery/public/galery")
    Call<List<Foto>> getAllGaleries();



}
