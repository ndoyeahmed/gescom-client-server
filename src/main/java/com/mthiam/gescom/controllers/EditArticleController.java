package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.Categorie;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static com.mthiam.gescom.controllers.GesStockController.selectedArticle;

public class EditArticleController implements Initializable {


    @FXML
    private TextField code_Tfd;

    @FXML
    private TextField reference_Tfd;

    @FXML
    private TextField designation_Tfd;

    @FXML
    private ComboBox<Categorie> categorie_Cbx;

    @FXML
    private TextField qte_alerte_Tfd;

    @FXML
    private TextField prix_Tfd;



    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void saveArticle(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        if("".equals(reference_Tfd.getText().trim()) || "".equals(designation_Tfd.getText().trim())
         || "".equals(prix_Tfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner toutes les informations");
        }else{
            if(!Validator.isAlphaNumericAndHyphen(reference_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Veuillez saisir une référence valide!");
            }
            if(!Validator.isAlphaNumericAndHyphen(designation_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Veuillez saisir une désignation valide!");
            }


            if(!"".equals(qte_alerte_Tfd.getText()) && !Validator.isPositifNumber(qte_alerte_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Veuillez saisir une quantité valide!");
            }

            if(!Validator.isPositifNumber(prix_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Veuillez saisir un prix valide!");
            }

            try{
                selectedArticle.getArticle().setReference(reference_Tfd.getText());
                selectedArticle.getArticle().setDesignation(designation_Tfd.getText());
                selectedArticle.getArticle().setPrixBase(Integer.parseInt(prix_Tfd.getText()));



                if(!"".equals(qte_alerte_Tfd.getText())){
                    try {
                        selectedArticle.setQteAlerte(Integer.parseInt(qte_alerte_Tfd.getText()));
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                selectedArticle.getArticle().setCategorie(categorie_Cbx.getValue());

                boolean reponse = RessourcesDAO.getIStock().updateStock(selectedArticle);
                if(reponse){
                    Message.alerteWithoutHeaderText("Informations","Article modifié avec succes!");
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
            }catch (Exception ex){
                Message.alerteWithoutHeaderText("Informations",ex.getMessage());
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        categorie_Cbx.setItems(FXCollections.observableArrayList(RessourcesDAO.getIStock().getAllCategories()));

        validQuantity();
        validPrice();

        if(selectedArticle!=null){
            code_Tfd.setText(selectedArticle.getArticle().getCodearticle());
            designation_Tfd.setText(selectedArticle.getArticle().getDesignation());
            reference_Tfd.setText(selectedArticle.getArticle().getReference());
            prix_Tfd.setText(String.valueOf(selectedArticle.getArticle().getPrixBase()));
            categorie_Cbx.setValue(selectedArticle.getArticle().getCategorie());
            qte_alerte_Tfd.setText(String.valueOf(selectedArticle.getQteAlerte()));
        }

    }

    private void validPrice(){
        /**
         * Cette methode permet de faire passer seulement des chiffres dans le champs
         * numero de port
         */

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        prix_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        prix_Tfd.setText("");
    }

    private void validQuantity(){
        /**
         * Cette methode permet de faire passer seulement des chiffres dans le champs
         * numero de port
         */

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        qte_alerte_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        qte_alerte_Tfd.setText("");
    }
}
