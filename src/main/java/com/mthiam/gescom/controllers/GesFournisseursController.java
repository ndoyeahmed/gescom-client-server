package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.Client;
import com.mthiam.gescom.models.Fournisseur;
import com.mthiam.gescom.services.RessourcesDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GesFournisseursController implements Initializable {

    @FXML
    private TextField filter_Tfd;

    @FXML
    private TableView<Fournisseur> table_fournisseurs;

    @FXML
    private TableColumn<Fournisseur, String> id_column;

    @FXML
    private TableColumn<Fournisseur, String> code_column;

    @FXML
    private TableColumn<Fournisseur, String> nom_column;

    @FXML
    private TableColumn<Fournisseur, String> adresse_column;

    @FXML
    private TableColumn<Fournisseur, String> num_tel_column;

    @FXML
    private TableColumn<Fournisseur, String> edit_column;

    public static Fournisseur selectedFournisseurOnList= null;

    private FilteredList<Fournisseur> filteredData;

    @FXML
    void filter(KeyEvent event) {

        filter_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Fournisseur>) (Fournisseur f) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (f.getNomF().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }else if(!"".equals(f.getTelephoneF()) && f.getTelephoneF().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                return false;
            });
        });
        SortedList<Fournisseur> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(table_fournisseurs.comparatorProperty());
        table_fournisseurs.setItems(liste);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        id_column.setCellValueFactory((TableColumn.CellDataFeatures<Fournisseur, String> parametre) -> {
            Fournisseur f = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(f.getId()));
        });

        code_column.setCellValueFactory((TableColumn.CellDataFeatures<Fournisseur, String> parametre) -> {
            Fournisseur f = parametre.getValue();
            return Bindings.createStringBinding(() -> f.getCodeF());
        });

        nom_column.setCellValueFactory((TableColumn.CellDataFeatures<Fournisseur, String> parametre) -> {
            Fournisseur f = parametre.getValue();
            return Bindings.createStringBinding(() -> f.getNomF());
        });

        num_tel_column.setCellValueFactory((TableColumn.CellDataFeatures<Fournisseur, String> parametre) -> {
            Fournisseur f = parametre.getValue();
            return Bindings.createStringBinding(() -> f.getTelephoneF());
        });

        adresse_column.setCellValueFactory((TableColumn.CellDataFeatures<Fournisseur, String> parametre) -> {
            Fournisseur f = parametre.getValue();
            return Bindings.createStringBinding(() -> f.getAdresseF());
        });


        Callback<TableColumn<Fournisseur, String>, TableCell<Fournisseur, String>> editFactory
                = //
                new Callback<TableColumn<Fournisseur, String>, TableCell<Fournisseur, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Fournisseur, String> param) {
                        final TableCell<Fournisseur, String> cell = new TableCell<Fournisseur, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
//
                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.PENCIL_SQUARE);
                                    icon.setSize("2em");

                                    btn.setId("editbutton");
                                    //btn.setText("modifier");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Modifier ce fournisseur"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedFournisseurOnList= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.EDITFOURNISSEUR));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Modifier un article -=-");
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            stage.setResizable(false);
                                            stage.initOwner(
                                                    ((Node) event.getSource()).getScene().getWindow());
                                            stage.show();

                                            stage.setOnHidden((e) -> {
                                                List<Fournisseur> fournisseurs;
                                                try {
                                                    fournisseurs = RessourcesDAO.getIEntree().getAllFournisseur();

                                                    ObservableList<Fournisseur> obFournisseurs = FXCollections.observableArrayList(fournisseurs);
                                                    table_fournisseurs.getSelectionModel().clearSelection();
                                                    selectedFournisseurOnList=null;
                                                    table_fournisseurs.setItems(obFournisseurs);
                                                    table_fournisseurs.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });

                                            stage.setOnCloseRequest((e) -> {
                                                List<Fournisseur> fournisseurs;
                                                try {
                                                    fournisseurs = RessourcesDAO.getIEntree().getAllFournisseur();

                                                    ObservableList<Fournisseur> obFournisseurs = FXCollections.observableArrayList(fournisseurs);
                                                    table_fournisseurs.getSelectionModel().clearSelection();
                                                    selectedFournisseurOnList=null;
                                                    table_fournisseurs.setItems(obFournisseurs);
                                                    table_fournisseurs.refresh();
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


        ObservableList<Fournisseur> obFournisseur = FXCollections.observableArrayList(RessourcesDAO.getIEntree().getAllFournisseur());
        filteredData = new FilteredList<>(obFournisseur, u -> true);
        table_fournisseurs.setItems(obFournisseur);

    }
}
