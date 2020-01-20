package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.*;
import com.mthiam.gescom.models.Entree;
import com.mthiam.gescom.models.EntreeExport;
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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListeEntreesController implements Initializable {

    @FXML
    private TableView<Entree> tableEntrees;

    @FXML
    private TableColumn<Entree, String> num_br_column;

    @FXML
    private TableColumn<Entree, String> date_column;

    @FXML
    private TableColumn<Entree, String> fournisseur_column;

    @FXML
    private TableColumn<Entree, String> num_facture_column;

    @FXML
    private TableColumn<Entree, String> montant_total_column;

    @FXML
    private TableColumn<Entree, String> montant_verse_column;

    @FXML
    private TableColumn<Entree, String> montant_restant_column;

    @FXML
    private TableColumn<Entree, String> details_column;

    @FXML
    private DatePicker date_Dpk;

    @FXML
    private TextField fournisseur_Tfd;

    @FXML
    private TextField facture_Tfd;

    public static Entree selectedEntree;

    private FilteredList<Entree> filteredData;

    private ObservableList<Entree> obEntrees = null;


    @FXML
    void exportEntreesList(ActionEvent event) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {

        List<EntreeExport> liste = new ArrayList<>();

        List<Entree> entrees = RessourcesDAO.getIEntree().getAllEntrees();

        System.out.println(entrees.size());

        for(Entree entree:entrees){

            EntreeExport e = new EntreeExport();

            e.setNumeroEntree(entree.getNumeroEntree());
            if(null==entree.getNumeroFacture()){
                e.setNumeroFacture("-------");

            }else{
                e.setNumeroFacture(entree.getNumeroFacture());
            }
            e.setEntreeDate(entree.getEntreeDate());
            e.setMontantEntree(entree.getMontantEntree());
            e.setRestant(entree.getRestant());
            if(null != entree.getFournisseur()){
                e.setFournisseur(entree.getFournisseur().getNomF());
            }else{
                e.setFournisseur("-------");
            }
            e.setUtilisateur(entree.getUtilisateur().getPrenomUtilisateur()+" "+entree.getUtilisateur().getNomUtilisateur());


            liste.add(e);
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Exporter la liste des entrées vers Excel");
        alert.setContentText("Etes-vous d'accord?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                FileExporter.exportEntreesToExcel(liste,"Liste entrées effectuées","Exportation-Entrees-"+ NumeroGenerator.maDate());
                Message.alerteWithoutHeaderText("Informations","Liste des entrées exportées avec succès!");
            }catch (Exception ex){
                Message.alerteWithoutHeaderText("Informations",ex.getMessage());
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    @FXML
    void filterByFournisseur(KeyEvent event) {

        fournisseur_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Entree>) (Entree e) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (null != e.getFournisseur() && e.getFournisseur().getNomF().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Entree> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableEntrees.comparatorProperty());
        tableEntrees.setItems(liste);


    }

    @FXML
    void filterByFacture(KeyEvent event) {

        facture_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Entree>) (Entree e) -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;

                } else if (null != e.getNumeroFacture() && e.getNumeroFacture().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Entree> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableEntrees.comparatorProperty());
        tableEntrees.setItems(liste);

    }

    private Callback<DatePicker, DateCell> getDayCellFactory() {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);


                        if (item.compareTo(LocalDate.now()) > 0) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Factory to create Cell of DatePicker
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        date_Dpk.setDayCellFactory(dayCellFactory);

        num_br_column.setCellValueFactory((TableColumn.CellDataFeatures<Entree, String> parametre) -> {
            Entree e = parametre.getValue();
            return Bindings.createStringBinding(e::getNumeroEntree);
        });

        num_facture_column.setCellValueFactory((TableColumn.CellDataFeatures<Entree, String> parametre) -> {
            Entree e = parametre.getValue();
            if(null == e.getNumeroFacture()){
                return Bindings.createStringBinding(()-> "---");
            }else{
                return Bindings.createStringBinding(e::getNumeroFacture);
            }
        });

        fournisseur_column.setCellValueFactory((TableColumn.CellDataFeatures<Entree, String> parametre) -> {
            Entree e = parametre.getValue();
            if(null!=e.getFournisseur()){
                return Bindings.createStringBinding(e.getFournisseur()::getNomF);
            }else{
                return Bindings.createStringBinding(()->"---");
            }
        });

        date_column.setCellValueFactory((TableColumn.CellDataFeatures<Entree, String> parametre) -> {
            Entree e = parametre.getValue();
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
            return Bindings.createStringBinding(() -> {
                return dt1.format(e.getEntreeDate());
            });
        });

        montant_total_column.setCellValueFactory((TableColumn.CellDataFeatures<Entree, String> parametre) -> {
            Entree e = parametre.getValue();

            return Bindings.createStringBinding(() -> e.getMontantEntree().toString());
        });

        montant_restant_column.setCellValueFactory((TableColumn.CellDataFeatures<Entree, String> parametre) -> {
            Entree e = parametre.getValue();
            return Bindings.createStringBinding(() -> e.getRestant().toString());
        });

        montant_verse_column.setCellValueFactory((TableColumn.CellDataFeatures<Entree, String> parametre) -> {
            Entree e = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(e.getMontantEntree()-e.getRestant()));
        });


        Callback<TableColumn<Entree, String>, TableCell<Entree, String>> detailsFactory
                = //
                new Callback<TableColumn<Entree, String>, TableCell<Entree, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Entree, String> param) {
                        final TableCell<Entree, String> cell = new TableCell<Entree, String>() {

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
                                    btn.setTooltip(new Tooltip("Voir les détails de cette entrée"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedEntree= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.DETAILSENTREE));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Détails entrée -=-");
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


        details_column.setCellFactory(detailsFactory);


        obEntrees = FXCollections.observableArrayList(RessourcesDAO.getIEntree().getAllEntrees());


        tableEntrees.getSelectionModel().clearSelection();
        filteredData = new FilteredList<>(obEntrees, u -> true);
        tableEntrees.setItems(obEntrees);


        date_Dpk.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate localDate = date_Dpk.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");


            List<Entree> entrees =
                    RessourcesDAO.getIEntree().getAllEntrees().stream().filter(e->dt1.format(e.getEntreeDate()).
                            equals(dt1.format(date))).collect(Collectors.toList());
            tableEntrees.getSelectionModel().clearSelection();
            obEntrees = FXCollections.observableArrayList(entrees);
            filteredData = new FilteredList<>(obEntrees, u -> true);
            tableEntrees.setItems(obEntrees);
        });











    }
}
