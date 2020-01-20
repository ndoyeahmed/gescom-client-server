package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.Fournisseur;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
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

import static com.mthiam.gescom.controllers.GesFournisseursController.selectedFournisseurOnList;

public class EditfournisseurController implements Initializable {

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
    void editFournisseur(ActionEvent event) {

        if("".equals(nom_Tfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner au moins le nom du fournisseur!");
        }else{
            if(!Validator.isNomWhithWhiteEspace(nom_Tfd.getText())){
                Message.alerteWithoutHeaderText("Infpormations","Format du nom invalide!");
                return;

            }

            if(!"".equals(adresse_Tfd.getSelectedText()) && !Validator.isAlphaNumericAndHyphen(adresse_Tfd.getText())){
                Message.alerteWithoutHeaderText("Infpormations","Format de l'adresse invalide!");
                return;
            }

            if(!selectedFournisseurOnList.getTelephoneF().equals(telephone_Tfd.getText()) && !"".equals(telephone_Tfd.getText()) && RessourcesDAO.getIEntree().getFournisseurByTel(telephone_Tfd.getText())!=null){
                Message.alerteWithoutHeaderText("Infpormations","Ce numéro de téléphone est déjà associé à un fournisseur!");
                return;

            }


            selectedFournisseurOnList.setNomF(nom_Tfd.getText());
            selectedFournisseurOnList.setAdresseF(adresse_Tfd.getText());
            selectedFournisseurOnList.setTelephoneF(telephone_Tfd.getText());

            try{
                boolean reponse = RessourcesDAO.getIEntree().updateFournisseur(selectedFournisseurOnList);

                if(reponse){
                    Message.alerteWithoutHeaderText("Informations","Fournisseur mis à jour avec succés!");
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

        if(selectedFournisseurOnList!=null){
            code_Tfd.setText(selectedFournisseurOnList.getCodeF());
            nom_Tfd.setText(selectedFournisseurOnList.getNomF());
            telephone_Tfd.setText(selectedFournisseurOnList.getTelephoneF());
            adresse_Tfd.setText(selectedFournisseurOnList.getAdresseF());
        }


    }
}
