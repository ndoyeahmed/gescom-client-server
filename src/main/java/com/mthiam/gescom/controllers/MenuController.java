package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.*;
import com.mthiam.gescom.main.Main;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.mthiam.gescom.controllers.NouvelleEntreeController.currentEntree;
import static com.mthiam.gescom.controllers.NouvelleVenteController.currentVente;
import static com.mthiam.gescom.controllers.SignInController.loggedUser;
import static javafx.fxml.FXMLLoader.load;

public class MenuController implements Initializable {

    @FXML
    private ToolBar toolbar;

    @FXML
    private JFXButton home_Btn;

    @FXML
    private JFXButton stock_Btn;

    @FXML
    private JFXButton stockAlerte_Btn;

    @FXML
    private JFXButton inventaire_Btn;

    @FXML
    private JFXButton nouvelleEntree_Btn;

    @FXML
    private JFXButton historiqueEntrees_Btn;

    @FXML
    private JFXButton liste_fournis_Btn;

    @FXML
    private JFXButton nouvelleVente_Btn;

    @FXML
    private JFXButton historiqueVentes_Btn;

    @FXML
    private JFXButton liste_clients_Btn;

    @FXML
    private JFXButton nouvelles_factures_Btn;

    @FXML
    private JFXButton mes_factures_Btn;

    @FXML
    private JFXButton creance_Btn;

    @FXML
    private JFXButton creance_fournis_Btn;

    @FXML
    private JFXButton ma_caisse_Btn;

    @FXML
    private JFXButton gesUtilisateurs_Btn;

    @FXML
    private JFXButton gesCategorie_Btn;

    @FXML
    private JFXButton entrepriseInfo_Btn;

    @FXML
    private JFXButton deconnexion_Btn;

    @FXML
    private JFXButton monCompteBtn;

    @FXML
    private Tab caisse_rubrique;

    @FXML
    private Tab parametrage_rubrique;

    Parent contentarea = null;






    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        if("ADMINISTRATEUR".equals(loggedUser.getRole().getNomRole())){
//            nouvelleVente_Btn.setVisible(false);
//            nouvelleEntree_Btn.setVisible(false);
//
//        }else if("CAISSIER".equals(loggedUser.getRole().getNomRole())){
//            parametrage_rubrique.setDisable(true);
//            stock_Btn.setVisible(false);
//            stockAlerte_Btn.setVisible(false);
//            nouvelleEntree_Btn.setVisible(false);
//            nouvelleVente_Btn.setVisible(false);
//        }else if("GESTIONNAIRE DE STOCK".equals(loggedUser.getRole().getNomRole())){
//            entrepriseInfo_Btn.setVisible(false);
//            gesUtilisateurs_Btn.setVisible(false);
//            caisse_rubrique.setDisable(true);
//        }else{
//
//        }



        stockAlerte_Btn.setText("Stock alerte ("+ RessourcesDAO.getIStock().getStockAlerte().size()+")");


        home_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.DASHBORD));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });


        stock_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.GESSTOCK));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        stockAlerte_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.FINSTOCKARTICLE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        nouvelleEntree_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = load(getClass().getResource(Rooting.NOUVELLEENTREE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(root));
            stage.setTitle("-=- GES'COM | Ajouter nouvelle entrée-=-");
            stage.initModality(Modality.WINDOW_MODAL);

            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            stage.centerOnScreen();
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();

            stage.setOnHidden((e) -> {
                if(currentEntree!=null){
                    BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

                    try {
                        contentarea = FXMLLoader.load(getClass().getResource(Rooting.ENTREESTOCK));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    border_pane.setCenter(contentarea);
                }
            });

            stage.setOnCloseRequest((e) -> {
                if(currentEntree!=null){
                    BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

                    try {
                        contentarea = FXMLLoader.load(getClass().getResource(Rooting.ENTREESTOCK));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    border_pane.setCenter(contentarea);
                }
            });

        });


        nouvelleVente_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = load(getClass().getResource(Rooting.NOUVELLEVENTE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(root));
            stage.setTitle("-=- GES'COM | Nouvelle vente-=-");
            stage.initModality(Modality.WINDOW_MODAL);

            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            stage.centerOnScreen();
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();

            stage.setOnHidden((e) -> {
                if(currentVente!=null){
                    BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

                    try {
                        contentarea = FXMLLoader.load(getClass().getResource(Rooting.SORTIESTOCK));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    border_pane.setCenter(contentarea);
                }
            });

            stage.setOnCloseRequest((e) -> {
                if(currentVente!=null){
                    BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

                    try {
                        contentarea = FXMLLoader.load(getClass().getResource(Rooting.ENTREESTOCK));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    border_pane.setCenter(contentarea);
                }
            });

        });


        historiqueEntrees_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.LISTEENTREE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        mes_factures_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.MESFACTURES));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        gesUtilisateurs_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.GESUTILISATEURS));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });


        liste_clients_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.GESCLIENTS));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        liste_fournis_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.GESFOURNISSEURS));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        nouvelles_factures_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.VENTEENCOURS));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        creance_fournis_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.DETTEFOURNISSEUR));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });

        creance_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {


            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.CREANCECLIENT));
            } catch (IOException e) {
                e.printStackTrace();
            }
            border_pane.setCenter(contentarea);


        });


        gesCategorie_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

            Stage stage = new Stage();
            Parent root = null;
            try {
                root = load(getClass().getResource(Rooting.GESCATEGORIE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(root));
            stage.setTitle("-=- GES'COM | Gestion des catégories d'article-=-");
            stage.initModality(Modality.WINDOW_MODAL);

            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stage.centerOnScreen();
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        });



        entrepriseInfo_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.EDITPARAMETRAGEAPP));
            } catch (IOException e) {
                e.printStackTrace();
            }
            borderPane.setCenter(null);
            borderPane.setCenter(contentarea);
        });

        historiqueVentes_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            BorderPane borderPane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

            try {
                contentarea = FXMLLoader.load(getClass().getResource(Rooting.LISTEVENTE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            borderPane.setCenter(null);
            borderPane.setCenter(contentarea);
        });


        deconnexion_Btn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Informations");
            alert.setHeaderText("Vous êtes sur le point de vous déconnecter");
            alert.setContentText("Etes-vous d'accord?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){

                stage = (Stage) toolbar.getScene().getWindow();
                stage.close();
                Platform.runLater(() -> {
                    try {
                        new Main().start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });


        monCompteBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

            Stage stage = new Stage();
            Parent root = null;
            try {
                root = load(getClass().getResource(Rooting.CHANGEPASSWORD));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(root));
            stage.setTitle("-=- GES'COM | Changer mon mot de passe-=-");
            stage.initModality(Modality.WINDOW_MODAL);

            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stage.centerOnScreen();
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        });



    }
}
