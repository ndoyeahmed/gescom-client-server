package com.mthiam.gescom.models;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Utilisateur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nomUtilisateure",nullable = false,length = 80)
    private String nomUtilisateur;

    @Column(name = "prenomUtilisateur",nullable = false,length = 80)
    private String prenomUtilisateur;

    @Column(name = "loginUtilisateur",nullable = false,length = 50)
    private String loginUtilisateur;

    @Column(name = "passwordUtilisateur",columnDefinition = "text",nullable = false)
    private String passwordUtilisateur;

    @Column(name = "saltPassword",columnDefinition = "text",nullable = false)
    private String saltPassword;

    @Column(name = "status",nullable = false)
    private boolean status;

    @Column(name = "changePassword",nullable = false)
    private boolean change;


    @JoinColumn(name = "role",nullable = false,referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Role role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenomUtilisateur() {
        return prenomUtilisateur;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    public String getLoginUtilisateur() {
        return loginUtilisateur;
    }

    public void setLoginUtilisateur(String loginUtilisateur) {
        this.loginUtilisateur = loginUtilisateur;
    }

    public String getPasswordUtilisateur() {
        return passwordUtilisateur;
    }

    public void setPasswordUtilisateur(String passwordUtilisateur) {
        this.passwordUtilisateur = passwordUtilisateur;
    }

    public String getSaltPassword() {
        return saltPassword;
    }

    public void setSaltPassword(String saltPassword) {
        this.saltPassword = saltPassword;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
