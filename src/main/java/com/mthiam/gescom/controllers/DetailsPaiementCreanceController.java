package com.mthiam.gescom.controllers;

import com.mthiam.gescom.models.VersementCreance;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import static com.mthiam.gescom.controllers.CreanceClientController.selectedCreance;

public class DetailsPaiementCreanceController implements Initializable {

    @FXML
    private Label title_Lbl;

    @FXML
    private TableView<VersementCreance> tabledetailsPaieFournisseur;

    @FXML
    private TableColumn<VersementCreance, String> numero_column;

    @FXML
    private TableColumn<VersementCreance, String> date_column;

    @FXML
    private TableColumn<VersementCreance, String> montant_column;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(null != selectedCreance){
            title_Lbl.setText("Détails paiement entrée N° "+selectedCreance.getVente().getNumeroVente());
        }

        numero_column.setCellValueFactory((TableColumn.CellDataFeatures<VersementCreance, String> parametre) -> {
            VersementCreance v= parametre.getValue();
            return Bindings.createStringBinding(() -> v.getNumeroVersement());
        });

        date_column.setCellValueFactory((TableColumn.CellDataFeatures<VersementCreance, String> parametre) -> {
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            VersementCreance v = parametre.getValue();
            return Bindings.createStringBinding(() -> dt1.format(v.getVersementDate()));
        });

        montant_column.setCellValueFactory((TableColumn.CellDataFeatures<VersementCreance, String> parametre) -> {
            VersementCreance v = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(v.getMontant()));
        });

        ObservableList<VersementCreance> obVersement = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVersementByIdVente(selectedCreance.getVente().getId()));

        tabledetailsPaieFournisseur.setItems(obVersement);
    }
}
