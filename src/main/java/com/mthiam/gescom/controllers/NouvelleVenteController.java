package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.Vente;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mthiam.gescom.controllers.SelectClientController.selectedClient;
import static com.mthiam.gescom.controllers.SelectFournisseurController.selectedFournisseur;
import static com.mthiam.gescom.controllers.SignInController.loggedUser;
import static javafx.fxml.FXMLLoader.load;

public class NouvelleVenteController implements Initializable {

    @FXML
    private DatePicker date_entree_Dpk;

    @FXML
    private ComboBox<String> client_Cbx;

    @FXML
    private TextField bon_sortie_Tfd;

    public static Vente currentVente;

    @FXML
    void add_forunisseur_Btn(ActionEvent event) {

        try{
            Stage stage = new Stage();
            Parent root = load(getClass().getResource(Rooting.SELECTCLIENT));
            stage.setScene(new Scene(root));
            stage.setTitle("-=- GES'COM | Liste des client-=-");
            stage.initModality(Modality.WINDOW_MODAL);

            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            stage.centerOnScreen();
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();

            stage.setOnHidden((e) -> {
                if (selectedClient != null) {
                    client_Cbx.setValue(selectedClient.getNomC());

                }else{
                    currentVente=null;

                }

            });

            stage.setOnCloseRequest((e) -> {
                if (selectedClient != null) {
                    client_Cbx.setValue(selectedFournisseur.getNomF());

                }else{
                    currentVente=null;

                }

            });


        }catch (Exception ex){
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void toNext(ActionEvent event) {
        currentVente = new Vente();
        currentVente.setNumeroVente(bon_sortie_Tfd.getText());

        if(selectedClient!=null) {
            currentVente.setClient(selectedClient);

        }else{
            currentVente.setClient(RessourcesDAO.getIVente().getDefaultClient());


        }

        currentVente.setVenteDate(java.sql.Date.valueOf(date_entree_Dpk.getValue()));
        currentVente.setUtilisateur(loggedUser);

        ((Node)(event.getSource())).getScene().getWindow().hide();
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

        client_Cbx.setValue("AU COMPTOIR");


        date_entree_Dpk.setValue(LocalDate.now());

        bon_sortie_Tfd.setText(NumeroGenerator.generateNumVente());

    }
}
