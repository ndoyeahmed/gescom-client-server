package com.mthiam.gescom.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Configuration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String appname;

    private String ninea;

    private String rc;

    private String adresse;

    private String telephone_fixe;

    private String telephone_mobile;

    private String email;

    private String web;

    private String logo;

    private String directory;



    private boolean status_admin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getNinea() {
        return ninea;
    }

    public void setNinea(String ninea) {
        this.ninea = ninea;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone_fixe() {
        return telephone_fixe;
    }

    public void setTelephone_fixe(String telephone_fixe) {
        this.telephone_fixe = telephone_fixe;
    }

    public String getTelephone_mobile() {
        return telephone_mobile;
    }

    public void setTelephone_mobile(String telephone_mobile) {
        this.telephone_mobile = telephone_mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }


    public boolean getStatus_admin() {
        return status_admin;
    }

    public void setStatus_admin(boolean status_admin) {
        this.status_admin = status_admin;
    }
}
