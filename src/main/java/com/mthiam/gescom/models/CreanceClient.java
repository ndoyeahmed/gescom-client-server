package com.mthiam.gescom.models;

import javax.persistence.*;

@Entity
public class CreanceClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean ok;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Vente vente;

    public CreanceClient() {
    }

    public CreanceClient(boolean ok, Vente vente) {
        this.ok = ok;
        this.vente = vente;
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

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }
}
