package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class DetteFournisseur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean ok;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Entree entree;

    public DetteFournisseur() {
    }

    public DetteFournisseur(boolean ok, Entree entree) {
        this.ok = ok;
        this.entree = entree;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Entree getEntree() {
        return entree;
    }

    public void setEntree(Entree entree) {
        this.entree = entree;
    }
}
