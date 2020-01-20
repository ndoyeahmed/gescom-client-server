package com.mthiam.gescom.services;

import com.mthiam.gescom.config.HibernateConfiguration;
import com.mthiam.gescom.models.*;
import org.hibernate.Session;

import javax.persistence.EntityTransaction;
import java.util.List;

public class EntreeImplementation implements IEntree{

    @Override
    public List<Fournisseur> getAllFournisseur() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Fournisseur> fournisseurs = null;
        try{
            fournisseurs = session.createQuery("select f from Fournisseur f",Fournisseur.class).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
            ex.printStackTrace();

        }
        return fournisseurs;
    }

    @Override
    public boolean addFournisseur(Fournisseur fournisseur) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.save(fournisseur);

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
    public Fournisseur getFournisseurByTel(String tel) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Fournisseur fournisseur = null;

        try{
            fournisseur = session.createQuery("select f from Fournisseur f where f.telephoneF=:telephone",Fournisseur.class)
                    .setParameter("telephone",tel)
                    .getSingleResult();
            session.close();

        }catch (Exception ex){
            session.close();
            ex.printStackTrace();

        }
        return fournisseur;
    }



    @Override
    public boolean addEntree(Entree entree,List<LigneEntree> ligneEntrees) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        try {

            session.save(entree);

            session.flush();
            for(LigneEntree l:ligneEntrees){
                session.saveOrUpdate(l.getStock());
                session.flush();
                l.setEntree(entree);
                session.save(l);
            }
            if(entree.getRestant()!=0){
                session.save(new DetteFournisseur(false,entree));
            }
            tx.commit();
            session.close();
            return true;
        } catch (Exception ex){
            tx.rollback();
            session.close();
            ex.printStackTrace();
            return false;
        }
    }



    @Override
    public Stock getArticleById(long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Stock a = null;

        try{
            a = session.createQuery("select s from Stock s where s.article.id=:id",Stock.class)
                    .setParameter("id",id).getSingleResult();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return a;
    }

    @Override
    public Stock getArticleByReference(String reference) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Stock a = null;

        try{
            a = session.createQuery("select s from Stock s where s.article.reference=:reference",Stock.class)
                    .setParameter("reference",reference).getSingleResult();
            session.close();

        }catch (Exception ex){
            session.close();
        }
        return a;
    }

    @Override
    public List<Entree> getAllEntrees() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Entree> entrees = null;
        try{
            entrees = session.createQuery("select e from Entree e",Entree.class).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();

        }

        return entrees;
    }

    @Override
    public List<LigneEntree> getLignesEntreeByIdEntree(long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<LigneEntree> ligneEntrees = null;
        try{
            ligneEntrees = session.createQuery("select l from LigneEntree l where l.entree.id=:id",LigneEntree.class)
                    .setParameter("id",id).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();

        }
        return ligneEntrees;
    }

    @Override
    public List<DetteFournisseur> allCreancesOk() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<DetteFournisseur> detteFournisseurs = null;
        try{
            detteFournisseurs = session.createQuery("select c from DetteFournisseur c  where c.ok=:rep", DetteFournisseur.class)
                    .setParameter("rep",true).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return detteFournisseurs;
    }

    @Override
    public List<DetteFournisseur> allCreancesNotOk() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<DetteFournisseur> detteFournisseurs = null;

        try{
            detteFournisseurs = session.createQuery("select c from DetteFournisseur c  where c.ok=:rep", DetteFournisseur.class)
                    .setParameter("rep",false).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return detteFournisseurs;
    }

    @Override
    public boolean addPaiementDette(VersementDette versementDette) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(versementDette.getCreanceFournisseur().getEntree());
            session.update(versementDette.getCreanceFournisseur());
            session.save(versementDette);
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
    public List<VersementDette> getAllVersementByIdEntree(long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<VersementDette> versementDettes = null;

        try{
            versementDettes = session.createQuery("select v from VersementDette  v where v.creanceFournisseur.entree.id=:id", VersementDette.class)
                    .setParameter("id",id).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return versementDettes;
    }

    @Override
    public boolean updateFournisseur(Fournisseur fournisseur) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(fournisseur);
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

}
