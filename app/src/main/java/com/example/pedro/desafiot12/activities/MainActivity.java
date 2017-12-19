package com.example.pedro.desafiot12.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.pedro.desafiot12.entities.AcaoSocial;
import com.example.pedro.desafiot12.entities.AcoesListEntity;
import com.example.pedro.desafiot12.network.Api;
import com.example.pedro.desafiot12.R;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import shivam.developer.featuredrecyclerview.FeatureLinearLayoutManager;
import shivam.developer.featuredrecyclerview.FeaturedRecyclerView;


public class MainActivity extends AppCompatActivity {
    AcoesListEntity acoesListEntity;
    FeaturedRecyclerView featuredRecyclerView;
    NetworkInfo wifiCheck;
    Gson gson;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Api acoesApi = Api.getInstance();
        gson = new Gson();
        sharedPreferences = getSharedPreferences("acoes", MODE_PRIVATE);


        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiCheck.isConnected()) {
            acoesApi.getAcoes().enqueue(new Callback<AcoesListEntity>() {
                @Override
                public void onResponse(Call<AcoesListEntity> call, Response<AcoesListEntity> response) {
                    acoesListEntity = response.body();
                    if (acoesListEntity != null) {
                        acoesListEntity.getAcoesSociais().add(new AcaoSocial());
                        acoesListEntity.getAcoesSociais().add(new AcaoSocial()); // isso aqui é pra deixar um espaço em branco no final do meu recycler view adaptado
                        acoesListEntity.getAcoesSociais().add(new AcaoSocial());

                        AtivarRecycler();
                        String acoes_json = gson.toJson(acoesListEntity);
                        sharedPreferences.edit().putString("acoes_json", acoes_json).apply(); // Salva o json no shared preferences

                        findViewById(R.id.loading).setVisibility(View.GONE);

                    } else {
                        Log.d("Erro", "Falha ao pegar os dados do json");
                    }
                }

                @Override
                public void onFailure(Call<AcoesListEntity> call, Throwable t) {

                }

            });
        } else {
            if (sharedPreferences.contains("acoes_json")) {
                String acoes_json = sharedPreferences.getString("acoes_json", "[{}]"); // Pega o json pra carregar no AcoesListEntity
                acoesListEntity = gson.fromJson(acoes_json, AcoesListEntity.class);
                AtivarRecycler();
                findViewById(R.id.loading).setVisibility(View.GONE);
            } else {
                Intent intent = new Intent(this, BadInternetActivity.class);
                startActivity(intent);
            }
        }


    }

    private void AtivarRecycler() {
        featuredRecyclerView = (FeaturedRecyclerView) findViewById(R.id.featured_recycler_view);
        FeatureLinearLayoutManager layoutManager = new FeatureLinearLayoutManager(this);
        featuredRecyclerView.setLayoutManager(layoutManager);
        RAdapter_AcaoSocial adapter = new RAdapter_AcaoSocial(this, acoesListEntity);
        featuredRecyclerView.setAdapter(adapter);
    }
}
