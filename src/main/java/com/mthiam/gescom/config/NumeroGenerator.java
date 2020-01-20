package com.mthiam.gescom.config;

import org.hibernate.Session;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class NumeroGenerator {

    private static Session session = HibernateConfiguration.getSessionFactory().openSession();


    public static String generateCodeArticle() {
        try {
            int a = (int) session.createQuery("SELECT MAX(a.id) FROM Article a").getSingleResult();
            NumberFormat nf = new DecimalFormat("0000");
            int b = a + 1;
            String matricule = nf.format(b);
            return "P" +maDate() + matricule;

        } catch (Exception e) {
            return "P" + maDate() + "0001";
        }
    }

    public static String generateNumBR() {
        try {
            int a = (int) session.createQuery("SELECT MAX(e.id) FROM Entree e").getSingleResult();
            NumberFormat nf = new DecimalFormat("0000");
            int b = a + 1;
            String matricule = nf.format(b);
            return "BR" +maDate() + matricule;

        } catch (Exception e) {
            return "BR" + maDate() + "0001";
        }
    }

    public static String generateNumDetteVersement() {
        try {
            int a = (int) session.createQuery("SELECT MAX(v.id) FROM VersementDette  v").getSingleResult();
            NumberFormat nf = new DecimalFormat("0000");
            int b = a + 1;
            String matricule = nf.format(b);
            return "PF" +maDate() + matricule;

        } catch (Exception e) {
            return "PF" + maDate() + "0001";
        }
    }

    public static String generateNumCreanceVersement() {
        try {
            int a = (int) session.createQuery("SELECT MAX(v.id) FROM VersementCreance  v").getSingleResult();
            NumberFormat nf = new DecimalFormat("0000");
            int b = a + 1;
            String matricule = nf.format(b);
            return "PC" +maDate() + matricule;

        } catch (Exception e) {
            return "PC" + maDate() + "0001";
        }
    }


    public static String generateNumVente() {
        try {
            int a = (int) session.createQuery("SELECT MAX(v.id) FROM Vente v").getSingleResult();
            NumberFormat nf = new DecimalFormat("0000");
            int b = a + 1;
            String matricule = nf.format(b);
            return "V" +maDate() + matricule;

        } catch (Exception e) {
            return "V" + maDate() + "0001";
        }
    }

    public static String generateCodeFournisseur() {
        try {
            int a = (int) session.createQuery("SELECT MAX(f.id) FROM Fournisseur f").getSingleResult();
            NumberFormat nf = new DecimalFormat("0000");
            int b = a + 1;
            String matricule = nf.format(b);
            return "P" +maDate() + matricule;

        } catch (Exception e) {
            return "P" + maDate() + "0001";
        }
    }


    public static String generateCodeClient() {
        try {
            int a = (int) session.createQuery("SELECT MAX(c.id) FROM Client c").getSingleResult();
            NumberFormat nf = new DecimalFormat("0000");
            int b = a + 1;
            String matricule = nf.format(b);
            return "C" +maDate() + matricule;

        } catch (Exception e) {
            return "C" + maDate() + "0001";
        }
    }


    public static String maDate() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String samaDate = df.format(date);
        String[] array1 = samaDate.split(" ");
        String[] array = array1[0].split("/");

        String y = array[2];
        String[] array2 = array1[1].split(":");
        String hh = array2[0];
        String mm = array2[1];
        String ss = array2[2];
        return y + hh + mm + ss;
    }
}
