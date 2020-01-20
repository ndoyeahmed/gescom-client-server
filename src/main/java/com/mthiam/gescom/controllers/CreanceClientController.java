package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.CreanceClient;
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

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static com.mthiam.gescom.controllers.SignInController.loggedUser;

public class CreanceClientController implements Initializable {
    @FXML
    private TableView<CreanceClient> tableCreances;

    @FXML
    private TableColumn<CreanceClient, String> num_facture_column;

    @FXML
    private TableColumn<CreanceClient, String> date_column;

    @FXML
    private TableColumn<CreanceClient, String> client_column;

    @FXML
    private TableColumn<CreanceClient, String> num_telephone_column;

    @FXML
    private TableColumn<CreanceClient, String> montant_total_column;

    @FXML
    private TableColumn<CreanceClient, String> montant_restant_column;

    @FXML
    private TableColumn<CreanceClient, String> paiement_column;

    @FXML
    private TableColumn<CreanceClient, String> details_column;

    @FXML
    private ComboBox<String> statut_Cbx;

    @FXML
    private TextField client_Tfd;

    @FXML
    private TextField facture_Tfd;

    private FilteredList<CreanceClient> filteredData;

    public static CreanceClient selectedCreance=null;

    @FXML
    void filterByFacture(KeyEvent event) {

        facture_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super CreanceClient>) (CreanceClient c) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (null != c.getVente().getNumeroVente() && c.getVente().getNumeroVente().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<CreanceClient> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableCreances.comparatorProperty());
        tableCreances.setItems(liste);

    }

    @FXML
    void filterByClient(KeyEvent event) {
        client_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super CreanceClient>) (CreanceClient c) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (null != c.getVente().getClient() && c.getVente().getClient().getNomC().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<CreanceClient> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableCreances.comparatorProperty());
        tableCreances.setItems(liste);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> liste_cbx =
                FXCollections.observableArrayList (
                        "Réglée",
                        "Non réglée"
                );
        statut_Cbx.setItems(liste_cbx);
        
        statut_Cbx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null && newValue != oldValue) {

                ObservableList<CreanceClient> obCreance;
                if("Réglée".equals(newValue)){
                    obCreance = FXCollections.observableArrayList(RessourcesDAO.getIVente().allCreancesOk());
                    filteredData = new FilteredList<>(obCreance, u -> true);
                    tableCreances.setItems(obCreance);
                }else{
                    obCreance = FXCollections.observableArrayList(RessourcesDAO.getIVente().allCreancesNotOk());
                    filteredData = new FilteredList<>(obCreance, u -> true);
                    tableCreances.setItems(obCreance);
                }
            }


        });




        num_facture_column.setCellValueFactory((TableColumn.CellDataFeatures<CreanceClient, String> parametre) -> {
            CreanceClient c = parametre.getValue();
            if(null!=c.getVente().getNumeroVente()){
                return Bindings.createStringBinding(() -> c.getVente().getNumeroVente());
            }else{
                return Bindings.createStringBinding(() -> "---");
            }
        });

        client_column.setCellValueFactory((TableColumn.CellDataFeatures<CreanceClient, String> parametre) -> {
            CreanceClient c = parametre.getValue();
            if(c.getVente().getClient()!=null){
                return Bindings.createStringBinding(() -> c.getVente().getClient().getNomC());
            }else{
                return Bindings.createStringBinding(() -> "---");
            }
        });

        num_telephone_column.setCellValueFactory((TableColumn.CellDataFeatures<CreanceClient, String> parametre) -> {
            CreanceClient c = parametre.getValue();
            if(c.getVente().getClient()!=null){
                return Bindings.createStringBinding(() -> c.getVente().getClient().getTelephoneC());
            }else{
                return Bindings.createStringBinding(() -> "---");
            }
        });

        date_column.setCellValueFactory((TableColumn.CellDataFeatures<CreanceClient, String> parametre) -> {
            CreanceClient c = parametre.getValue();
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            return Bindings.createStringBinding(() -> {
                return dt1.format(c.getVente().getVenteDate());
            });
        });

        montant_total_column.setCellValueFactory((TableColumn.CellDataFeatures<CreanceClient, String> parametre) -> {
            CreanceClient c = parametre.getValue();
            return Bindings.createStringBinding(() -> c.getVente().getMontantvente().toString());
        });


        montant_restant_column.setCellValueFactory((TableColumn.CellDataFeatures<CreanceClient, String> parametre) -> {
            CreanceClient c = parametre.getValue();
            return Bindings.createStringBinding(() -> c.getVente().getRestant().toString());
        });

        Callback<TableColumn<CreanceClient, String>, TableCell<CreanceClient, String>> addVersementFactory
                = //
                new Callback<TableColumn<CreanceClient, String>, TableCell<CreanceClient, String>>() {
                    @Override
                    public TableCell call(final TableColumn<CreanceClient, String> param) {
                        final TableCell<CreanceClient, String> cell = new TableCell<CreanceClient, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.MONEY);
                                    icon.setSize("2em");

                                    btn.setId("addVersementbutton");
                                    //btn.setText("modifier");
                                    btn.setGraphic(icon);
                                    if(!"CAISSIER".equals(loggedUser.getRole().getNomRole())){
                                        btn.setVisible(false);
                                    }
                                    btn.setTooltip(new Tooltip("Ajouter paiement"));
                                    selectedCreance= getTableView().getItems().get(getIndex());
                                    if(0!=selectedCreance.getVente().getRestant()) {
                                        btn.setOnAction(event -> {


                                            Stage stage = new Stage();
                                            Parent root = null;
                                            try {
                                                root = FXMLLoader.load(getClass().getResource(Rooting.PAIEMENTCREANCECLIENT));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Paiement dette fournisseur -=-");
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.getIcons().add(new Image("file:" + AppConfiguration.getEntrepriseLogo()));
                                            stage.setResizable(false);
                                            stage.initOwner(
                                                    ((Node) event.getSource()).getScene().getWindow());
                                            stage.show();

                                            stage.setOnHidden((e) -> {

                                                try {
                                                    ObservableList<CreanceClient> obCreance = FXCollections.observableArrayList(RessourcesDAO.getIVente().allCreancesNotOk());
                                                    filteredData = new FilteredList<>(obCreance, u -> true);
                                                    tableCreances.setItems(obCreance);
                                                    tableCreances.getSelectionModel().clearSelection();
                                                    selectedCreance = null;
                                                } catch (Exception ex) {

                                                }


                                            });


                                        });
                                    }else{
                                        btn.setDisable(true);
                                    }
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };


        Callback<TableColumn<CreanceClient, String>, TableCell<CreanceClient, String>> viewVersementFactory
                = //
                new Callback<TableColumn<CreanceClient, String>, TableCell<CreanceClient, String>>() {
                    @Override
                    public TableCell call(final TableColumn<CreanceClient, String> param) {
                        final TableCell<CreanceClient, String> cell = new TableCell<CreanceClient, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.EYE);
                                    icon.setSize("2em");

                                    btn.setId("viewVersementbutton");
                                    //btn.setText("modifier");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Voir les versements"));
                                    btn.setOnAction(event -> {
                                        selectedCreance= getTableView().getItems().get(getIndex());

                                        Stage stage = new Stage();
                                        Parent root = null;
                                        try {
                                            root = FXMLLoader.load(getClass().getResource(Rooting.DETAILSPAIEMENTCREANCE));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        stage.setScene(new Scene(root));
                                        stage.setTitle("-=- GES'COM | Détails paiement créance -=-");
                                        stage.initModality(Modality.WINDOW_MODAL);
                                        stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                        stage.setResizable(false);
                                        stage.initOwner(
                                                ((Node) event.getSource()).getScene().getWindow());
                                        stage.show();


                                        stage.setOnHidden((e) -> {

                                            tableCreances.getSelectionModel().clearSelection();



                                        });


                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };


        paiement_column.setCellFactory(addVersementFactory);
        details_column.setCellFactory(viewVersementFactory);




    }
}
