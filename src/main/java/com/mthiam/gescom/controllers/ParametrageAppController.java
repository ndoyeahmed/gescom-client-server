package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.main.Main;
import com.mthiam.gescom.models.Configuration;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;


public class ParametrageAppController extends Window implements Initializable {

    @FXML
    private TextField entreprise_name_Tfd;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextField adresse_Tfd;

    @FXML
    private TextField tel_fixe_Tfd;

    @FXML
    private TextField tel_mobile_Tfd;

    @FXML
    private TextField email_Tfd;

    @FXML
    private TextField website_Tfd;

    @FXML
    private TextField ninea_Tfd;

    @FXML
    private TextField rc_Tfd;

    @FXML
    private Button setDirectory_Btn;

    @FXML
    private Button setLogo_Btn;

    @FXML
    private Label path_logo_Lbl;

    @FXML
    private Label path_directory_Lbl;






    @FXML
    void aPropos(ActionEvent event) {
        System.out.println("about");
    }

    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void savebtn(ActionEvent event) throws SAXException, IOException, ParserConfigurationException, URISyntaxException {
        if(entreprise_name_Tfd.getText().trim().equals("") || adresse_Tfd.getText().trim().equals("")
                || tel_mobile_Tfd.getText().equals("") ||
                path_logo_Lbl.getText().trim().equals("") || path_directory_Lbl.getText().trim().equals("")
                )
        {

                Message.alerteWithoutHeaderText("Informations","Veuillez renseigner toutes les informations");

        }else{
            if(!Validator.isAlphaNumericAndHyphen(entreprise_name_Tfd.getText())){

                    Message.alerteWithoutHeaderText("Informations","Le nom de l'entreprise est invalide");
                return;
            }
            if(!Validator.isAlphaNumericAndHyphen(adresse_Tfd.getText())){

                    Message.alerteWithoutHeaderText("Informations","L'adresse de l'entreprise est invalide");

                return;
            }
            if(tel_fixe_Tfd.getText().trim().length()!=0 && !Validator.isSenegaleseFixeNumber(tel_fixe_Tfd.getText())){


                    Message.alerteWithoutHeaderText("Informations","Le numéro de téléphone fixe de l'entreprise est invalide");

                return;

            }

            if(!Validator.isSenegaleseCallNumber(tel_mobile_Tfd.getText())){

                    Message.alerteWithoutHeaderText("Informations","Le numéro de téléphone mobile de l'entreprise est invalide");

                return;
            }

            if(email_Tfd.getText().trim().length()!=0 && !Validator.isEmail(email_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","L'adresse email de l'entreprise est invalide");
                return;

            }

            if(website_Tfd.getText().trim().length()!=0 && !Validator.isDomainName(website_Tfd.getText())){


                Message.alerteWithoutHeaderText("Informations","Le nom de domaine de l'entreprise est invalide");
                return;

            }

            if(ninea_Tfd.getText().trim().length()!=0 && !Validator.isAlphaNumericAndHyphen(ninea_Tfd.getText())){


                Message.alerteWithoutHeaderText("Informations","Le NINEA fixe de l'entreprise est invalide");
                return;

            }

            if(rc_Tfd.getText().trim().length()!=0 && !Validator.isAlphaNumericAndHyphen(rc_Tfd.getText())){

                Message.alerteWithoutHeaderText("Informations","Le régistre de commercefixe de l'entreprise est invalide");
                return;

            }

            Configuration configuration = new Configuration();
            configuration.setAppname(entreprise_name_Tfd.getText().toUpperCase());
            configuration.setAdresse(adresse_Tfd.getText());
            configuration.setTelephone_fixe(tel_fixe_Tfd.getText());
            configuration.setTelephone_mobile(tel_mobile_Tfd.getText());
            configuration.setEmail(email_Tfd.getText());
            configuration.setWeb(website_Tfd.getText());
            configuration.setNinea(ninea_Tfd.getText());
            configuration.setRc(rc_Tfd.getText());
            configuration.setLogo(path_logo_Lbl.getText());
            configuration.setDirectory(path_directory_Lbl.getText());

            try{
                RessourcesDAO.getIConf().saveConfiguration(configuration);
                Message.alerteWithoutHeaderText("Informations","Paramètrage enregistré avec succés!");
            }catch (Exception ex){
                Message.alerteWithoutHeaderText("Informations","Une erreur est survenue!");
            }


            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.close();
            Platform.runLater(() -> {
                try {
                    new Main().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }



    @FXML
    private void setDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this);

        if(selectedDirectory != null){
            path_directory_Lbl.setText(selectedDirectory.getAbsolutePath());
        }
    }



    @FXML
    private void setLogo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file;
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new
                FileChooser.ExtensionFilter("Image files (jpg,png)", "*.jpg","*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String fileAsString = file.toString();

            path_logo_Lbl.setText(fileAsString);
        } else {
            path_logo_Lbl.setText(null);
        }


    }

    /*private void validEntrepriseName(){
        *//**
         * Cette methode permet de tester si le nom de l'entreprise est alphabetique
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        entreprise_name_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isNomWhithWhiteEspace(newValue)){
                    entreprise_name_Tfd.getStyleClass().clear();
                    entreprise_name_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    entreprise_name_Tfd.getStyleClass().clear();
                    entreprise_name_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if(newValue.equals("")){
                entreprise_name_Tfd.getStyleClass().clear();
                entreprise_name_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validTelephoneFixe(){
        *//**
         * Cette methode permet de tester si le numero de telephone fixe de l'entreprise est correct
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        tel_fixe_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isSenegaleseFixeNumber(newValue)){
                    tel_fixe_Tfd.getStyleClass().clear();
                    tel_fixe_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    tel_fixe_Tfd.getStyleClass().clear();
                    tel_fixe_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if(newValue.equals("")){
                tel_fixe_Tfd.getStyleClass().clear();
                tel_fixe_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validTelephoneMobile(){
        *//**
         * Cette methode permet de tester si le numero de telephone mobile de l'entreprise est correct
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        tel_mobile_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isSenegaleseCallNumber(newValue)){
                    tel_mobile_Tfd.getStyleClass().clear();
                    tel_mobile_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    tel_mobile_Tfd.getStyleClass().clear();
                    tel_mobile_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if(newValue.equals("")){
                tel_mobile_Tfd.getStyleClass().clear();
                tel_mobile_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validAdresse(){
        *//**
         * Cette methode permet de tester si l'adresse de l'entreprise est alphabetique
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        adresse_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isAlphaNumericAndHyphen(newValue)){
                    adresse_Tfd.getStyleClass().clear();
                    adresse_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    adresse_Tfd.getStyleClass().clear();
                    adresse_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if(newValue.equals("")){
                adresse_Tfd.getStyleClass().clear();
                adresse_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validEmail(){
        *//**
         * Cette methode permet de tester si l'adresse email de l'entreprise est correcte
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        email_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isEmail(newValue)){
                    email_Tfd.getStyleClass().clear();
                    email_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    email_Tfd.getStyleClass().clear();
                    email_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if(newValue.equals("")){
                email_Tfd.getStyleClass().clear();
                email_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validWebsite(){
        *//**
         * Cette methode permet de tester si le nom de domaine de l'entreprise est correcte
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        website_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isDomainName(newValue)){
                    website_Tfd.getStyleClass().clear();
                    website_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    website_Tfd.getStyleClass().clear();
                    website_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if(newValue.equals("")){
                website_Tfd.getStyleClass().clear();
                website_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validNinea(){
        *//**
         * Cette methode permet de tester si lw nineade l'entreprise est correcte
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        ninea_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isAlphaNumericAndHyphen(newValue)){
                    ninea_Tfd.getStyleClass().clear();
                    ninea_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    ninea_Tfd.getStyleClass().clear();
                    ninea_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if(newValue.equals("")){
                ninea_Tfd.getStyleClass().clear();
                ninea_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validRC(){
        *//**
         * Cette methode permet de tester si lw registre de commerce de l'entreprise est correcte
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        rc_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isAlphaNumericAndHyphen(newValue)){
                    rc_Tfd.getStyleClass().clear();
                    rc_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    rc_Tfd.getStyleClass().clear();
                    rc_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if("".equals(newValue)){
                rc_Tfd.getStyleClass().clear();
                rc_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }

    private void validHost(){
        *//**
         * Cette methode permet de tester si l'adresse ip du serveur de la base
         * de données de  l'entreprise est correcte
         * si oui => le champs est styliser en bordure vert
         * si nom => le champs est styliser en bordure rouge
         *//*

        host_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!oldValue.equals(newValue)){
                if(Validator.isIpAddress(newValue)){
                    host_Tfd.getStyleClass().clear();
                    host_Tfd.getStyleClass().addAll("text-field","field-success");
                }else{
                    host_Tfd.getStyleClass().clear();
                    host_Tfd.getStyleClass().addAll("text-field","field-error");
                }
            }

            if("".equals(newValue)){
                host_Tfd.getStyleClass().clear();
                host_Tfd.getStyleClass().addAll("text-field");
            }
        });
    }
*/


    @Override
    public void initialize(URL location, ResourceBundle rb) {



        /**
         * Initialize SGBD combobox
         */



        /**
         * Set Boostrap style
         */

        setLogo_Btn.getStyleClass().setAll("btn","btn-success");
        setDirectory_Btn.getStyleClass().setAll("btn","btn-success");





    }
}
