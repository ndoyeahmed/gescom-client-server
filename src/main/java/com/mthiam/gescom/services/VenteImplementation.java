package com.mthiam.gescom.services;

import com.mthiam.gescom.config.HibernateConfiguration;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.models.*;
import org.hibernate.Session;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class VenteImplementation implements IVente {

    @Override
    public List<Client> getAllClient() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Client> clients = null;
        try{
            clients = session.createQuery("select c from Client c",Client.class).getResultList();
            session.close();

        }catch (Exception ex){
            session.close();
            ex.printStackTrace();

        }
        return clients;
    }

    @Override
    public List<Vente> getAllVente() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Vente> ventes = null;
        try{
            ventes = session.createQuery("select v from Vente v",Vente.class).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
            ex.printStackTrace();

        }
        return ventes;
    }

    @Override
    public boolean addClient(Client client) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.save(client);

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
    public Client getClientByTel(String tel) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Client client = null;

        try{
            client = session.createQuery("select c from Client c where c.telephoneC=:telephone",Client.class)
                    .setParameter("telephone",tel)
                    .getSingleResult();
            session.close();
        }catch (NoResultException ex){
            session.close();

        }
        return client;
    }

    @Override
    public boolean addVente(Vente vente, List<LigneVente> ligneVentes){
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        try {

            session.save(vente);

            session.flush();
            for(LigneVente l:ligneVentes){
                session.update(l.getStock());
                l.setVente(vente);
                session.save(l);
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
    public List<LigneVente> getLignesVenteByIdVente(long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<LigneVente> ligneVentes = null;
        try{
            ligneVentes = session.createQuery("select l from LigneVente l where l.vente.id=:id",LigneVente.class)
                    .setParameter("id",id).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();

        }
        return ligneVentes;
    }

    @Override
    public List<CreanceClient> allCreancesOk() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<CreanceClient> creanceClients = null;
        try{
            creanceClients = session.createQuery("select c from CreanceClient c  where c.ok=:rep", CreanceClient.class)
                    .setParameter("rep",true).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return creanceClients;
    }

    @Override
    public List<CreanceClient> allCreancesNotOk() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<CreanceClient> creanceClients = null;
        try{
            creanceClients = session.createQuery("select c from CreanceClient c  where c.ok=:rep", CreanceClient.class)
                    .setParameter("rep",false).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return creanceClients;
    }

    @Override
    public boolean addPaiementCreance(VersementCreance versementCreance) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(versementCreance.getCreanceClient().getVente());
            session.update(versementCreance.getCreanceClient());
            session.save(versementCreance);
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
    public List<VersementCreance> getAllVersementByIdVente(long id) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<VersementCreance> versementCreances= null;

        try{
            versementCreances = session.createQuery("select v from VersementCreance v where v.creanceClient.vente.id=:id", VersementCreance.class)
                    .setParameter("id",id).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return versementCreances;
    }

    @Override
    public boolean addDefaultClient() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(new Client(NumeroGenerator.generateCodeClient(),"AU COMPTOIR","",""));
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
    public Client getDefaultClient() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Client client = null;
        try{
            client = session.createQuery("select c from Client c where c.nomC=:client", Client.class)
                    .setParameter("client","AU COMPTOIR").getSingleResult();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return client;
    }

    @Override
    public boolean updateClient(Client client) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(client);
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
    public boolean updateVente(Vente vente) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.update(vente);
            if(vente.getRestant()!=0){
                session.save(new CreanceClient(false,vente));
            }
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
    public boolean annulerVente(Vente vente) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            List<LigneVente> lignes = getLignesVenteByIdVente(vente.getId());
            session.beginTransaction();
            for(LigneVente l:lignes){
                int qr = l.getStock().getQteRestante();
                l.getStock().setQteRestante(qr + l.getQuantite());
                session.update(l.getStock());
            }

            vente.setStatusVente(3);
            session.update(vente);
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
    public List<StockCategorie> getNbStockByCategorie() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<StockCategorie> liste= null;
        try{
           Query query = session.createNativeQuery("select categorie.nomCategorie, SUM(stock.qteRestante) from article, Categorie,stock where article.categorie_id=categorie.id AND article.id=stock.article_id GROUP BY categorie.nomCategorie");
            List<Object[]> results =query.getResultList();
            List<StockCategorie> lj = new ArrayList<>();
            for (Object[] a : results) {
                lj.add(new StockCategorie(a[0].toString(),Integer.parseInt(a[1].toString())));
            }
            liste=lj;

            session.close();
        }catch (Exception ex){
            session.close();

        }
        return liste;

    }
}
