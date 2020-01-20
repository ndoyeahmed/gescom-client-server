package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.main.Main;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import static com.mthiam.gescom.controllers.SignInController.loggedUser;

public class ResetPasswordController implements Initializable {





    @FXML
    private PasswordField password_Pfd;

    @FXML
    private PasswordField confirm_password_Pfd;

    @FXML
    void resetPassword(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

        if("".equals(password_Pfd.getText().trim()) || "".equals(confirm_password_Pfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez saisir un nouveau mot de passe et confirmez le!");
            return;
        }else{
            if(!Validator.isPassword(password_Pfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du mot de passe est invalide!\nLe mot de passe doit contenir au minimum 8 caractères, un chiffre, une lettre majuscule, une lettre minuscule et un caractère spéciale!");
                return;
            }
            if(!password_Pfd.getText().equals(confirm_password_Pfd.getText())){
                Message.alerteWithoutHeaderText("Informations","les mots de passe ne correspondent pas!");
                return;
            }else{
                boolean reponse = RessourcesDAO.getIUser().changePassword(password_Pfd.getText(),loggedUser);

                if(!reponse){
                    Message.alerteWithoutHeaderText("Informations","Erreur au moment de changer le mot de passe!");
                    return;
                }else{
                    Message.alerteWithoutHeaderText("Informations","Mot de passe changé avec success!");
                    Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
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
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
