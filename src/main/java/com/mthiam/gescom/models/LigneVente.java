package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LigneVente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pu",nullable = false)
    private long pu;

    @Column(name = "quantite",nullable = false)
    private int quantite;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    private Vente vente;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    private Stock stock;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPu() {
        return pu;
    }

    public void setPu(long pu) {
        this.pu = pu;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
