package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.*;
import com.mthiam.gescom.main.Main;
import com.mthiam.gescom.models.Configuration;
import com.mthiam.gescom.models.Utilisateur;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpAdminController implements Initializable {

    @FXML
    private VBox content;

    @FXML
    private TextField prenom_Tfd;

    @FXML
    private TextField nom_Tfd;

    @FXML
    private TextField username_Tfd;

    @FXML
    private PasswordField password_Pfd;

    @FXML
    private PasswordField confirm_password_Pfd;



    @FXML
    void Register(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        if("".equals(nom_Tfd.getText().trim()) || "".equals(prenom_Tfd.getText().trim()) || "".equals(username_Tfd.getText().trim())
        || "".equals(password_Pfd.getText().trim()) || "".equals(confirm_password_Pfd.getText().trim())){
            Message.alerteWithoutHeaderText("Information","Veuillez renseigner toutes les informations!");
        }else{
            if(!Validator.isNomWhithWhiteEspace(prenom_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du prénom est invalide!");
                return;
            }

            if(!Validator.isNomWhithWhiteEspace(nom_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du nom est invalide!");
                return;
            }

            if(!Validator.isAlphaNumeric(username_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du nom d'utilisateur est invalide!");
                return;
            }

            if(!Validator.isPassword(password_Pfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du mot de passe est invalide!\nLe mot de passe doit contenir au minimum 8 caractères, un chiffre et une lettre majuscule, une lettre minuscule!");
                return;
            }

            if(!password_Pfd.getText().equals(confirm_password_Pfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Les mots de passes ne correspondent pas!");
                return;
            }

            if(RessourcesDAO.getIUser().getUserByUsername(username_Tfd.getText())!=null){
                Message.alerteWithoutHeaderText("Informations","Ce nom d'utilisateur est déjà utilisé!");
                return;
            }

            Utilisateur utilisateur=new Utilisateur();
            utilisateur.setPrenomUtilisateur(prenom_Tfd.getText());
            utilisateur.setNomUtilisateur(nom_Tfd.getText().toUpperCase());
            utilisateur.setLoginUtilisateur(username_Tfd.getText());

            String salt = PasswordUtil.getSalt(30);

            String crypted = PasswordUtil.generateSecurePassword(password_Pfd.getText(),salt);
            utilisateur.setSaltPassword(salt);
            utilisateur.setPasswordUtilisateur(crypted);
            utilisateur.setChange(true);
            utilisateur.setStatus(true);
            utilisateur.setRole(RessourcesDAO.getIUser().getRoleByName("ADMINISTRATEUR"));


            try{
                boolean rep = RessourcesDAO.getIUser().addUser(utilisateur);
                if(rep){
                    Message.alerteWithoutHeaderText("Informations","Utilisateur crée avec succés!");
                    Configuration c = RessourcesDAO.getIConf().getConfiguration();
                    c.setStatus_admin(true);
                    RessourcesDAO.getIConf().saveConfiguration(c);

                }else{
                    Message.alerteWithoutHeaderText("Informations","Erreur au moment de l'ajout de l'utilisateur!");
                }
                /**
                 * Redemarrage de l'application
                 */
                Stage stage = (Stage) content.getScene().getWindow();
                stage.close();
                Platform.runLater(() -> {
                    try {
                        new Main().start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
            if(!RessourcesDAO.getIConf().notHaveAdmin()){
                RessourcesDAO.getIUser().addProfiles();
                RessourcesDAO.getIVente().addDefaultClient();
            }
        }catch (Exception ex){
            try {
                Message.alerteWithoutHeaderText("Informations",ex.getMessage());
            } catch (Exception except)
            {

            }
        }
    }
}
