package com.mthiam.gescom.models;

public class ArticleVente {
    private Stock stock;

    private int quantite;

    private long pu;

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public long getPu() {
        return pu;
    }

    public void setPu(long pu) {
        this.pu = pu;
    }
}
