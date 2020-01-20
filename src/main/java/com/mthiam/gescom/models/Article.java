package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "codearticle",length = 50,nullable = false)
    private String codearticle;

    @Column(name = "designation",length = 50,nullable = false)
    private String designation;

    @Column(name = "reference",length = 50)
    private String reference;

    @Column(name = "prixBase")
    private long prixBase;

    @ManyToOne
    private Categorie categorie;





    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodearticle() {
        return codearticle;
    }

    public void setCodearticle(String codearticle) {
        this.codearticle = codearticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(long prixBase) {
        this.prixBase = prixBase;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }


}
