package com.mthiam.gescom.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Categorie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nomCategorie",length = 50,nullable = false)
    private String nomCategorie;

    @OneToMany(mappedBy = "categorie",fetch = FetchType.EAGER)
    List<Article> articleList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public String toString() {
        return nomCategorie;
    }
}
