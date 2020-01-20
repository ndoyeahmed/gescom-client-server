package com.mthiam.gescom.services;

import com.mthiam.gescom.config.HibernateConfiguration;
import com.mthiam.gescom.models.Categorie;
import com.mthiam.gescom.models.Stock;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;


public class StockImplementation implements IStock {



    @Override
    public List<Categorie> getAllCategories() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Categorie> categories = null;
        try{
            categories =  session.createQuery("select c from Categorie c",Categorie.class).getResultList();
            session.close();
            return categories;
        }catch (Exception ex){
            session.close();
            ex.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override
    public boolean addcategorie(Categorie categorie) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.save(categorie);

            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception ex){
            session.getTransaction().rollback();
            session.close();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editCategorie(Categorie categorie) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.merge(categorie);

            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception ex){

            session.getTransaction().rollback();
            session.close();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Categorie getCategorieByName(String nomCategorie) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Categorie categorie = null;
        try{
            categorie = session.createQuery("select c from Categorie c where c.nomCategorie=:nomcategorie",Categorie.class)
                    .setParameter("nomcategorie",nomCategorie).getSingleResult();
            session.close();
            return categorie;
        }catch (Exception ex){
            session.close();
            return null;
        }

    }

    @Override
    public Categorie getCategorieById(long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Categorie categorie = null;
        try{
            categorie = session.createQuery("select c from Categorie c where c.id=:id",Categorie.class)
                    .setParameter("id",id).getSingleResult();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return categorie;
    }

    @Override
    public List<Stock> getAllArticles() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Stock> stocks = null;
        try{
            stocks = session.createQuery("select s from Stock s",Stock.class).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
            ex.printStackTrace();
        }

        return stocks;

    }

    @Override
    public List<Stock> getArticleByIdCategorie(long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Stock> stocks = null;
        try{
            stocks = session.createQuery("select s from Stock s where s.article.categorie.id=:id",Stock.class)
                    .setParameter("id",id).getResultList();
            session.close();
            return stocks;
        }catch (Exception ex){
            session.close();
            return new ArrayList<>();
        }


    }

    @Override
    public boolean updateStock(Stock stock) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(stock.getArticle());
            session.update(stock);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception ex){
            session.getTransaction().rollback();
            session.close();
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Stock> getStockAlerte() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Stock> stocks =null;
        try{
            stocks =  session.createQuery("select s from Stock s where s.qteRestante<=s.qteAlerte",Stock.class)
                    .getResultList();
            session.close();
            return stocks;
        }catch (Exception ex){
           ex.printStackTrace();
            session.close();
           return new ArrayList<>();
        }

    }




}
