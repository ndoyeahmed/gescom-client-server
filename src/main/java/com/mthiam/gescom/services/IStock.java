package com.mthiam.gescom.services;

import com.mthiam.gescom.models.Article;
import com.mthiam.gescom.models.Categorie;
import com.mthiam.gescom.models.Stock;


import java.util.List;

public interface IStock {

    List<Categorie> getAllCategories();

    boolean addcategorie(Categorie categorie);

    boolean editCategorie(Categorie categorie);

    Categorie getCategorieByName(String nomCategorie);

    Categorie getCategorieById(long id);

    List<Stock> getAllArticles();

    List<Stock> getArticleByIdCategorie(long id);

    boolean updateStock(Stock stock);

    List<Stock> getStockAlerte();







}
