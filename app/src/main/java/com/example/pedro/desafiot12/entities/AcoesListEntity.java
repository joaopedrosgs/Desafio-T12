package com.example.pedro.desafiot12.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 16/12/2017.
 */

public class AcoesListEntity {

    @SerializedName("acoes_sociais")
    @Expose
    private List<AcaoSocial> acoesSociais = null;

    public AcoesListEntity() {
        this.acoesSociais = new ArrayList<>();
    }

    public List<AcaoSocial> getAcoesSociais() {
        return acoesSociais;
    }
}
