package com.mthiam.gescom.controllers;

import com.mthiam.gescom.models.LigneEntree;
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

import static com.mthiam.gescom.controllers.ListeEntreesController.selectedEntree;

public class DetailsEntreeController implements Initializable {
    @FXML
    private Label title_Lbl;

    @FXML
    private TableView<LigneEntree> tableLigne;

    @FXML
    private TableColumn<LigneEntree, String> code_column;

    @FXML
    private TableColumn<LigneEntree, String> reference_column;

    @FXML
    private TableColumn<LigneEntree, String> designation_column;

    @FXML
    private TableColumn<LigneEntree, String> qte_column;

    @FXML
    private TableColumn<LigneEntree, String> parix_achat_column;

    @FXML
    private TableColumn<LigneEntree, String> montant_column;

    ObservableList<LigneEntree> obLigne=null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(null != selectedEntree){
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            title_Lbl.setText("Détails entrée N° "+selectedEntree.getNumeroEntree()+" du "+dt1.format(selectedEntree.getEntreeDate()));
        }

        code_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneEntree, String> parametre) -> {
            LigneEntree l = parametre.getValue();
            return Bindings.createStringBinding(() -> l.getStock().getArticle().getCodearticle());
        });

        reference_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneEntree, String> parametre) -> {
            LigneEntree l = parametre.getValue();
            return Bindings.createStringBinding(() -> l.getStock().getArticle().getReference());
        });

        designation_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneEntree, String> parametre) -> {
            LigneEntree l = parametre.getValue();
            return Bindings.createStringBinding(() -> l.getStock().getArticle().getDesignation());
        });

        qte_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneEntree, String> parametre) -> {
            LigneEntree l = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(l.getQuantite()));
        });

        parix_achat_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneEntree, String> parametre) -> {
            LigneEntree l = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(l.getPu()));
        });

        montant_column.setCellValueFactory((TableColumn.CellDataFeatures<LigneEntree, String> parametre) -> {
            LigneEntree l = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(l.getQuantite()*l.getPu()));
        });

        obLigne = FXCollections.observableArrayList(RessourcesDAO.getIEntree().getLignesEntreeByIdEntree(selectedEntree.getId()));

        tableLigne.setItems(obLigne);

    }
}
