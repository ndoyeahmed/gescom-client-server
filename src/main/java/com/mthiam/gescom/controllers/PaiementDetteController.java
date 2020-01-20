package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.VersementDette;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.mthiam.gescom.controllers.DettesFournisseurController.selectedDette;

public class PaiementDetteController implements Initializable {

    @FXML
    private TextField numero_paiement;

    @FXML
    private TextField somme_paiement;

    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void handleSave(ActionEvent event) {


        if("".equals(somme_paiement.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Renseigner le montant payé!");
            return;
        }else{
            if(!Validator.isPositifNumber(somme_paiement.getText())){
                Message.alerteWithoutHeaderText("Informations","La valeur saisie doit ëtre positive!");
                return;
            }
            if(Long.parseLong(somme_paiement.getText())>selectedDette.getEntree().getRestant()){
                Message.alerteWithoutHeaderText("Informations","Le montant payé ne peut pas être supérieur au montant restant \n Maximum "+selectedDette.getEntree().getRestant());
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Informations");
            alert.setHeaderText("Vous êtes sur le point de valider ce paiement");
            alert.setContentText("Cette action est irréversible.\nEtes-vous d'accord?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                VersementDette v = new VersementDette();
                v.setNumeroVersement(numero_paiement.getText());
                v.setMontant(Long.parseLong(somme_paiement.getText()));
                v.setVersementDate(java.sql.Date.valueOf(LocalDate.now()));
                long restant = selectedDette.getEntree().getRestant();
                restant-=Long.parseLong(somme_paiement.getText());

                if(restant==0){
                    selectedDette.setOk(true);
                }

                selectedDette.getEntree().setRestant(restant);
                v.setCreanceFournisseur(selectedDette);

                try{
                    RessourcesDAO.getIEntree().addPaiementDette(v);
                    Message.alerteWithoutHeaderText("Informations", "Paiement fournisseur effectué!");
                    ((Node)(event.getSource())).getScene().getWindow().hide();

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }



        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        numero_paiement.setEditable(false);
        numero_paiement.setText(NumeroGenerator.generateNumDetteVersement());
    }
}
