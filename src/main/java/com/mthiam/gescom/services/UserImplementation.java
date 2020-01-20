package com.mthiam.gescom.services;

import com.mthiam.gescom.config.HibernateConfiguration;
import com.mthiam.gescom.config.PasswordUtil;
import com.mthiam.gescom.models.Role;
import com.mthiam.gescom.models.Utilisateur;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserImplementation implements IUser {



    @Override
    public boolean addProfiles() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Role> roles=new ArrayList<>();
        roles.add(new Role("ADMINISTRATEUR"));
        roles.add(new Role("CAISSIER"));
        roles.add(new Role("GESTIONNAIRE DE STOCK"));
        try {
            session.beginTransaction();
            for (Role role:roles) {
                session.save(role);
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
    public boolean addUser(Utilisateur utilisateur) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.saveOrUpdate(utilisateur);

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
    public List<Utilisateur> getAllUser() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Utilisateur> utilisateurs =null;
        try{
            utilisateurs =session.createQuery("select u from Utilisateur u",Utilisateur.class).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
            ex.printStackTrace();

        }
        return utilisateurs;
    }

    @Override
    public List<Role> getAllRole() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        List<Role> roles = null;
        try{
            roles = session.createQuery("select r from Role r",Role.class).getResultList();
            session.close();
        }catch (Exception ex){
            session.close();
        }

        return roles;
    }

    @Override
    public boolean activeUser(Utilisateur utilisateur) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        utilisateur.setStatus(true);
        try {
            session.beginTransaction();

            session.merge(utilisateur);

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
    public Utilisateur getUserByUsername(String username) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Utilisateur user = null;
        try{
            user = session.createQuery("select u from Utilisateur u where u.loginUtilisateur=:username",Utilisateur.class).setParameter("username",username).
                    getSingleResult();
            session.close();
        }catch(Exception ex){
            session.close();
        }
        return user;
    }

    @Override
    public Role getRoleByName(String nomRole) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        Role role = null;
        try{
            role = session.createQuery("select r from Role r where r.nomRole=:nomRole",Role.class).setParameter("nomRole",nomRole).
                    getSingleResult();
            session.close();
        }catch(Exception ex){
            session.close();
        }
        return role;
    }

    @Override
    public Utilisateur login(String username, String password) {

        Utilisateur user = getUserByUsername(username);

        if(user!=null){
            if(PasswordUtil.verifyUserPassword(password,user.getPasswordUtilisateur(),user.getSaltPassword())){
                return user;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public boolean updateUser(Utilisateur utilisateur) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(utilisateur);
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
    public boolean changePassword(String newPassword, Utilisateur utilisateur) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        String salt = PasswordUtil.getSalt(30);

        String crypted = PasswordUtil.generateSecurePassword(newPassword,salt);
        utilisateur.setSaltPassword(salt);
        utilisateur.setPasswordUtilisateur(crypted);
        utilisateur.setChange(true);
        try {
            session.beginTransaction();

            session.merge(utilisateur);

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
    public boolean resetPassword(String newPassword, Utilisateur utilisateur) {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        String salt = PasswordUtil.getSalt(30);

        String crypted = PasswordUtil.generateSecurePassword(newPassword,salt);
        utilisateur.setSaltPassword(salt);
        utilisateur.setPasswordUtilisateur(crypted);
        utilisateur.setChange(false);
        try {
            session.beginTransaction();

            session.merge(utilisateur);

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
