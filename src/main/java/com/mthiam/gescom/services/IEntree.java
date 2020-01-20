package com.mthiam.gescom.services;

import com.mthiam.gescom.models.*;

import java.util.List;

public interface IEntree {

    List<Fournisseur> getAllFournisseur();

    boolean addFournisseur(Fournisseur fournisseur);

    Fournisseur getFournisseurByTel(String tel);

    boolean addEntree(Entree entree,List<LigneEntree> ligneEntrees);



    Stock getArticleById(long id);

    Stock getArticleByReference(String reference);

    List<Entree> getAllEntrees();

    List<LigneEntree> getLignesEntreeByIdEntree(long id);

    List<DetteFournisseur> allCreancesOk();

    List<DetteFournisseur> allCreancesNotOk();

    boolean addPaiementDette(VersementDette versementDette);

    List<VersementDette> getAllVersementByIdEntree(long id);

    boolean updateFournisseur(Fournisseur fournisseur);

    }
