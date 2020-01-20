package com.mthiam.gescom.services;

public abstract class RessourcesDAO {

    private static IUser iuser;

    private static IStock istock;

    private static IEntree ientree;

    private static IVente ivente;

    private static IConfiguration iconf;

    public static IUser getIUser(){
        
        return iuser;
    }

    public static IConfiguration getIConf(){

        return iconf;
    }

    public static IStock getIStock(){
        
        return istock ;
    }
    public static IVente getIVente(){

        return ivente ;
    }

    public static IEntree getIEntree(){
        return  ientree;
    }
    
    public static void initInterfaces(){
        istock = new StockImplementation();
        iuser = new UserImplementation();
        ientree = new EntreeImplementation();
        ivente = new VenteImplementation();
        iconf = new ConfigurationImplementation();
    }
}
