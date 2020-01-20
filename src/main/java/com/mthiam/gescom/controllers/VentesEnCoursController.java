package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.GenerateFacture;
import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.Vente;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.mthiam.gescom.controllers.SignInController.loggedUser;

public class VentesEnCoursController implements Initializable {

    @FXML
    private TableView<Vente> tableVentes;

    @FXML
    private TableColumn<Vente, String> num_bs_column;

    @FXML
    private TableColumn<Vente, String> date_column;

    @FXML
    private TableColumn<Vente, String> client_column;

    @FXML
    private TableColumn<Vente, String> montant_total_column;

    @FXML
    private TableColumn<Vente, String> details_column;

    @FXML
    private TableColumn<Vente, String> annuler_column;

    @FXML
    private TextField montant_total_Tfd;

    @FXML
    private TextField montant_encaisse_Tfd;

    @FXML
    private TextField montant_restant_Tfd;

    @FXML
    private RadioButton regler_Rbt;

    @FXML
    private ToggleGroup estReglee;

    @FXML
    private RadioButton nonRegler_Rbt;

    @FXML
    private TextField fournisseur_Tfd;

    @FXML
    private HBox radioBtnHbox;

    @FXML
    private JFXButton validFactureBtn;

    @FXML
    private JFXButton printFactureBtn;


    public static Vente selectedVenteEnCours = null;

    private FilteredList<Vente> filteredData;

