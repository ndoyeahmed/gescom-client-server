package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.FileExporter;
import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.models.ArticleExport;
import com.mthiam.gescom.models.Stock;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class ArticleFinStockController implements Initializable {

    @FXML
    private TableView<Stock> tableProduits;

    @FXML
    private TableColumn<Stock, String> code_column;

    @FXML
    private TableColumn<Stock, String> reference_column;

    @FXML
    private TableColumn<Stock, String> designation_column;

    @FXML
    private TableColumn<Stock, String> prix_column;

    @FXML
    private TableColumn<Stock, String> qte_initiale_stock;

    @FXML
    private TableColumn<Stock, String> qte_alerte_column;

    @FXML
    private TableColumn<Stock, String> qte_restante_column;


    public static Stock selectedArticle = null;



    private void initTableView(){
        ///////////////////////////////////////////////////////////////////////////////////
        ///INITIALISATION DES COLONNES DU TABLEVIEW


        code_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getArticle().getCodearticle()) );
        });

        designation_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getArticle().getDesignation()) );
        });

        reference_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getArticle().getReference()));
        });

        prix_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(String.valueOf(s.getArticle().getPrixBase())) );
        });

        qte_alerte_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getQteAlerte()) );
        });

        qte_initiale_stock.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getQteInitiale()) );
        });

        qte_restante_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getQteRestante()) );
        });

        ObservableList<Stock> obArticles=null;
        obArticles = FXCollections.observableArrayList(RessourcesDAO.getIStock().getStockAlerte());

        tableProduits.getSelectionModel().clearSelection();
        tableProduits.setItems(obArticles);
    }




    @FXML
    void exportStock(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

        List<ArticleExport> articles = new ArrayList<>();
        List<Stock> articleslist = RessourcesDAO.getIStock().getAllArticles();

        for(Stock stock:articleslist){

            ArticleExport a = new ArticleExport();

            a.setCodearticle(stock.getArticle().getCodearticle());
            a.setReference(stock.getArticle().getReference());
            a.setDesignation(stock.getArticle().getDesignation());
            a.setCategorie(stock.getArticle().getCategorie().getNomCategorie());
            a.setPrixBase(stock.getArticle().getPrixBase());
            a.setQteEnStock(stock.getQteRestante());
            a.setQteAlerte(stock.getQteAlerte());

            articles.add(a);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Exporter la liste des articles en fin de stock vers Excel");
        alert.setContentText("Etes-vous d'accord?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                FileExporter.exportArticleToExcel(articles,"Liste articles en fin de stock","Exportation-Article-Fin-Stock"+ NumeroGenerator.maDate());
                Message.alerteWithoutHeaderText("Informations","Liste des articles exportée avec succès!");
            }catch (Exception ex){
                Message.alerteWithoutHeaderText("Informations",ex.getMessage());
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initTableView();
    }
}
