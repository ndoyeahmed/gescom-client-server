package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.Fournisseur;
import com.mthiam.gescom.models.Stock;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class SelectFournisseurController implements Initializable {
    @FXML
    private TextField code_Tfd;

    @FXML
    private TextField nom_Tfd;

    @FXML
    private TextField adresse_Tfd;

    @FXML
    private TextField telephone_Tfd;

    @FXML
    private Button save_Btn;

    @FXML
    private TableView<Fournisseur> tableFournisseur;

    @FXML
    private TableColumn<Fournisseur, String> nom_column;

    @FXML
    private TableColumn<Fournisseur, String> telephone_column;

    @FXML
    private TextField filter_Tfd;

    private ObservableList<Fournisseur> obFournisseur;

    public static Fournisseur selectedFournisseur = null;

    private FilteredList<Fournisseur> filteredData;


    @FXML
    void filterFournisseur(KeyEvent event) {
        filter_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Fournisseur>) (Fournisseur f) -> {

                if (newValue.isEmpty()) {
                    return true;

                } else if (f.getNomF().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }else if(!"".equals(f.getTelephoneF()) && f.getTelephoneF().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                return false;
            });
        });
        SortedList<Fournisseur> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableFournisseur.comparatorProperty());
        tableFournisseur.setItems(liste);

    }

    @FXML
    void addNewFournisseur(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

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

            if(!"".equals(telephone_Tfd.getText()) && RessourcesDAO.getIEntree().getFournisseurByTel(telephone_Tfd.getText())!=null){
                Message.alerteWithoutHeaderText("Infpormations","Ce numéro de téléphone est déjà associé à un fournisseur!");
                return;

            }

            Fournisseur f = new Fournisseur();
            f.setCodeF(code_Tfd.getText());
            f.setNomF(nom_Tfd.getText());
            f.setAdresseF(adresse_Tfd.getText());
            f.setTelephoneF(telephone_Tfd.getText());

            try{
                boolean reponse = RessourcesDAO.getIEntree().addFournisseur(f);

                if(reponse){
                    Message.alerteWithoutHeaderText("Informations","Fournisseur enregistré avec succés!");
                    code_Tfd.setText(NumeroGenerator.generateCodeFournisseur());
                    nom_Tfd.setText("");
                    adresse_Tfd.setText("");
                    telephone_Tfd.setText("");
                    obFournisseur = FXCollections.observableArrayList(RessourcesDAO.getIEntree().getAllFournisseur());
                    filteredData = new FilteredList<>(obFournisseur, u -> true);
                    tableFournisseur.getSelectionModel().clearSelection();
                    tableFournisseur.setItems(obFournisseur);
                }
            }catch (Exception ex){

            }
        }

    }

    public void doubleclick() {
        tableFournisseur.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    selectedFournisseur= tableFournisseur.getSelectionModel().getSelectedItem();
                    obFournisseur=null;
                    ((Node)(event.getSource())).getScene().getWindow().hide();

                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        code_Tfd.setText(NumeroGenerator.generateCodeFournisseur());

        ///////////////////////////////////////////////////////////////////////////////////
        ///INITIALISATION DES COLONNES DU TABLEVIEW


        nom_column.setCellValueFactory((TableColumn.CellDataFeatures<Fournisseur, String> parametre) -> {
            Fournisseur f = parametre.getValue();
            return Bindings.createStringBinding(() -> f.getNomF());
        });

        telephone_column.setCellValueFactory((TableColumn.CellDataFeatures<Fournisseur, String> parametre) -> {
            Fournisseur f = parametre.getValue();
            return Bindings.createStringBinding(() -> f.getTelephoneF());
        });

        obFournisseur = FXCollections.observableArrayList(RessourcesDAO.getIEntree().getAllFournisseur());
        filteredData = new FilteredList<>(obFournisseur, u -> true);
        tableFournisseur.setItems(obFournisseur);

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

        doubleclick();
    }
}
