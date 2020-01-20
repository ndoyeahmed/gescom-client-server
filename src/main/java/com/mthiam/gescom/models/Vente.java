package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Vente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "numeroVente",nullable = false,length = 30)
    private String numeroVente;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date venteDate;

    @Column(name = "montantEntree")
    private Long montantvente;


    @Column(name = "restant")
    private Long restant;

    @Column(name = "statusVente")
    private int statusVente;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Client client;

    @OneToMany(mappedBy = "vente")
    private List<LigneVente> ligneVenteList;


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

    public Long getMontantvente() {
        return montantvente;
    }

    public void setMontantvente(Long montantvente) {
        this.montantvente = montantvente;
    }

    public Long getRestant() {
        return restant;
    }

    public void setRestant(Long restant) {
        this.restant = restant;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<LigneVente> getLigneVenteList() {
        return ligneVenteList;
    }

    public void setLigneVenteList(List<LigneVente> ligneVenteList) {
        this.ligneVenteList = ligneVenteList;
    }

    public int getStatusVente() {
        return statusVente;
    }

    public void setStatusVente(int statusVente) {
        this.statusVente = statusVente;
    }
}
