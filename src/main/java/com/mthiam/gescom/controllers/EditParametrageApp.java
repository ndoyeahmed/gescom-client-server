package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.main.Main;
import com.mthiam.gescom.models.Configuration;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditParametrageApp extends Window implements Initializable {

    @FXML
    private ToolBar toolBar;

    @FXML
    private TextField entreprise_name_Tfd;

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
    private Label path_directory_Lbl;

    @FXML
    private ImageView logo_Img;

    @FXML
    private Button saveBtn;

    @FXML
    private Button setDirectoryBtn;

    @FXML
    private Button setLogoBtn;

    private String logo=AppConfiguration.getEntrepriseLogo();

    @FXML
    void handleSave(ActionEvent event) {

        if(entreprise_name_Tfd.getText().trim().equals("") || adresse_Tfd.getText().trim().equals("")
                || tel_mobile_Tfd.getText().equals("") ||
                logo.trim().equals("") || path_directory_Lbl.getText().trim().equals("")
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Informations");
            alert.setHeaderText("Vous êtes sur le point de changer un ou des paramètres " +
                    "de l'application.\nAprès enregistrement l'application va redémarrer.");
            alert.setContentText("Etes-vous d'accord?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){

                Configuration configuration = RessourcesDAO.getIConf().getConfiguration();
                configuration.setAppname(entreprise_name_Tfd.getText().toUpperCase());
                configuration.setAdresse(adresse_Tfd.getText());
                configuration.setTelephone_fixe(tel_fixe_Tfd.getText());
                configuration.setTelephone_mobile(tel_mobile_Tfd.getText());
                configuration.setEmail(email_Tfd.getText());
                configuration.setWeb(website_Tfd.getText());
                configuration.setNinea(ninea_Tfd.getText());
                configuration.setRc(rc_Tfd.getText());
                configuration.setLogo(logo);
                configuration.setDirectory(path_directory_Lbl.getText());

                try{
                    RessourcesDAO.getIConf().saveConfiguration(configuration);
                    Message.alerteWithoutHeaderText("Informations","Paramètrage enregistré avec succés!");
                }catch (Exception ex){
                    Message.alerteWithoutHeaderText("Informations","Une erreur est survenue!");
                }


                stage = (Stage) toolBar.getScene().getWindow();

                stage.close();
                Platform.runLater(() -> {
                    try {
                        new Main().start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                // ... user chose CANCEL or closed the dialog
            }



        }

    }

    @FXML
    void setDirectory(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this);

        if(selectedDirectory != null){
            path_directory_Lbl.setText(selectedDirectory.getAbsolutePath());
        }

    }

    @FXML
    void setLogo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file;
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new
                FileChooser.ExtensionFilter("Image files (jpg,png)", "*.jpg","*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            logo = file.toString();
            logo_Img.setImage(new Image("file:"+logo));

        } else {
            logo = AppConfiguration.getEntrepriseLogo();
            logo_Img.setImage(new Image("file:"+logo));
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        logo_Img.setImage(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
        entreprise_name_Tfd.setText(AppConfiguration.getEntrepriseName());
        adresse_Tfd.setText(AppConfiguration.getEntrepriseAddress());
        tel_fixe_Tfd.setText(AppConfiguration.getEntrepriseFixeCall());
        tel_mobile_Tfd.setText(AppConfiguration.getEntrepriseMobileCall());
        email_Tfd.setText(AppConfiguration.getEntrepriseEmail());
        website_Tfd.setText(AppConfiguration.getEntrepriseWebSite());
        ninea_Tfd.setText(AppConfiguration.getEntrepriseNinea());
        rc_Tfd.setText(AppConfiguration.getEntrepriseRegistreDeCommerce());
        path_directory_Lbl.setText(AppConfiguration.getEntrepriseDirectory());


    }
}