    @FXML
    void filterByFournisseur(KeyEvent event) {

        fournisseur_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Vente>) (Vente v) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (null != v.getClient() && v.getClient().getNomC().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Vente> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableVentes.comparatorProperty());
        tableVentes.setItems(liste);

    }

    @FXML
    void nonRegler(ActionEvent event) {
        montant_encaisse_Tfd.setDisable(false);
    }

    @FXML
    void printFacture(ActionEvent event) {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("entrepriseName", AppConfiguration.getEntrepriseName());
        parameters.put("idvente", selectedVenteEnCours.getId());
        parameters.put("logo", AppConfiguration.getEntrepriseLogo());
        parameters.put("adresse", AppConfiguration.getEntrepriseAddress()+" - "+
                AppConfiguration.getEntrepriseFixeCall()+" - "+AppConfiguration.getEntrepriseMobileCall()
        +" - "+AppConfiguration.getEntrepriseEmail()+" - "+AppConfiguration.getEntrepriseWebSite());
        parameters.put("ninea","NINEA: "+AppConfiguration.getEntrepriseNinea()+" - RC: "+AppConfiguration.getEntrepriseRegistreDeCommerce());
        GenerateFacture.printReport(parameters);

    }

    @FXML
    void regler(ActionEvent event) {
        montant_encaisse_Tfd.setText("");
        montant_encaisse_Tfd.setDisable(true);
    }

    @FXML
    void actualiserVentesEnCours(ActionEvent event) {
        ObservableList<Vente> obVente = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVente()
                .stream().filter(v->v.getStatusVente()==1).collect(Collectors.toList()));

        tableVentes.getSelectionModel().clearSelection();
        filteredData = new FilteredList<>(obVente, u -> true);
        tableVentes.setItems(obVente);
        validFactureBtn.setDisable(true);
        printFactureBtn.setDisable(true);
        radioBtnHbox.setDisable(true);
        montant_encaisse_Tfd.setText("");
        montant_restant_Tfd.setText("0");
        montant_total_Tfd.setText("");
        montant_encaisse_Tfd.setDisable(true);
    }

    @FXML
    void validerFacture(ActionEvent event) {

        if(nonRegler_Rbt.isSelected()){
            if("".equals(montant_encaisse_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Veuillez renseigner le montant versé pqr le client!");
                return;
            }else{
                if(Long.parseLong(montant_encaisse_Tfd.getText()) > Long.parseLong(montant_total_Tfd.getText())){
                    Message.alerteWithoutHeaderText("Informations","Le montant versé ne peut pas être supérieur au \n" +
                            "au montant total des articles!");
                }else{
                    selectedVenteEnCours.setRestant(Long.parseLong(montant_total_Tfd.getText())-Long.parseLong(montant_encaisse_Tfd.getText()));
                }
            }
        }else{
            selectedVenteEnCours.setRestant((long) 0);


        }
        selectedVenteEnCours.setStatusVente(2);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Vous êtes sur le point de valider cette facture.");
        alert.setContentText("Cette action est irréversible.\nEtes-vous d'accord?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            boolean rep = false;
            try {
                rep = RessourcesDAO.getIVente().updateVente(selectedVenteEnCours);
                Message.alerteWithoutHeaderText("Informations","Vente validée avec succés!");
                ObservableList<Vente> obVente = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVente()
                        .stream().filter(v->v.getStatusVente()==1).collect(Collectors.toList()));

                tableVentes.getSelectionModel().clearSelection();
                filteredData = new FilteredList<>(obVente, u -> true);
                tableVentes.setItems(obVente);
                validFactureBtn.setDisable(true);
                printFactureBtn.setDisable(false);
                radioBtnHbox.setDisable(true);
                montant_encaisse_Tfd.setDisable(true);
                regler_Rbt.setSelected(true);
                tableVentes.getSelectionModel().clearSelection();
                tableVentes.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (rep) {
            }

        }else{

        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!"CAISSIER".equals(loggedUser.getRole().getNomRole())){
            validFactureBtn.setVisible(false);
            printFactureBtn.setVisible(false);
        }

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };



        montant_encaisse_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        montant_encaisse_Tfd.setText("");

        num_bs_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
            Vente v = parametre.getValue();
            return Bindings.createStringBinding(v::getNumeroVente);
        });



        client_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
            Vente v = parametre.getValue();
            return Bindings.createStringBinding(v.getClient()::getNomC);
        });

        date_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
            Vente v = parametre.getValue();
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            return Bindings.createStringBinding(() -> {
                return dt1.format(v.getVenteDate());
            });
        });

        montant_total_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
            Vente v = parametre.getValue();

            return Bindings.createStringBinding(() -> v.getMontantvente().toString());
        });

        Callback<TableColumn<Vente, String>, TableCell<Vente, String>> detailsFactory
                = //
                new Callback<TableColumn<Vente, String>, TableCell<Vente, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Vente, String> param) {
                        final TableCell<Vente, String> cell = new TableCell<Vente, String>() {

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

                                    btn.setId("detailsbutton");
                                    //btn.setText("modifier");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Voir les détails de cette vente"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedVenteEnCours= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.DETAILSVENTEENCOURS));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Détails vente en cours -=-");
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            stage.setResizable(false);
                                            stage.initOwner(
                                                    ((Node) event.getSource()).getScene().getWindow());
                                            stage.show();



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

        Callback<TableColumn<Vente, String>, TableCell<Vente, String>> annulerFactory
                = //
                new Callback<TableColumn<Vente, String>, TableCell<Vente, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Vente, String> param) {
                        final TableCell<Vente, String> cell = new TableCell<Vente, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.REMOVE);
                                    icon.setSize("2em");

                                    btn.setId("cancelbutton");
                                    //btn.setText("modifier");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Annuler cette vente"));
                                    if(!"CAISSIER".equals(loggedUser.getRole().getNomRole())){
                                        btn.setVisible(false);
                                    }
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedVenteEnCours= getTableView().getItems().get(getIndex());

                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Informations");
                                            alert.setHeaderText("Vous êtes sur le point d'annuler cette vente.");
                                            alert.setContentText("Cette action est irréversible.\nEtes-vous d'accord?");
                                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                            try {
                                                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            Optional<ButtonType> result = alert.showAndWait();
                                            if (result.get() == ButtonType.OK){

                                                boolean rep = RessourcesDAO.getIVente().annulerVente(selectedVenteEnCours);
                                                if(rep){
                                                    ObservableList<Vente> obVente = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVente()
                                                            .stream().filter(v->v.getStatusVente()==1).collect(Collectors.toList()));

                                                    tableVentes.getSelectionModel().clearSelection();
                                                    filteredData = new FilteredList<>(obVente, u -> true);
                                                    tableVentes.setItems(obVente);
                                                }

                                            }else {
                                            // ... user chose CANCEL or closed the dialog
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


        details_column.setCellFactory(detailsFactory);
        annuler_column.setCellFactory(annulerFactory);

        ObservableList<Vente> obVente = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVente()
        .stream().filter(v->v.getStatusVente()==1).collect(Collectors.toList()));

        tableVentes.getSelectionModel().clearSelection();
        filteredData = new FilteredList<>(obVente, u -> true);
        tableVentes.setItems(obVente);


        montant_encaisse_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!oldValue.equals(newValue)){

                try {
                    if(Long.parseLong(newValue)<=selectedVenteEnCours.getMontantvente()){
                        montant_restant_Tfd.setText(String.valueOf(selectedVenteEnCours.getMontantvente()-Long.parseLong(newValue)));
                    }else{
                        Message.alerteWithoutHeaderText("Informations","Le montant reçu ne peut pas être supérieur au \n" +
                                "au montant total des articles!");
                        montant_encaisse_Tfd.setText(oldValue);
                        return;
                    }
                }catch (NumberFormatException ignored){

                }

            }
        });



        tableVentes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        if (newValue != null && newValue != oldValue) {
                           radioBtnHbox.setDisable(false);
                           validFactureBtn.setDisable(false);
                           selectedVenteEnCours=newValue;
                           montant_total_Tfd.setText(String.valueOf(newValue.getMontantvente()));
                        } else {

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        );





    }
}
