package com.mthiam.gescom.controllers;


import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.Utilisateur;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GesUsersController implements Initializable {

    @FXML
    private TableView<Utilisateur> table_users;

    @FXML
    private TableColumn<Utilisateur, String> id_column;

    @FXML
    private TableColumn<Utilisateur, String> prenom_column;

    @FXML
    private TableColumn<Utilisateur, String> nom_column;

    @FXML
    private TableColumn<Utilisateur, String> login_column;

    @FXML
    private TableColumn<Utilisateur, String> etatCompte_column;

    @FXML
    private TableColumn<Utilisateur, String> etatPassword_column;

    @FXML
    private TableColumn<Utilisateur, String> role_column;

    @FXML
    private TableColumn<Utilisateur, String> action_column;

    @FXML
    private TableColumn<Utilisateur, String> generate_column;


    public static Utilisateur selectedUser = null;



    private ObservableList<Utilisateur> obUsers;

    @FXML
    void add_new_user(ActionEvent event) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(Rooting.ADDUTILISATEURS));
        stage.setScene(new Scene(root));
        stage.setTitle("-=- GES'COM | Ajouter  un utilisateur -=-");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        stage.show();
        stage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
        stage.centerOnScreen();
        stage.setOnHidden((e) -> {
            List<Utilisateur> utilisateurs;
            try {
                utilisateurs = RessourcesDAO.getIUser().getAllUser();
                obUsers = FXCollections.observableArrayList(utilisateurs);
                table_users.getSelectionModel().clearSelection();
                table_users.setItems(obUsers);
                table_users.refresh();
            } catch (Exception ex) {

            }


        });

        stage.setOnCloseRequest((e) -> {
            List<Utilisateur> utilisateurs;
            try {
                utilisateurs = RessourcesDAO.getIUser().getAllUser();
                obUsers = FXCollections.observableArrayList(utilisateurs);
                table_users.getSelectionModel().clearSelection();
                table_users.setItems(obUsers);
                table_users.refresh();
            } catch (Exception ex) {

            }


        });


    }

    private void initTableView(){
        ///////////////////////////////////////////////////////////////////////////////////
        ///INITIALISATION DES COLONNES DU TABLEVIEW


        id_column.setCellValueFactory((TableColumn.CellDataFeatures<Utilisateur, String> parametre) -> {
            Utilisateur u = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(u.getId()) );
        });

        nom_column.setCellValueFactory((TableColumn.CellDataFeatures<Utilisateur, String> parametre) -> {
            Utilisateur u = parametre.getValue();
            return Bindings.createStringBinding(() -> u.getNomUtilisateur());
        });

        prenom_column.setCellValueFactory((TableColumn.CellDataFeatures<Utilisateur, String> parametre) -> {
            Utilisateur u = parametre.getValue();
            return Bindings.createStringBinding(() -> u.getPrenomUtilisateur());
        });

        login_column.setCellValueFactory((TableColumn.CellDataFeatures<Utilisateur, String> parametre) -> {
            Utilisateur u = parametre.getValue();
            return Bindings.createStringBinding(() -> u.getLoginUtilisateur());
        });

        etatCompte_column.setCellValueFactory((TableColumn.CellDataFeatures<Utilisateur, String> parametre) -> {
            Utilisateur u = parametre.getValue();
            if(u.isStatus()){
                return Bindings.createStringBinding(() -> "Actif");
            }else{
                return Bindings.createStringBinding(() -> "Inactif");
            }
        });

        etatPassword_column.setCellValueFactory((TableColumn.CellDataFeatures<Utilisateur, String> parametre) -> {
            Utilisateur u = parametre.getValue();
            if(u.isChange()){
                return Bindings.createStringBinding(() -> "Actif");
            }else{
                return Bindings.createStringBinding(() -> "Inactif");
            }
        });

        role_column.setCellValueFactory((TableColumn.CellDataFeatures<Utilisateur, String> parametre) -> {
            Utilisateur u = parametre.getValue();
            return Bindings.createStringBinding(() -> u.getRole().getNomRole());
        });

        Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>> editFactory
                = //
                new Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Utilisateur, String> param) {
                        final TableCell<Utilisateur, String> cell = new TableCell<Utilisateur, String>() {

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
                                    btn.setTooltip(new Tooltip("Modifier utilisateur"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedUser= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.EDITUTILISATEURS));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Modifier un utilisateur -=-");
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            stage.setResizable(false);
                                            stage.initOwner(
                                                    ((Node) event.getSource()).getScene().getWindow());
                                            stage.show();
                                            stage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
                                            stage.setOnHidden((e) -> {
                                                List<Utilisateur> utilisateurs;
                                                try {
                                                    utilisateurs = RessourcesDAO.getIUser().getAllUser();
                                                    obUsers = FXCollections.observableArrayList(utilisateurs);
                                                    table_users.getSelectionModel().clearSelection();
                                                    table_users.setItems(obUsers);
                                                    table_users.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });

                                            stage.setOnCloseRequest((e) -> {
                                                List<Utilisateur> le;
                                                try {
                                                    le = RessourcesDAO.getIUser().getAllUser();
                                                    obUsers= FXCollections.observableArrayList(le);

                                                    table_users.getSelectionModel().clearSelection();
                                                    table_users.refresh();
                                                    table_users.setItems(obUsers);
                                                    table_users.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });
                                        } catch (IOException ex) {
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

        Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>> generateFactory
                = //
                new Callback<TableColumn<Utilisateur, String>, TableCell<Utilisateur, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Utilisateur, String> param) {
                        final TableCell<Utilisateur, String> cell = new TableCell<Utilisateur, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.MINUS_CIRCLE);
                                    icon.setSize("2em");
                                    btn.setId("resetbutton");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Réinitialiser le mot de passe"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedUser= getTableView().getItems().get(getIndex());

                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Informations");
                                            alert.setHeaderText("Voulez-vous réinitialiser le mot de passe de cet utilisateur?");
                                            alert.setContentText("Vous êtes sure?");

                                            Optional<ButtonType> result = alert.showAndWait();
                                            if (result.get() == ButtonType.OK){

                                                RessourcesDAO.getIUser().resetPassword("gescom2019",selectedUser);
                                                Message.alerteWithoutHeaderText("Informations","Mot de passe réinitialisé avec succes!");
                                                obUsers = FXCollections.observableArrayList(RessourcesDAO.getIUser().getAllUser());
                                                table_users.getSelectionModel().clearSelection();
                                                table_users.setItems(obUsers);
                                            } else {
                                                // ... user chose CANCEL or closed the dialog// ... user chose CANCEL or closed the dialog
                                            }


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

        action_column.setCellFactory(editFactory);
        generate_column.setCellFactory(generateFactory);

        obUsers = FXCollections.observableArrayList(RessourcesDAO.getIUser().getAllUser());
        table_users.setItems(obUsers);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initTableView();



    }
}
