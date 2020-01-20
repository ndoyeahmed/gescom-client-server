package com.mthiam.gescom.main;

import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.HibernateConfiguration;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.services.RessourcesDAO;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;



public class Main extends Application {

    private static final int COUNT_LIMIT = 10;

    public static List<String> chargements = new ArrayList<>();

    public static Stage stage = null;


    @Override
    public void init() throws Exception {
        HibernateConfiguration.initDatabase();
        RessourcesDAO.initInterfaces();

        chargements.add("Chargement des informations");
        chargements.add("Chargement des informations");
        chargements.add("Vérification de la base de données");
        chargements.add("Vérification de la base de données");
        chargements.add("Vérification de la base de données");
        chargements.add("Vérification de la base de données");
        chargements.add("Vérification de la base de données");
        chargements.add("GESCOM en cours de démarrage");
        chargements.add("GESCOM en cours de démarrage");
        chargements.add("GESCOM en cours de démarrage");
        chargements.add("GESCOM en cours de démarrage");
        chargements.add("GESCOM en cours de démarrage");
        chargements.add("GESCOM en cours de démarrage");

//        String password = "Passer@123";
//        String salt = PasswordUtil.getSalt(30);
//        System.out.println(salt);
//        String crypted = PasswordUtil.generateSecurePassword(password,salt);
//        System.out.println(crypted);
//        boolean decrypt = PasswordUtil.verifyUserPassword("Passer@123",crypted,salt);
//        if(decrypt){
//            System.out.println("success");
//        }else{
//            System.out.println("error");
//        }

        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        for (int i = 1; i <= COUNT_LIMIT; i++) {
            double progress = (double) i / 10;
            System.out.println("progress: " + progress);
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
            Thread.sleep(800);
        }





   }

    @Override
    public void start(Stage primaryStage) throws Exception{




        if(RessourcesDAO.getIConf().isNotConfigured()){
            Parent root = FXMLLoader.load(getClass().getResource(Rooting.PARAMETRAGE_VIEW));
            primaryStage.setTitle("-=- GES'COM | PARAMETRAGE -=-");
            Scene scene=new Scene(root);
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            //root.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
            primaryStage.getIcons().add(new Image("/icons/defaultLogo.png"));
            primaryStage.setResizable(false);
            primaryStage.show();
        }else if(!RessourcesDAO.getIConf().notHaveAdmin()){

            Parent root = FXMLLoader.load(getClass().getResource(Rooting.SIGNUPADMINVIEW));
            primaryStage.setTitle("-=- GES'COM | UTILISATEURS -=-");
            Scene scene=new Scene(root);
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            //root.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
            primaryStage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
            primaryStage.setResizable(false);
            primaryStage.show();
        }else{

            Parent root = FXMLLoader.load(getClass().getResource(Rooting.SIGNIN));
            primaryStage.setTitle("-=- GES'COM | "+AppConfiguration.getEntrepriseName()+"-==-");
            Scene scene=new Scene(root);
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.getIcons().add(new Image("file:"+AppConfiguration.getEntrepriseLogo()));
            //root.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
            primaryStage.setResizable(false);
            primaryStage.show();
        }


    }


    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, MyPreloader.class, args);
    }
}
