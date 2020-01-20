package com.mthiam.gescom.services;

import com.mthiam.gescom.models.Role;
import com.mthiam.gescom.models.Utilisateur;

import java.util.List;


public interface IUser {

    boolean addProfiles();

    boolean addUser(Utilisateur utilisateur);

    List<Utilisateur> getAllUser();

    List<Role> getAllRole();

    boolean activeUser(Utilisateur utilisateur);

    Utilisateur getUserByUsername(String username);

    Role getRoleByName(String nomRole);

    Utilisateur login(String username, String password);

    boolean updateUser(Utilisateur utilisateur);

    boolean changePassword(String newPassword,Utilisateur utilisateur);

    boolean resetPassword(String newPassword,Utilisateur utilisateur);






}
