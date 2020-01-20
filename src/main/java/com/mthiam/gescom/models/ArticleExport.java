package com.mthiam.gescom.models;

public class ArticleExport {


    private String codearticle;


    private String designation;


    private String reference;


    private long prixBase;

    private String categorie;

    private int qteEnStock;

    private int qteAlerte;

    public String getCodearticle() {
        return codearticle;
    }

    public void setCodearticle(String codearticle) {
        this.codearticle = codearticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(long prixBase) {
        this.prixBase = prixBase;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getQteEnStock() {
        return qteEnStock;
    }

    public void setQteEnStock(int qteEnStock) {
        this.qteEnStock = qteEnStock;
    }

    public int getQteAlerte() {
        return qteAlerte;
    }

    public void setQteAlerte(int qteAlerte) {
        this.qteAlerte = qteAlerte;
    }
}
