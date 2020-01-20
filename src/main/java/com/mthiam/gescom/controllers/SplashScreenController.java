package com.mthiam.gescom.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController implements Initializable {
    @FXML
    private Label progress;
    public static Label label;
    public static Label chargement;

    @FXML
    private ProgressBar progressBar;

    public static ProgressBar statProgressBar;
    @FXML
    private Label process_TextView;
    @FXML
    private ImageView logo_Img;

    @FXML
    private Label entreprise_name_Lbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        label = progress;
        statProgressBar = progressBar;
        chargement = process_TextView;

    }


}
