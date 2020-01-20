package com.mthiam.gescom.controllers;

import com.mthiam.gescom.models.LigneVente;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

import static com.mthiam.gescom.controllers.ListeVenteController.selectedVente;
import static com.mthiam.gescom.controllers.VentesEnCoursController.selectedVenteEnCours;

public class DetailsVenteEnCoursController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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


        ObservableList<LigneVente> obLigne = FXCollections.observableArrayList(RessourcesDAO.getIVente().getLignesVenteByIdVente(selectedVenteEnCours.getId()));

        tableLigne.setItems(obLigne);
    }
}
