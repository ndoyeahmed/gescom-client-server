package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Rooting;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminMenuController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            Parent menu = FXMLLoader.load(getClass().getResource(Rooting.MENU));
            borderPane.setTop(menu);


            Parent contentarea = FXMLLoader.load(getClass().getResource(Rooting.DASHBORD));

            borderPane.setCenter(contentarea);

        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
