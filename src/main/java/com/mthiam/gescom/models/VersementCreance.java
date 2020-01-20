package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class VersementCreance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "numeroVersement",nullable = false,length = 30)
    private String numeroVersement;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date versementDate;

    @Column(name = "montant")
    private Long montant;

    @ManyToOne(fetch = FetchType.EAGER)
    private CreanceClient creanceClient;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumeroVersement() {
        return numeroVersement;
    }

    public void setNumeroVersement(String numeroVersement) {
        this.numeroVersement = numeroVersement;
    }

    public Date getVersementDate() {
        return versementDate;
    }

    public void setVersementDate(Date versementDate) {
        this.versementDate = versementDate;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public CreanceClient getCreanceClient() {
        return creanceClient;
    }

    public void setCreanceClient(CreanceClient creanceClient) {
        this.creanceClient = creanceClient;
    }
}
