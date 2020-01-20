package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Fournisseur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name = "codeF",nullable = false,length = 50)
    private String codeF;

    @Column(name = "nomFF",nullable = false,length = 80)
    private String nomF;

    @Column(name = "adresseF",length = 100)
    private String adresseF;

    @Column(name = "telephoneF",length = 20)
    private String telephoneF;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getCodeF() {
        return codeF;
    }

    public void setCodeF(String codeF) {
        this.codeF = codeF;
    }

    public String getAdresseF() {
        return adresseF;
    }

    public void setAdresseF(String adresseF) {
        this.adresseF = adresseF;
    }

    public String getTelephoneF() {
        return telephoneF;
    }

    public void setTelephoneF(String telephoneF) {
        this.telephoneF = telephoneF;
    }

    public String getNomF() {
        return nomF;
    }

    public void setNomF(String nomF) {
        this.nomF = nomF;
    }
}
