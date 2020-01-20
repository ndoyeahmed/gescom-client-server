package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.DetteFournisseur;
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


public class DettesFournisseurController implements Initializable {

    @FXML
    private TableView<DetteFournisseur> tableCreances;

    @FXML
    private TableColumn<DetteFournisseur, String> num_br_column;

    @FXML
    private TableColumn<DetteFournisseur, String> date_column;

    @FXML
    private TableColumn<DetteFournisseur, String> fournisseur_column;

    @FXML
    private TableColumn<DetteFournisseur, String> num_facture_column;

    @FXML
    private TableColumn<DetteFournisseur, String> montant_total_column;

    @FXML
    private TableColumn<DetteFournisseur, String> montant_restant_column;

    @FXML
    private TableColumn<DetteFournisseur, String> paiement_column;

    @FXML
    private TableColumn<DetteFournisseur, String> details_column;

    private ObservableList<DetteFournisseur> obCreance;

    @FXML
    private ComboBox<String> statut_Cbx;

    @FXML
    private TextField fournisseur_Tfd;

    @FXML
    private TextField facture_Tfd;

    private FilteredList<DetteFournisseur> filteredData;

    public static DetteFournisseur selectedDette=null;

    @FXML
    void filterByFacture(KeyEvent event) {
        facture_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super DetteFournisseur>) (DetteFournisseur s) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (null != s.getEntree().getNumeroFacture() && s.getEntree().getNumeroFacture().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<DetteFournisseur> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableCreances.comparatorProperty());
        tableCreances.setItems(liste);
    }

    @FXML
    void filterByFournisseur(KeyEvent event) {

        fournisseur_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super DetteFournisseur>) (DetteFournisseur s) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (null != s.getEntree().getFournisseur() && s.getEntree().getFournisseur().getNomF().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<DetteFournisseur> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableCreances.comparatorProperty());
        tableCreances.setItems(liste);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        statut_Cbx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null && newValue != oldValue) {

                if("Réglée".equals(newValue)){
                    obCreance = FXCollections.observableArrayList(RessourcesDAO.getIEntree().allCreancesOk());
                    filteredData = new FilteredList<>(obCreance, u -> true);
                    tableCreances.setItems(obCreance);
                }else{
                    obCreance = FXCollections.observableArrayList(RessourcesDAO.getIEntree().allCreancesNotOk());
                    filteredData = new FilteredList<>(obCreance, u -> true);
                    tableCreances.setItems(obCreance);
                }
            }


        });


        ObservableList<String> liste_cbx =
                FXCollections.observableArrayList (
                        "Réglée",
                        "Non réglée"
                );
        statut_Cbx.setItems(liste_cbx);

        num_br_column.setCellValueFactory((TableColumn.CellDataFeatures<DetteFournisseur, String> parametre) -> {
            DetteFournisseur c = parametre.getValue();
            return Bindings.createStringBinding(() -> c.getEntree().getNumeroEntree());
        });

        num_facture_column.setCellValueFactory((TableColumn.CellDataFeatures<DetteFournisseur, String> parametre) -> {
            DetteFournisseur c = parametre.getValue();
            if(null!=c.getEntree().getNumeroFacture()){
                return Bindings.createStringBinding(() -> c.getEntree().getNumeroFacture());
            }else{
                return Bindings.createStringBinding(() -> "---");
            }
        });

        fournisseur_column.setCellValueFactory((TableColumn.CellDataFeatures<DetteFournisseur, String> parametre) -> {
            DetteFournisseur c = parametre.getValue();
            if(c.getEntree().getFournisseur()!=null){
                return Bindings.createStringBinding(() -> c.getEntree().getFournisseur().getNomF());
            }else{
                return Bindings.createStringBinding(() -> "---");
            }
        });

        date_column.setCellValueFactory((TableColumn.CellDataFeatures<DetteFournisseur, String> parametre) -> {
            DetteFournisseur c = parametre.getValue();
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            return Bindings.createStringBinding(() -> {
                return dt1.format(c.getEntree().getEntreeDate());
            });
        });

        montant_total_column.setCellValueFactory((TableColumn.CellDataFeatures<DetteFournisseur, String> parametre) -> {
            DetteFournisseur c = parametre.getValue();
            return Bindings.createStringBinding(() -> c.getEntree().getMontantEntree().toString());
        });


        montant_restant_column.setCellValueFactory((TableColumn.CellDataFeatures<DetteFournisseur, String> parametre) -> {
            DetteFournisseur c = parametre.getValue();
            return Bindings.createStringBinding(() -> c.getEntree().getRestant().toString());
        });



        Callback<TableColumn<DetteFournisseur, String>, TableCell<DetteFournisseur, String>> addVersementFactory
                = //
                new Callback<TableColumn<DetteFournisseur, String>, TableCell<DetteFournisseur, String>>() {
                    @Override
                    public TableCell call(final TableColumn<DetteFournisseur, String> param) {
                        final TableCell<DetteFournisseur, String> cell = new TableCell<DetteFournisseur, String>() {

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
                                    selectedDette= getTableView().getItems().get(getIndex());
                                    if(0!=selectedDette.getEntree().getRestant()) {
                                        btn.setOnAction(event -> {


                                            Stage stage = new Stage();
                                            Parent root = null;
                                            try {
                                                root = FXMLLoader.load(getClass().getResource(Rooting.PAIEMENTFOURNISSEUR));
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
                                                    obCreance = FXCollections.observableArrayList(RessourcesDAO.getIEntree().allCreancesNotOk());
                                                    filteredData = new FilteredList<>(obCreance, u -> true);
                                                    tableCreances.setItems(obCreance);
                                                    tableCreances.getSelectionModel().clearSelection();
                                                    selectedDette = null;
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


        Callback<TableColumn<DetteFournisseur, String>, TableCell<DetteFournisseur, String>> viewVersementFactory
                = //
                new Callback<TableColumn<DetteFournisseur, String>, TableCell<DetteFournisseur, String>>() {
                    @Override
                    public TableCell call(final TableColumn<DetteFournisseur, String> param) {
                        final TableCell<DetteFournisseur, String> cell = new TableCell<DetteFournisseur, String>() {

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
                                        selectedDette= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                        Parent root = null;
                                        try {
                                            root = FXMLLoader.load(getClass().getResource(Rooting.DETAILSPAIEMENTDETTE));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Détails paiement dette -=-");
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
