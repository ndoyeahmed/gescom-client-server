package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.PasswordUtil;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.mthiam.gescom.controllers.SignInController.loggedUser;

public class ChangePasswordController implements Initializable {

    @FXML
    private PasswordField password_Pfd;

    @FXML
    private PasswordField new_password_Pfd;

    @FXML
    private PasswordField confirm_password_Pfd;

    @FXML
    void resetPassword(ActionEvent event) {
        if("".equals(password_Pfd.getText().trim()) || "".equals(new_password_Pfd.getText().trim()) ||
        "".equals(confirm_password_Pfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner toutes les informations!");
            return;
        }else {

            if(PasswordUtil.verifyUserPassword(password_Pfd.getText(),loggedUser.getPasswordUtilisateur(),loggedUser.getSaltPassword())){

                if(Validator.isPassword(new_password_Pfd.getText())){
                    if(new_password_Pfd.getText().equals(confirm_password_Pfd.getText())){
                        boolean rep = RessourcesDAO.getIUser().changePassword(new_password_Pfd.getText(),loggedUser);
                        if(rep){
                            Message.alerteWithoutHeaderText("Informations","Changement mot de passe réussie!");
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                        }
                    }else{
                        Message.alerteWithoutHeaderText("Informations","Le nouveau mot de passe et le mot de passe confirmé ne correspondent pas!\nVeuillez ressayer!");
                    }
                }else{
                    Message.alerteWithoutHeaderText("Informations","Le format du mot de passe est invalide!\nLe mot de passe doit contenir au minimum 8 caractères, un chiffre et une lettre majuscule, une lettre minuscule!");
                    return;
                }

            }else{
                Message.alerteWithoutHeaderText("Informations","Mot de passe incorrect! \n Veuillez ressayer!");
                return;
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



    }
}
