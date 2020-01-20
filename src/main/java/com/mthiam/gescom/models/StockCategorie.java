package com.mthiam.gescom.models;

import java.io.Serializable;

public class StockCategorie implements Serializable {

    private String nomCategorie;

    private int nbArticles;

    public StockCategorie() {
    }

    public StockCategorie(String nomCategorie, int nbArticles) {
        this.nomCategorie = nomCategorie;
        this.nbArticles = nbArticles;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public int getNbArticles() {
        return nbArticles;
    }

    public void setNbArticles(int nbArticles) {
        this.nbArticles = nbArticles;
    }
}
