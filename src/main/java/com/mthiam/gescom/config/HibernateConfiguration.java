package com.mthiam.gescom.config;


import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;

public abstract class HibernateConfiguration {

    private static SessionFactory ourSessionFactory;

    private HibernateConfiguration(){}

//    static {
//        try {
//            Configuration configuration = new Configuration();
//            configuration.configure("database/hibernate.cfg.xml");
//            ourSessionFactory = configuration.buildSessionFactory();
//
//        } catch (Throwable ex) {
//            ex.printStackTrace();
//            throw new ExceptionInInitializerError(ex);
//        }
//    }

//    public static Session getSession() throws HibernateException {
//        return ourSessionFactory.openSession();
//    }

    public static synchronized SessionFactory getSessionFactory() {

        if (ourSessionFactory == null) {
            ourSessionFactory = new Configuration().configure("database/hibernate.cfg.xml").
                    buildSessionFactory();
        }
        return ourSessionFactory;
    }









    public static void initDatabase() throws Exception {
        final Session session = getSessionFactory().openSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }
    }
}
