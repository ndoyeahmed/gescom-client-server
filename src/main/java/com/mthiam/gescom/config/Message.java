/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthiam.gescom.config;

import com.mthiam.gescom.services.RessourcesDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/**
 *
 * @author mbaye
 */
public abstract class Message {

    public static void alerteWithoutHeaderText(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if(RessourcesDAO.getIConf().isNotConfigured()){
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
        }else{
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
        }
        alert.showAndWait();
    }

}
