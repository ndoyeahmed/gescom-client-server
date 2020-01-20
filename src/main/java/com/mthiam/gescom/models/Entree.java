package com.mthiam.gescom.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Entree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "numeroEntree",nullable = false,length = 30)
    private String numeroEntree;

    @Column(name = "numeroFacture",length = 30)
    private String numeroFacture;



    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date entreeDate;

    @Column(name = "montantEntree")
    private Long montantEntree;

    @Column(name = "restant")
    private Long restant;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "entree")
    private List<LigneEntree> ligneEntreeList;



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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public Long getRestant() {
        return restant;
    }

    public void setRestant(Long restant) {
        this.restant = restant;
    }

    public List<LigneEntree> getLigneEntreeList() {
        return ligneEntreeList;
    }

    public void setLigneEntreeList(List<LigneEntree> ligneEntreeList) {
        this.ligneEntreeList = ligneEntreeList;
    }
}
