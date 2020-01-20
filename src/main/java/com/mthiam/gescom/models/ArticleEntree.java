package com.mthiam.gescom.models;



public class ArticleEntree {

    private long idArticle;


    private String codearticle;


    private String designation;


    private String reference;


    private long prixBase;

    private Categorie categorie;

    private int quantite;

    private long pu;


    private int qteInitiale;


    private int qteRestante;


    private int qteAlerte;

    public long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(long idArticle) {
        this.idArticle = idArticle;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCodearticle() {
        return codearticle;
    }

    public void setCodearticle(String codearticle) {
        this.codearticle = codearticle;
    }

    public long getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(long prixBase) {
        this.prixBase = prixBase;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getQteInitiale() {
        return qteInitiale;
    }

    public void setQteInitiale(int qteInitiale) {
        this.qteInitiale = qteInitiale;
    }

    public int getQteRestante() {
        return qteRestante;
    }

    public void setQteRestante(int qteRestante) {
        this.qteRestante = qteRestante;
    }

    public int getQteAlerte() {
        return qteAlerte;
    }

    public void setQteAlerte(int qteAlerte) {
        this.qteAlerte = qteAlerte;
    }
}
