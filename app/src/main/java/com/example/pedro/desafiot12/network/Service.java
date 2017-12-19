package com.example.pedro.desafiot12.network;

import com.example.pedro.desafiot12.entities.AcoesListEntity;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pedro on 16/12/2017.
 */
public interface Service {

    @GET("sociais.json")
    Call<AcoesListEntity> getAcoes();


}
