package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.ArticleEntree;
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

public class AddArticleController implements Initializable {

    @FXML
    private TextField code_Tfd;

    @FXML
    private TextField reference_Tfd;

    @FXML
    private TextField designation_Tfd;

    @FXML
    private ComboBox<Categorie> categorie_Cbx;

    @FXML
    private TextField prix_achat_Tfd;

    @FXML
    private TextField prix_vente_Tfd;

    @FXML
    private TextField quantite_stock_Tfd;

    @FXML
    private TextField quantite_alerte_Tfd;

    public static ArticleEntree newStock=null;



    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void handleSave(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

        if("".equals(designation_Tfd.getText().trim()) ||
            null == categorie_Cbx.getValue() || "".equals(prix_vente_Tfd.getText()) ||
                "".equals(quantite_stock_Tfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner toutes les informations!");
            return;
        }else{
            if(!"".equals(reference_Tfd.getText()) && !Validator.isAlphaNumeric(reference_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format de la référence est invalide!");
                return;
            }

            if(!Validator.isAlphaNumericAndHyphen(designation_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format de la désignation est invalide!");
                return;
            }

            if(!"".equals(reference_Tfd.getText()) && null != RessourcesDAO.getIEntree().getArticleByReference(reference_Tfd.getText().toUpperCase())){
                Message.alerteWithoutHeaderText("Informations","Cette référence est déjà associée à un article!");
                return;
            }

            newStock = new ArticleEntree();
            newStock.setCodearticle(code_Tfd.getText());
            newStock.setReference(reference_Tfd.getText().toUpperCase());
            newStock.setDesignation(designation_Tfd.getText().toUpperCase());
            newStock.setPrixBase(Long.parseLong(prix_vente_Tfd.getText()));
            newStock.setCategorie(categorie_Cbx.getValue());
            newStock.setQteInitiale(Integer.parseInt(quantite_stock_Tfd.getText()));
            newStock.setQteRestante(Integer.parseInt(quantite_stock_Tfd.getText()));

            newStock.setQuantite(Integer.parseInt(quantite_stock_Tfd.getText()));

            if(!"".equals(prix_achat_Tfd.getText().trim())){
                newStock.setPu(Long.parseLong(prix_achat_Tfd.getText()));
            }else{
                newStock.setPu(0);
            }
            if(!"".equals(prix_achat_Tfd.getText().trim())){
                newStock.setPu(Integer.parseInt(prix_achat_Tfd.getText()));
            }else{
                newStock.setPu(0);
            }

            if(!"".equals(quantite_alerte_Tfd.getText().trim())){
                newStock.setQteAlerte(Integer.parseInt(quantite_alerte_Tfd.getText()));
            }else{
                newStock.setQteAlerte(0);
            }

            if("".equals(quantite_alerte_Tfd.getText())){
                newStock.setQteAlerte(0);
            }else{
                newStock.setQteAlerte(Integer.parseInt(quantite_alerte_Tfd.getText()));
            }

            ((Node)(event.getSource())).getScene().getWindow().hide();

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        code_Tfd.setText(NumeroGenerator.generateCodeArticle());

        categorie_Cbx.setItems(FXCollections.observableArrayList(RessourcesDAO.getIStock().getAllCategories()));


        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        prix_vente_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        prix_vente_Tfd.setText("");

        prix_achat_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        prix_achat_Tfd.setText("");

        quantite_alerte_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        quantite_alerte_Tfd.setText("");

        quantite_stock_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        quantite_stock_Tfd.setText("");
    }
}
