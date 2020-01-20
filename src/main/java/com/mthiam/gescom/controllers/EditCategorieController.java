package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.mthiam.gescom.controllers.GesCategorieController.selectedCat;

public class EditCategorieController implements Initializable {

    @FXML
    private TextField nom_cat_Tfd;


    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void saveCategorie(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        if("".equals(nom_cat_Tfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner un nom de catégorie!");
            return;
        }else{
            if(!Validator.isAlphaNumeric(nom_cat_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Nom de catégorie invalide!");
                return;
            }

            if(!selectedCat.getNomCategorie().equals(nom_cat_Tfd.getText()) &&
            RessourcesDAO.getIStock().getCategorieByName(nom_cat_Tfd.getText())!=null){
                Message.alerteWithoutHeaderText("Informations","Cette catégorie existe déjà!");
                return;
            }


            selectedCat.setNomCategorie(nom_cat_Tfd.getText().toUpperCase());

            try{
                RessourcesDAO.getIStock().editCategorie(selectedCat);
                Message.alerteWithoutHeaderText("Informations","Catégorie modifié avec succes!");
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }catch (Exception ex){
                Message.alerteWithoutHeaderText("Informations","Erreur modification de catégorie!");
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        if(selectedCat!=null){
            nom_cat_Tfd.setText(selectedCat.getNomCategorie());
        }
    }
}
