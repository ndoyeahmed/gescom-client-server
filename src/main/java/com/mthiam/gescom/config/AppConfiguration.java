package com.mthiam.gescom.config;


import com.mthiam.gescom.services.RessourcesDAO;


public abstract class AppConfiguration {



    public static String getEntrepriseName() {
        return RessourcesDAO.getIConf().getConfiguration().getAppname();
    }

    public static String getEntrepriseAddress()  {
        return RessourcesDAO.getIConf().getConfiguration().getAdresse();
    }

    public static String getEntrepriseFixeCall(){
        return RessourcesDAO.getIConf().getConfiguration().getTelephone_fixe();
    }

    public static String getEntrepriseMobileCall()  {
        return RessourcesDAO.getIConf().getConfiguration().getTelephone_mobile();
    }

    public static String getEntrepriseEmail() {
        return RessourcesDAO.getIConf().getConfiguration().getEmail();
    }

    public static String getEntrepriseWebSite(){
        return RessourcesDAO.getIConf().getConfiguration().getWeb();
    }

    public static String getEntrepriseNinea()  {
        return RessourcesDAO.getIConf().getConfiguration().getNinea();
    }

    public static String getEntrepriseRegistreDeCommerce() {
        return RessourcesDAO.getIConf().getConfiguration().getRc();
    }

    public static String getEntrepriseLogo() {
        return RessourcesDAO.getIConf().getConfiguration().getLogo();
    }

    public static String getEntrepriseDirectory(){
        return RessourcesDAO.getIConf().getConfiguration().getDirectory();
    }



}
