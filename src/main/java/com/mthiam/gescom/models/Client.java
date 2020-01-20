package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Client implements Serializable {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name = "codeC",nullable = false,length = 50)
    private String codeC;

    @Column(name = "nomC",nullable = false,length = 80)
    private String nomC;

    @Column(name = "adresseC",length = 100)
    private String adresseC;

    @Column(name = "telephoneC",length = 20)
    private String telephoneC;



    public Client() {
    }

    public Client(String codeC, String nomC, String adresseC, String telephoneC) {
        this.codeC = codeC;
        this.nomC = nomC;
        this.adresseC = adresseC;
        this.telephoneC = telephoneC;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getCodeC() {
        return codeC;
    }

    public void setCodeC(String codeC) {
        this.codeC = codeC;
    }

    public String getNomC() {
        return nomC;
    }

    public void setNomC(String nomC) {
        this.nomC = nomC;
    }

    public String getAdresseC() {
        return adresseC;
    }

    public void setAdresseC(String adresseC) {
        this.adresseC = adresseC;
    }

    public String getTelephoneC() {
        return telephoneC;
    }

    public void setTelephoneC(String telephoneC) {
        this.telephoneC = telephoneC;
    }
}
