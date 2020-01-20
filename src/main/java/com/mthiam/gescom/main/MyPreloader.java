/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthiam.gescom.main;


import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.controllers.SplashScreenController;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MyPreloader extends Preloader {

    private Stage preloaderStage;
    private Scene scene;


    @Override
    public void init() throws Exception {               
                                         
    Parent root1 = FXMLLoader.load(getClass().getResource(Rooting.SPLASHVIEW));
    scene = new Scene(root1);                       
                
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
       this.preloaderStage = primaryStage;

        // Set preloader scene and show stage.
        preloaderStage.setScene(scene);  
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.getIcons().add(new Image("/icons/defaultLogo.png"));
        preloaderStage.show();
        
        
      
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        
        
          if (info instanceof ProgressNotification) {
            SplashScreenController.label.setText("Loading "+((ProgressNotification) info).getProgress()*100 + "%");
            System.out.println("Value@ :" + ((ProgressNotification) info).getProgress());
            SplashScreenController.chargement.setText(Main.chargements.get((int) (((ProgressNotification) info).getProgress()*10)));
            SplashScreenController.statProgressBar.setProgress(((ProgressNotification) info).getProgress());
            
            
            
            
        }

               
        
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
      
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            
            case BEFORE_START:
                // Called after MyApplication#init and before MyApplication#start is called.
                System.out.println("BEFORE_START");
                preloaderStage.hide();
                break;
            default:
                break;
        }
        
        
    }
}

