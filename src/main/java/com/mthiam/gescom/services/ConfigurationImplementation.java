package com.mthiam.gescom.services;

import com.mthiam.gescom.config.HibernateConfiguration;
import com.mthiam.gescom.models.Configuration;
import org.hibernate.Session;

public class ConfigurationImplementation implements IConfiguration {


    @Override
    public boolean isNotConfigured() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        boolean rep = false;
        try {
            rep = session.createQuery("select c from Configuration c", Configuration.class).getResultList().isEmpty();
            session.close();
        }catch (Exception ex){
            session.close();
        }
        return rep;
    }



    @Override
    public boolean saveConfiguration(Configuration configuration) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.saveOrUpdate(configuration);

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
    public boolean notHaveAdmin() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        boolean rep = false;
        try{
            rep = session.createQuery("select c from Configuration c where c.status_admin=:status",Configuration.class)
                    .setParameter("status",false).getResultList().isEmpty();
            session.close();
        }catch (Exception ex){
            session.close();

        }

        return rep;
    }

    @Override
    public Configuration getConfiguration() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Configuration configuration = null;
        try{
            configuration = session.createQuery("select c from Configuration c",Configuration.class).getResultList().get(0);
            session.close();
        }catch (Exception ex){
            session.close();

        }
        return configuration;
    }
}
