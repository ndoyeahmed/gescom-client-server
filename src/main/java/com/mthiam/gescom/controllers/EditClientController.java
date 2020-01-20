package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.Client;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static com.mthiam.gescom.controllers.GesClientsController.selectedClientOnList;

public class EditClientController implements Initializable {

    @FXML
    private TextField code_Tfd;

    @FXML
    private TextField nom_Tfd;

    @FXML
    private TextField adresse_Tfd;

    @FXML
    private TextField telephone_Tfd;

    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

    @FXML
    void editClient(ActionEvent event) {
        if("".equals(nom_Tfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner au moins le nom du client!");
        }else{
            if(!Validator.isNomWhithWhiteEspace(nom_Tfd.getText())){
                Message.alerteWithoutHeaderText("Infpormations","Format du nom invalide!");
                return;

            }

            if(!"".equals(adresse_Tfd.getText()) && !Validator.isAlphaNumericAndHyphen(adresse_Tfd.getText())){
                Message.alerteWithoutHeaderText("Infpormations","Format de l'adresse invalide!");
                return;
            }

            if(!selectedClientOnList.getTelephoneC().equals(telephone_Tfd) && !"".equals(telephone_Tfd.getText()) && RessourcesDAO.getIEntree().getFournisseurByTel(telephone_Tfd.getText())!=null){
                Message.alerteWithoutHeaderText("Infpormations","Ce numéro de téléphone est déjà associé à un client!");
                return;

            }



            selectedClientOnList.setNomC(nom_Tfd.getText());
            selectedClientOnList.setAdresseC(adresse_Tfd.getText());
            selectedClientOnList.setTelephoneC(telephone_Tfd.getText());

            try{
                boolean reponse = RessourcesDAO.getIVente().updateClient(selectedClientOnList);

                if(reponse){
                    Message.alerteWithoutHeaderText("Informations","Client mis à jour avec succés!");
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
            }catch (Exception ex){

            }
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        telephone_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        telephone_Tfd.setText("");

        if(selectedClientOnList!=null){
            code_Tfd.setText(selectedClientOnList.getCodeC());
            nom_Tfd.setText(selectedClientOnList.getNomC());
            adresse_Tfd.setText(selectedClientOnList.getAdresseC());
            telephone_Tfd.setText(selectedClientOnList.getTelephoneC());
        }



    }
}
