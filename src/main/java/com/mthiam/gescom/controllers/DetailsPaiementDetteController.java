package com.mthiam.gescom.controllers;

import com.mthiam.gescom.models.VersementDette;
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

import static com.mthiam.gescom.controllers.DettesFournisseurController.selectedDette;

public class DetailsPaiementDetteController implements Initializable {

    @FXML
    private Label title_Lbl;

    @FXML
    private TableView<VersementDette> tabledetailsPaieFournisseur;

    @FXML
    private TableColumn<VersementDette, String> numero_column;

    @FXML
    private TableColumn<VersementDette, String> date_column;

    @FXML
    private TableColumn<VersementDette, String> montant_column;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(null != selectedDette){
            title_Lbl.setText("Détails paiement entrée N° "+selectedDette.getEntree().getNumeroEntree());
        }

        numero_column.setCellValueFactory((TableColumn.CellDataFeatures<VersementDette, String> parametre) -> {
            VersementDette v = parametre.getValue();
            return Bindings.createStringBinding(() -> v.getNumeroVersement());
        });

        date_column.setCellValueFactory((TableColumn.CellDataFeatures<VersementDette, String> parametre) -> {
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            VersementDette v = parametre.getValue();
            return Bindings.createStringBinding(() -> dt1.format(v.getVersementDate()));
        });

        montant_column.setCellValueFactory((TableColumn.CellDataFeatures<VersementDette, String> parametre) -> {
            VersementDette v = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(v.getMontant()));
        });

        ObservableList<VersementDette> obVersement = FXCollections.observableArrayList(RessourcesDAO.getIEntree().getAllVersementByIdEntree(selectedDette.getEntree().getId()));

        tabledetailsPaieFournisseur.setItems(obVersement);


    }
}
