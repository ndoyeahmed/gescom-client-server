package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LigneEntree implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pu",nullable = false)
    private long pu;

    @Column(name = "quantite",nullable = false)
    private int quantite;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    private Entree entree;

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

    public Entree getEntree() {
        return entree;
    }

    public void setEntree(Entree entree) {
        this.entree = entree;
    }

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
}
