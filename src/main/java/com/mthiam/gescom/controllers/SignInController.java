package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.*;
import com.mthiam.gescom.models.Utilisateur;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;


public class SignInController implements Initializable {

    public static Utilisateur loggedUser=null;

    @FXML
    private ImageView logo_Img;

    @FXML
    private TextField username_Tfd;

    @FXML
    private PasswordField password_Pfd;

    @FXML
    private Button connexion_Btn;



    private Utilisateur utilisateur;

    private Parent root;



    @FXML
    void se_Connecter(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {



        if("".equals(username_Tfd.getText().trim()) || "".equals(password_Pfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Renseigner le nom d'utilisateur ou mot de passe!");
            return;
        }else{
            if(!Validator.isAlphaNumeric(username_Tfd.getText())){
                Message.alerteWithoutHeaderText("Informations","Le format du nom d'utilisateur est incorrect!");
                resetFields();
                return;
            }
            utilisateur = RessourcesDAO.getIUser().login(username_Tfd.getText(),password_Pfd.getText());
            if(utilisateur!=null){
                loggedUser  = utilisateur;
                if(!utilisateur.isStatus()){
                    Message.alerteWithoutHeaderText("Informations","Votre compte est inactif, Veuillez attendre l'activation!");
                    resetFields();
                    return;
                }
                if(!utilisateur.isChange()){
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    root = FXMLLoader.load(getClass().getResource(Rooting.RESETPASSWORD));
                    Stage stage = new Stage();
                    stage.setTitle("-=- GES'COM | Changer mot de passe-=-");
                    stage.setScene(new Scene(root));
                    stage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.show();
                    return;

                }else{
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();

                    root = FXMLLoader.load(getClass().getResource(Rooting.ADMINMENU));
                    Stage stage = new Stage();
                    stage.setTitle("-=- GES'COM | Menu principal-=-");
                    stage.setScene(new Scene(root));
                    stage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.setX(bounds.getMinX());
                    stage.setY(bounds.getMinY());
                    stage.setWidth(bounds.getWidth());
                    stage.setHeight(bounds.getHeight());
                    stage.show();
                }

            }else{
                Message.alerteWithoutHeaderText("Informations","Nom d'utilisateur ou mot de passe incorrect!");
                resetFields();
                return;
            }
        }

    }


    private void resetFields(){
        username_Tfd.setText("");
        password_Pfd.setText("");
    }

    @FXML
    void se_Connecter_By_Enter(KeyEvent event) throws IOException {

        if (event.getCode() == KeyCode.U && event.isShortcutDown()) {
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = load(getClass().getResource(Rooting.RESETBYSUPERADMIN));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(root));
            stage.setTitle("-=- GES'COM | Réinitialiser un mot de passe -=-");
            stage.initModality(Modality.WINDOW_MODAL);

            try {
                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stage.centerOnScreen();
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();

        }


        if(event.getCode() == KeyCode.ENTER){
            if("".equals(username_Tfd.getText().trim()) || "".equals(password_Pfd.getText().trim())){
                Message.alerteWithoutHeaderText("Informations","Renseigner le nom d'utilisateur ou mot de passe!");
                return;
            }else{
                if(!Validator.isAlphaNumeric(username_Tfd.getText())){
                    Message.alerteWithoutHeaderText("Informations","Le format du nom d'utilisateur est incorrect!");
                    resetFields();
                    return;
                }
                utilisateur = RessourcesDAO.getIUser().login(username_Tfd.getText(),password_Pfd.getText());
                if(utilisateur!=null){
                    loggedUser  = utilisateur;
                    if(!utilisateur.isStatus()){
                        Message.alerteWithoutHeaderText("Informations","Votre compte est inactif, Veuillez attendre l'activation!");
                        resetFields();
                        return;
                    }
                    if(!utilisateur.isChange()){
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        root = FXMLLoader.load(getClass().getResource(Rooting.RESETPASSWORD));
                        Stage stage = new Stage();
                        stage.setTitle("-=- GES'COM | Changer mot de passe-=-");
                        stage.setScene(new Scene(root));
                        stage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
                        stage.setResizable(false);
                        stage.centerOnScreen();
                        stage.show();
                        return;

                    }else{
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                        Screen screen = Screen.getPrimary();
                        Rectangle2D bounds = screen.getVisualBounds();

                        root = FXMLLoader.load(getClass().getResource(Rooting.ADMINMENU));
                        Stage stage = new Stage();
                        stage.setTitle("-=- GES'COM | Menu principal-=-");
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
                        stage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
                        stage.setResizable(false);
                        stage.centerOnScreen();
                        stage.setX(bounds.getMinX());
                        stage.setY(bounds.getMinY());
                        stage.setWidth(bounds.getWidth());
                        stage.setHeight(bounds.getHeight());
                        stage.show();

                        stage.setOnCloseRequest((e) -> {

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Informations");
                            alert.setHeaderText("Vous êtes sur le point de fermer l'application");
                            alert.setContentText("Etes-vous d'accord?");
                            Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
                            try {
                                stage1.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK){
                                System.exit(3);
                            } else {
                                // ... user chose CANCEL or closed the dialog
                            }

                        });
                    }

                }else{
                    Message.alerteWithoutHeaderText("Informations","Nom d'utilisateur ou mot de passe incorrect!");
                    resetFields();
                    return;
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        connexion_Btn.getStyleClass().setAll("btn","btn-success");
        try {
            logo_Img.setImage(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
