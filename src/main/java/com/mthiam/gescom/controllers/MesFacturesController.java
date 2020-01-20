package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.GenerateFacture;
import com.mthiam.gescom.models.Vente;
import com.mthiam.gescom.services.RessourcesDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

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

public class MesFacturesController implements Initializable {

    @FXML
    private TableView<Vente> tableFactures;

    @FXML
    private TableColumn<Vente, String> num_facture_column;

    @FXML
    private TableColumn<Vente, String> date_column;

    @FXML
    private TableColumn<Vente, String> client_column;

    @FXML
    private TableColumn<Vente, String> montant_total_column;

    @FXML
    private TableColumn<Vente, String> montant_verse_column;

    @FXML
    private TableColumn<Vente, String> montant_restant_column;

    @FXML
    private TableColumn<Vente, String> print_column;

    @FXML
    private ComboBox<String> typeFacture_Cbx;

    @FXML
    private DatePicker date_Dpk;

    @FXML
    private TextField fournisseur_Tfd;

    private static Vente selectedFActure = null;

    private ObservableList<Vente> obVente;

    private FilteredList<Vente> filteredData;

    @FXML
    void filterByFournisseur(KeyEvent event) {

        fournisseur_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Vente>) (Vente v) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (v.getClient().getNomC().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Vente> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableFactures.comparatorProperty());
        tableFactures.setItems(liste);

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

        ObservableList<String> liste_cbx =
                FXCollections.observableArrayList (
                        "Validée",
                        "Annulée"
                );
        typeFacture_Cbx.setItems(liste_cbx);

        typeFacture_Cbx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null && newValue != oldValue) {

                if("Validée".equals(newValue)){
                    obVente = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVente()
                            .stream().filter(v->v.getStatusVente()==2).collect(Collectors.toList()));


                    tableFactures.getSelectionModel().clearSelection();
                    filteredData = new FilteredList<>(obVente, u -> true);
                    tableFactures.setItems(obVente);
                }else{
                    obVente = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVente()
                            .stream().filter(v->v.getStatusVente()==3).collect(Collectors.toList()));


                    tableFactures.getSelectionModel().clearSelection();
                    filteredData = new FilteredList<>(obVente, u -> true);
                    tableFactures.setItems(obVente);
                }
            }


        });




        num_facture_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
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

        montant_restant_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
            Vente v = parametre.getValue();
            if(null==v.getRestant()){
                return Bindings.createStringBinding(() -> " ");
            }else{
                return Bindings.createStringBinding(() -> v.getRestant().toString());
            }
        });

        montant_verse_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
            Vente v = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(v.getMontantvente()-v.getRestant()));
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

                                    btn.setId("viewbutton");
                                    //btn.setText("modifier");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Voir la facture"));
                                    if(typeFacture_Cbx.getSelectionModel().getSelectedItem().equals("Annulée")){
                                        btn.setDisable(true);
                                    }
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedFActure= getTableView().getItems().get(getIndex());
                                            Map<String, Object> parameters = new HashMap<String, Object>();
                                            parameters.put("entrepriseName", AppConfiguration.getEntrepriseName());
                                            parameters.put("idvente", selectedFActure.getId());
                                            parameters.put("logo", AppConfiguration.getEntrepriseLogo());
                                            parameters.put("adresse", AppConfiguration.getEntrepriseAddress()+" - "+
                                                    AppConfiguration.getEntrepriseFixeCall()+" - "+AppConfiguration.getEntrepriseMobileCall()
                                                    +" - "+AppConfiguration.getEntrepriseEmail()+" - "+AppConfiguration.getEntrepriseWebSite());
                                            parameters.put("ninea","NINEA: "+AppConfiguration.getEntrepriseNinea()+" - RC: "+AppConfiguration.getEntrepriseRegistreDeCommerce());
                                            GenerateFacture.showOrdonnance(parameters);

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


        print_column.setCellFactory(detailsFactory);

        // Factory to create Cell of DatePicker
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        date_Dpk.setDayCellFactory(dayCellFactory);





        date_Dpk.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate localDate = date_Dpk.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");


            List<Vente> ventes;
            if(typeFacture_Cbx.getSelectionModel().getSelectedItem().equals("Validée")){
                ventes =
                        RessourcesDAO.getIVente().getAllVente().stream().filter(c->c.getStatusVente()==2).filter(e->dt1.format(e.getVenteDate()).
                                equals(dt1.format(date))).collect(Collectors.toList());
            }else{
                ventes =
                        RessourcesDAO.getIVente().getAllVente().stream().filter(c->c.getStatusVente()==3).filter(e->dt1.format(e.getVenteDate()).
                                equals(dt1.format(date))).collect(Collectors.toList());
            }
            tableFactures.getSelectionModel().clearSelection();
            obVente = FXCollections.observableArrayList(ventes);
            filteredData = new FilteredList<>(obVente, u -> true);
            tableFactures.setItems(obVente);
        });

    }
}
