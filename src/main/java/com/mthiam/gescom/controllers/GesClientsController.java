package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.Client;
import com.mthiam.gescom.models.Fournisseur;
import com.mthiam.gescom.models.Stock;
import com.mthiam.gescom.services.RessourcesDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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

public class GesClientsController implements Initializable {

    @FXML
    private TextField filter_Tfd;

    @FXML
    private TableView<Client> table_clients;

    @FXML
    private TableColumn<Client, String> id_column;

    @FXML
    private TableColumn<Client, String> code_column;

    @FXML
    private TableColumn<Client, String> nom_column;

    @FXML
    private TableColumn<Client, String> adresse_column;

    @FXML
    private TableColumn<Client, String> num_tel_column;

    @FXML
    private TableColumn<Client, String> edit_column;



    private FilteredList<Client> filteredData;

    public static Client selectedClientOnList = null;



    @FXML
    void filter(KeyEvent event) {

        filter_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Client>) (Client c) -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;

                } else if (c.getNomC().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }else if(!"".equals(c.getTelephoneC()) && c.getTelephoneC().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                return false;
            });
        });
        SortedList<Client> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(table_clients.comparatorProperty());
        table_clients.setItems(liste);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        id_column.setCellValueFactory((TableColumn.CellDataFeatures<Client, String> parametre) -> {
            Client cli = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(cli.getId()));
        });

        code_column.setCellValueFactory((TableColumn.CellDataFeatures<Client, String> parametre) -> {
            Client cli = parametre.getValue();
            return Bindings.createStringBinding(() -> cli.getCodeC());
        });

        nom_column.setCellValueFactory((TableColumn.CellDataFeatures<Client, String> parametre) -> {
            Client cli = parametre.getValue();
            return Bindings.createStringBinding(() -> cli.getNomC());
        });

        adresse_column.setCellValueFactory((TableColumn.CellDataFeatures<Client, String> parametre) -> {
            Client cli = parametre.getValue();
            return Bindings.createStringBinding(() -> cli.getAdresseC());
        });

        num_tel_column.setCellValueFactory((TableColumn.CellDataFeatures<Client, String> parametre) -> {
            Client cli = parametre.getValue();
            return Bindings.createStringBinding(() -> cli.getTelephoneC());
        });


        Callback<TableColumn<Client, String>, TableCell<Client, String>> editFactory
                = //
                new Callback<TableColumn<Client, String>, TableCell<Client, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Client, String> param) {
                        final TableCell<Client, String> cell = new TableCell<Client, String>() {

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
                                    btn.setTooltip(new Tooltip("Modifier ce client"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedClientOnList= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.EDITCLIENT));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Modifier un article -=-");
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            stage.setResizable(false);
                                            stage.initOwner(
                                                    ((Node) event.getSource()).getScene().getWindow());
                                            stage.show();

                                            stage.setOnHidden((e) -> {
                                                List<Client> clients;
                                                try {
                                                    clients = RessourcesDAO.getIVente().getAllClient()
                                                            .stream().filter(c->!c.getNomC().equals("AU COMPTOIR")).collect(Collectors.toList());
                                                    ObservableList<Client> obClients = FXCollections.observableArrayList(clients);
                                                    table_clients.getSelectionModel().clearSelection();
                                                    table_clients.setItems(obClients);
                                                    table_clients.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });

                                            stage.setOnCloseRequest((e) -> {
                                                List<Client> clients;
                                                try {
                                                    clients = RessourcesDAO.getIVente().getAllClient()
                                                            .stream().filter(c->!c.getNomC().equals("AU COMPTOIR")).collect(Collectors.toList());
                                                    ObservableList<Client> obClients = FXCollections.observableArrayList(clients);
                                                    table_clients.getSelectionModel().clearSelection();
                                                    table_clients.setItems(obClients);
                                                    table_clients.refresh();
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

        ObservableList<Client> obClient = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllClient()
        .stream().filter(c->!c.getNomC().equals("AU COMPTOIR")).collect(Collectors.toList()));
        filteredData = new FilteredList<>(obClient, u -> true);
        table_clients.setItems(obClient);



    }
}
