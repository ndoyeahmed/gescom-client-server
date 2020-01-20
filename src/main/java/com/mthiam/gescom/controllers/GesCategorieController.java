package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.Categorie;
import com.mthiam.gescom.services.RessourcesDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GesCategorieController implements Initializable {

    @FXML
    private TableView<Categorie> tableau_categorie;

    @FXML
    private TableColumn<Categorie, String> id_column;

    @FXML
    private TableColumn<Categorie, String> libelle_column;

    @FXML
    private TableColumn<Categorie, String> edit_column;

    private ObservableList<Categorie> obCategories;


    public static Categorie selectedCat = null;



    @FXML
    void add_new_category(ActionEvent event) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(Rooting.ADDCATEGORIE));
        stage.setScene(new Scene(root));
        stage.setTitle("-=- GES'COM | Ajouter  une catégorie de produit -=-");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setMaximized(false);
        stage.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        stage.show();
        stage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
        stage.centerOnScreen();
        stage.setOnHidden((e) -> {
            List<Categorie> categories;
            try {
                categories = RessourcesDAO.getIStock().getAllCategories();
                obCategories= FXCollections.observableList(categories);
                tableau_categorie.getSelectionModel().clearSelection();
                tableau_categorie.setItems(obCategories);
                tableau_categorie.refresh();
            } catch (Exception ex) {

            }


        });

        stage.setOnCloseRequest((e) -> {
            List<Categorie> categories;
            try {
                categories = RessourcesDAO.getIStock().getAllCategories();
                obCategories= FXCollections.observableList(categories);
                tableau_categorie.getSelectionModel().clearSelection();
                tableau_categorie.setItems(obCategories);
                tableau_categorie.refresh();
            } catch (Exception ex) {

            }


        });

    }

    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        ///////////////////////////////////////////////////////////////////////////////////
        ///INITIALISATION DES COLONNES DU TABLEVIEW


        id_column.setCellValueFactory((TableColumn.CellDataFeatures<Categorie, String> parametre) -> {
            Categorie c = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(c.getId()) );
        });

        libelle_column.setCellValueFactory((TableColumn.CellDataFeatures<Categorie, String> parametre) -> {
            Categorie c = parametre.getValue();
            return Bindings.createStringBinding(() -> c.getNomCategorie());
        });

        Callback<TableColumn<Categorie, String>, TableCell<Categorie, String>> editFactory
                = //
                new Callback<TableColumn<Categorie, String>, TableCell<Categorie, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Categorie, String> param) {
                        final TableCell<Categorie, String> cell = new TableCell<Categorie, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.PENCIL_SQUARE);
                                    icon.setSize("2em");
                                    btn.setId("editbutton");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Modifier catégorie"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedCat= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.EDITCATEGORIE));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Ajouter une catégorie -=-");
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            stage.setResizable(false);
                                            stage.initOwner(
                                                    ((Node) event.getSource()).getScene().getWindow());
                                            stage.show();
                                            stage.setOnHidden((e) -> {
                                                List<Categorie> categories;
                                                try {
                                                    categories = RessourcesDAO.getIStock().getAllCategories();
                                                    obCategories = FXCollections.observableList(categories);
                                                    tableau_categorie.getSelectionModel().clearSelection();
                                                    tableau_categorie.setItems(obCategories);
                                                    tableau_categorie.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });

                                            stage.setOnCloseRequest((e) -> {
                                                List<Categorie> categories;
                                                try {
                                                    categories = RessourcesDAO.getIStock().getAllCategories();
                                                    obCategories= FXCollections.observableList(categories);

                                                    tableau_categorie.getSelectionModel().clearSelection();
                                                    tableau_categorie.refresh();
                                                    tableau_categorie.setItems(obCategories);
                                                    tableau_categorie.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });
                                        } catch (Exception ex) {
                                            Logger.getLogger(GesUsersController.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        edit_column.setCellFactory(editFactory);
        obCategories = FXCollections.observableList(RessourcesDAO.getIStock().getAllCategories());
        tableau_categorie.setItems(obCategories);

    }
}
