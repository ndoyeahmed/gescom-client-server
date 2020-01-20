package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.GenerateFacture;
import com.mthiam.gescom.models.LigneVente;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.mthiam.gescom.controllers.ListeVenteController.selectedVente;
import static com.mthiam.gescom.controllers.SignInController.loggedUser;

public class DetailsVenteController implements Initializable {
    @FXML
    private Label title_Lbl;

    @FXML
    private TableView<LigneVente> tableLigne;

    @FXML
    private TableColumn<LigneVente, String> code_column;

    @FXML
    private TableColumn<LigneVente, String> reference_column;

    @FXML
    private TableColumn<LigneVente, String> designation_column;

    @FXML
    private TableColumn<LigneVente, String> qte_column;

    @FXML
    private TableColumn<LigneVente, String> parix_achat_column;

    @FXML
    private TableColumn<LigneVente, String> montant_column;

    @FXML
    private Button printReportBtn;

    ObservableList<LigneVente> obLigne=null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!"GESTIONNAIRE DE STOCK".equals(loggedUser.getRole().getNomRole()) && selectedVente.getStatusVente()==2){
            printReportBtn.setDisable(false);
        }

        if(null != selectedVente){
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            title_Lbl.setText("Détails vente N° "+selectedVente.getNumeroVente()+" du "+dt1.format(selectedVente.getVenteDate()));
        }

        code_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneVente, String> parametre) -> {
            LigneVente l = parametre.getValue();
            return Bindings.createStringBinding(() -> l.getStock().getArticle().getCodearticle());
        });

        reference_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneVente, String> parametre) -> {
            LigneVente l = parametre.getValue();
            return Bindings.createStringBinding(() -> l.getStock().getArticle().getReference());
        });

        designation_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneVente, String> parametre) -> {
            LigneVente l = parametre.getValue();
            return Bindings.createStringBinding(() -> l.getStock().getArticle().getDesignation());
        });

        qte_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneVente, String> parametre) -> {
            LigneVente l = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(l.getQuantite()));
        });

        parix_achat_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneVente, String> parametre) -> {
            LigneVente l = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(l.getPu()));
        });

        montant_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneVente, String> parametre) -> {
            LigneVente l = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(l.getQuantite()*l.getPu()));
        });



        obLigne = FXCollections.observableArrayList(RessourcesDAO.getIVente().getLignesVenteByIdVente(selectedVente.getId()));

        tableLigne.setItems(obLigne);

    }

    @FXML
    void printReport(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("entrepriseName", AppConfiguration.getEntrepriseName());
        parameters.put("idvente", selectedVente.getId());
        parameters.put("logo", AppConfiguration.getEntrepriseLogo());
        parameters.put("adresse", AppConfiguration.getEntrepriseAddress()+" - "+
                AppConfiguration.getEntrepriseFixeCall()+" - "+AppConfiguration.getEntrepriseMobileCall());
        parameters.put("ninea","NINEA: "+AppConfiguration.getEntrepriseNinea()+" - RC: "+AppConfiguration.getEntrepriseRegistreDeCommerce());
        GenerateFacture.showOrdonnance(parameters);
    }

}
