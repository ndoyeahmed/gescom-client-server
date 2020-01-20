package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.Role;
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

import static com.mthiam.gescom.controllers.GesUsersController.selectedUser;

public class EditUserController implements Initializable {

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
                || "".equals(login_Tfd.getText().trim())){
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

            if(!Validator.isAlphaNumeric(login_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du nom d'utilisateur est invalide!");
                return;
            }

            if(!selectedUser.getLoginUtilisateur().equals(login_Tfd.getText()) &&
                    RessourcesDAO.getIUser().getUserByUsername(login_Tfd.getText())!=null){
                Message.alerteWithoutHeaderText("Informations","Ce nom d'utilisateur est déjà utilisé!");
                login_Tfd.setText(selectedUser.getLoginUtilisateur());
                return;
            }

            if(role_Cbx.getSelectionModel().getSelectedItem()==null){
                Message.alerteWithoutHeaderText("Informations","Veuillez attribuer un role à cet utilisateur!");
                return;
            }


            selectedUser.setPrenomUtilisateur(prenom_Tfd.getText());
            selectedUser.setNomUtilisateur(nom_Tfd.getText());
            selectedUser.setLoginUtilisateur(login_Tfd.getText());

            if(actif_Rbt.isSelected()){
                selectedUser.setStatus(true);
            }else{
                selectedUser.setStatus(false);
            }


            selectedUser.setRole(role_Cbx.getSelectionModel().getSelectedItem());

            try{
                boolean reponse = RessourcesDAO.getIUser().updateUser(selectedUser);
                if(reponse){
                    Message.alerteWithoutHeaderText("Informations","Utilisateur modifié avec succés!");
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
            }catch (Exception ex){

            }

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        role_Cbx.setItems(FXCollections.observableArrayList(RessourcesDAO.getIUser().getAllRole()));

        if(selectedUser!=null){
            prenom_Tfd.setText(selectedUser.getPrenomUtilisateur());
            nom_Tfd.setText(selectedUser.getNomUtilisateur());
            login_Tfd.setText(selectedUser.getLoginUtilisateur());
            password_Pfd.setDisable(true);
            role_Cbx.setValue(selectedUser.getRole());

            if(selectedUser.isStatus()){
                actif_Rbt.setSelected(true);
            }else{
                inactif_Rbt.setSelected(true);
            }

        }
    }
}
