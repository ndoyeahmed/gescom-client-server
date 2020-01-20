package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.Entree;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mthiam.gescom.controllers.SelectFournisseurController.selectedFournisseur;
import static com.mthiam.gescom.controllers.SignInController.loggedUser;
import static javafx.fxml.FXMLLoader.load;

public class NouvelleEntreeController implements Initializable {

    @FXML
    private DatePicker date_entree_Dpk;

    @FXML
    private ComboBox<String> fournisseur_Cbx;

    @FXML
    private TextField bon_recep_Tfd;


    @FXML
    private VBox facture_view_Vbx;

    @FXML
    private TextField num_facture_Tfd;

    public static Entree currentEntree;

    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
        selectedFournisseur=null;
    }

    @FXML
    void toNext(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        currentEntree = new Entree();
        currentEntree.setNumeroEntree(bon_recep_Tfd.getText());

        if(selectedFournisseur!=null) {
            currentEntree.setFournisseur(selectedFournisseur);
            if (!"".equals(num_facture_Tfd.getText())) {
                currentEntree.setNumeroFacture(num_facture_Tfd.getText());
            }
        }else{
            currentEntree.setFournisseur(null);


        }

        currentEntree.setEntreeDate(java.sql.Date.valueOf(date_entree_Dpk.getValue()));
        currentEntree.setUtilisateur(loggedUser);

        ((Node)(event.getSource())).getScene().getWindow().hide();


    }

    @FXML
    void add_forunisseur_Btn(ActionEvent event) {
        try{
            Stage stage = new Stage();
            Parent root = load(getClass().getResource(Rooting.SELECTFOURNISSEUR));
            stage.setScene(new Scene(root));
            stage.setTitle("-=- GES'COM | Liste des fournisseurs-=-");
            stage.initModality(Modality.WINDOW_MODAL);

            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            stage.centerOnScreen();
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();

            stage.setOnHidden((e) -> {
                if (selectedFournisseur != null) {
                    fournisseur_Cbx.setValue(selectedFournisseur.getNomF());
                    facture_view_Vbx.setVisible(true);
                }else{
                    currentEntree=null;
                    //currentEntree.setFournisseur(null);
                }

            });

            stage.setOnCloseRequest((e) -> {
                if (selectedFournisseur != null) {
                    fournisseur_Cbx.setValue(selectedFournisseur.getNomF());
                    facture_view_Vbx.setVisible(true);
                }else{
                    currentEntree=null;
                    //currentEntree.setFournisseur(null);
                }
            });


        }catch (Exception ex){
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        date_entree_Dpk.setDayCellFactory(dayCellFactory);

        fournisseur_Cbx.setValue("Stock Initial");


        date_entree_Dpk.setValue(LocalDate.now());

        bon_recep_Tfd.setText(NumeroGenerator.generateNumBR());





    }


}
