package com.mthiam.gescom.models;

import java.util.Date;

public class VenteExport {
    private long id;
    private String numeroVente;
    private Date venteDate;
    private Long montantVente;
    private long sommeVerse;
    private Long restant;
    private String client;
    private String utilisateur;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumeroVente() {
        return numeroVente;
    }

    public void setNumeroVente(String numeroVente) {
        this.numeroVente = numeroVente;
    }

    public Date getVenteDate() {
        return venteDate;
    }

    public void setVenteDate(Date venteDate) {
        this.venteDate = venteDate;
    }

    public Long getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(Long montantVente) {
        this.montantVente = montantVente;
    }

    public long getSommeVerse() {
        return sommeVerse;
    }

    public void setSommeVerse(long sommeVerse) {
        this.sommeVerse = sommeVerse;
    }

    public Long getRestant() {
        return restant;
    }

    public void setRestant(Long restant) {
        this.restant = restant;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}
