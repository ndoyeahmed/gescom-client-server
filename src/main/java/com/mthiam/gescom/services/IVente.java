package com.mthiam.gescom.services;

import com.mthiam.gescom.models.*;

import java.util.List;

public interface IVente {

    List<Client> getAllClient();

    List<Vente> getAllVente();

    boolean addClient(Client client);

    Client getClientByTel(String tel);

    boolean addVente(Vente vente, List<LigneVente> ligneVentes);

    List<LigneVente> getLignesVenteByIdVente(long id);

    List<CreanceClient> allCreancesOk();

    List<CreanceClient> allCreancesNotOk();

    boolean addPaiementCreance(VersementCreance versementCreance);

    List<VersementCreance> getAllVersementByIdVente(long id);

    boolean addDefaultClient();

    Client getDefaultClient();

    boolean updateClient(Client client);

    boolean updateVente(Vente vente);

    boolean annulerVente(Vente vente);

    List<StockCategorie> getNbStockByCategorie();
}
