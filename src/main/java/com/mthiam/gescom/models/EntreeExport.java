package com.mthiam.gescom.models;

import java.util.Date;

public class EntreeExport {
    private long id;
    private String numeroEntree;
    private String numeroFacture;
    private Date entreeDate;
    private Long montantEntree;
    private Long restant;

    private String fournisseur;

    private String utilisateur;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumeroEntree() {
        return numeroEntree;
    }

    public void setNumeroEntree(String numeroEntree) {
        this.numeroEntree = numeroEntree;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public Date getEntreeDate() {
        return entreeDate;
    }

    public void setEntreeDate(Date entreeDate) {
        this.entreeDate = entreeDate;
    }

    public Long getMontantEntree() {
        return montantEntree;
    }

    public void setMontantEntree(Long montantEntree) {
        this.montantEntree = montantEntree;
    }

    public Long getRestant() {
        return restant;
    }

    public void setRestant(Long restant) {
        this.restant = restant;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}
