package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.PasswordUtil;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.Role;
import com.mthiam.gescom.models.Utilisateur;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {
    @FXML
    private TextField prenom_Tfd;

    @FXML
    private TextField nom_Tfd;

    @FXML
    private TextField login_Tfd;

    @FXML
    private PasswordField password_Pfd;

    @FXML
    private ComboBox<Role> role_Cbx;

    @FXML
    private RadioButton actif_Rbt;

    @FXML
    private ToggleGroup statusCompte;

    @FXML
    private RadioButton inactif_Rbt;


    @FXML
    void closePopup(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void saveUser(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        if("".equals(prenom_Tfd.getText().trim()) || "".equals(nom_Tfd.getText().trim())
                || "".equals(login_Tfd.getText().trim()) || "".equals(password_Pfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner tous les champs!");
        }else{
            if(!Validator.isNomWhithWhiteEspace(prenom_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du prénom est invalide!");
                return;
            }

            if(!Validator.isNomWhithWhiteEspace(nom_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du nom est invalide!");
                return;
            }

            if(!Validator.isNomWhithWhiteEspace(login_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du nom d'utilisateur est invalide!");
                return;
            }

            if(role_Cbx.getSelectionModel().getSelectedItem()==null){
                Message.alerteWithoutHeaderText("Informations","Veuillez attribuer un role à cet utilisateur!");
                return;
            }

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setPrenomUtilisateur(prenom_Tfd.getText());
            utilisateur.setNomUtilisateur(nom_Tfd.getText());
            utilisateur.setLoginUtilisateur(login_Tfd.getText());
            String salt = PasswordUtil.getSalt(30);
            String crypted = PasswordUtil.generateSecurePassword(password_Pfd.getText(),salt);
            utilisateur.setPasswordUtilisateur(crypted);
            utilisateur.setSaltPassword(salt);
            if(actif_Rbt.isSelected()){
                utilisateur.setStatus(true);
            }else{
                utilisateur.setStatus(false);
            }

            utilisateur.setChange(false);
            utilisateur.setRole(role_Cbx.getSelectionModel().getSelectedItem());

            try{
                boolean reponse = RessourcesDAO.getIUser().addUser(utilisateur);
                if(reponse){
                    Message.alerteWithoutHeaderText("Informations","Utilisateur enregistré avec succés!");
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
            }catch (Exception ex){

            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        role_Cbx.setItems(FXCollections.observableList(RessourcesDAO.getIUser().getAllRole()));
    }
}
