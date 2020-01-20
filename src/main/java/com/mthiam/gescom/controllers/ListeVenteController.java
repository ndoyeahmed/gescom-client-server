package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.*;
import com.mthiam.gescom.models.Vente;
import com.mthiam.gescom.models.VenteExport;
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

import static com.mthiam.gescom.controllers.SignInController.loggedUser;

public class ListeVenteController implements Initializable {

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
    private TableColumn<Vente, String> montant_verse_column;

    @FXML
    private TableColumn<Vente, String> montant_restant_column;

    @FXML
    private TableColumn<Vente, String> details_column;

    @FXML
    private DatePicker date_Dpk;

    @FXML
    private TextField fournisseur_Tfd;

    @FXML
    private TableColumn<Vente, String> etat_column;

    @FXML
    private Button exportVentesBtn;

    public static Vente selectedVente;

    private FilteredList<Vente> filteredData;

    private ObservableList<Vente> obVente = null;


    @FXML
    void exportVentesList(ActionEvent event) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {

        List<VenteExport> liste = new ArrayList<>();

        List<Vente> ventes = RessourcesDAO.getIVente().getAllVente()
                .stream().filter(v->v.getStatusVente()==2).collect(Collectors.toList());

        if(ventes.size()==0){
            Message.alerteWithoutHeaderText("Informations","Vous n'avez pas encore effectué de ventes validées!");
            return;
        }


        for(Vente vente:ventes){

            VenteExport v = new VenteExport();

            v.setNumeroVente(vente.getNumeroVente());

           v.setVenteDate(vente.getVenteDate());
            v.setMontantVente(vente.getMontantvente());
            v.setRestant(vente.getRestant());
            if(null != vente.getClient()){
                v.setClient(vente.getClient().getNomC());
            }else{
                v.setClient("-------");
            }
            v.setUtilisateur(vente.getUtilisateur().getPrenomUtilisateur()+" "+vente.getUtilisateur().getNomUtilisateur());


            liste.add(v);
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Exporter la liste des ventes vers Excel");
        alert.setContentText("Etes-vous d'accord?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                FileExporter.exportVentesToExcel(liste,"Liste ventes effectuées","Exportation-Ventes-"+ NumeroGenerator.maDate());
                Message.alerteWithoutHeaderText("Informations","Liste des ventes exportées avec succès!");
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

        if("GESTIONNAIRE DE STOCK".equals(loggedUser.getRole().getNomRole())){
            exportVentesBtn.setVisible(false);
        }

        // Factory to create Cell of DatePicker
        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        date_Dpk.setDayCellFactory(dayCellFactory);

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

        etat_column.setCellValueFactory((TableColumn.CellDataFeatures<Vente, String> parametre) -> {
            Vente v = parametre.getValue();

            if(v.getStatusVente()==1){
                return Bindings.createStringBinding(() -> "En cours");
            }else if(v.getStatusVente()==2){
                return Bindings.createStringBinding(() -> "Valider");
            }else{
                return Bindings.createStringBinding(() -> "Annulée");
            }

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
                                            selectedVente= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.DETAILSVENTE));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Détails vente-=-");
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


        obVente = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllVente());


        tableVentes.getSelectionModel().clearSelection();
        filteredData = new FilteredList<>(obVente, u -> true);
        tableVentes.setItems(obVente);


        date_Dpk.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate localDate = date_Dpk.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");


            List<Vente> ventes =
                    RessourcesDAO.getIVente().getAllVente().stream().filter(e->dt1.format(e.getVenteDate()).
                            equals(dt1.format(date))).collect(Collectors.toList());
            tableVentes.getSelectionModel().clearSelection();
            obVente = FXCollections.observableArrayList(ventes);
            filteredData = new FilteredList<>(obVente, u -> true);
            tableVentes.setItems(obVente);
        });

    }
}
