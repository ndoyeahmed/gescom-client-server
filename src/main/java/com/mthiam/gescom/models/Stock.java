package com.mthiam.gescom.models;

import javax.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "qteInitiale")
    private int qteInitiale;

    @Column(name = "qteRestante",nullable = false)
    private int qteRestante;

    @Column(name = "qteAlerte")
    private int qteAlerte;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    private Article article;

    public Stock() {
        article = new Article();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQteInitiale() {
        return qteInitiale;
    }

    public void setQteInitiale(int qteInitiale) {
        this.qteInitiale = qteInitiale;
    }

    public int getQteRestante() {
        return qteRestante;
    }

    public void setQteRestante(int qteRestante) {
        this.qteRestante = qteRestante;
    }

    public int getQteAlerte() {
        return qteAlerte;
    }

    public void setQteAlerte(int qteAlerte) {
        this.qteAlerte = qteAlerte;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
