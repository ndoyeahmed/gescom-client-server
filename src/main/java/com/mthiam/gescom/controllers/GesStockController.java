package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.*;
import com.mthiam.gescom.models.ArticleExport;
import com.mthiam.gescom.models.Categorie;
import com.mthiam.gescom.models.Stock;
import com.mthiam.gescom.services.RessourcesDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;



public class GesStockController implements Initializable {

    @FXML
    private ComboBox<Categorie> categorie_Cbx;

    @FXML
    private TextField reference_Tfd;

    @FXML
    private TextField designation_Tfd;

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

    @FXML
    private TableColumn<Stock, String> edit_column;


    public static Stock selectedArticle = null;



    private FilteredList<Stock> filteredData;



    @FXML
    void exportStock(ActionEvent event) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {

        List<ArticleExport> liste = new ArrayList<>();

        List<Stock> articles = RessourcesDAO.getIStock().getAllArticles();

        for(Stock stock:articles){

            ArticleExport a = new ArticleExport();

            a.setCodearticle(stock.getArticle().getCodearticle());
            a.setReference(stock.getArticle().getReference());
            a.setDesignation(stock.getArticle().getDesignation());
            a.setCategorie(stock.getArticle().getCategorie().getNomCategorie());
            a.setPrixBase(stock.getArticle().getPrixBase());
            a.setQteEnStock(stock.getQteRestante());
            a.setQteAlerte(stock.getQteAlerte());

            liste.add(a);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Exporter la liste des articles vers Excel");
        alert.setContentText("Etes-vous d'accord?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                FileExporter.exportArticleToExcel(liste,"Liste articles en stock","Exportation-Article-"+ NumeroGenerator.maDate());
                Message.alerteWithoutHeaderText("Informations","Liste des articles exportée avec succès!");
            }catch (Exception ex){
                Message.alerteWithoutHeaderText("Informations",ex.getMessage());
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }





    }

    @FXML
    void actualiserStock(ActionEvent event) {

        tableProduits.getItems().clear();

        List<Stock> ls = null;
        ls = RessourcesDAO.getIStock().getAllArticles();
        ObservableList<Stock> obArticle = FXCollections.observableList(ls);

        filteredData = new FilteredList<>(obArticle, u -> true);

        tableProduits.setItems(obArticle);
        tableProduits.refresh();
        tableProduits.getSelectionModel().clearSelection();

    }




    @FXML
    void filterByDesignation(KeyEvent event) {
        designation_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Stock>) (Stock s) -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;

                } else if (s.getArticle().getDesignation().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Stock> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableProduits.comparatorProperty());
        tableProduits.setItems(liste);
    }

    @FXML
    void filterByReference(KeyEvent event) {
        reference_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Stock>) (Stock s) -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;

                } else if (s.getArticle().getReference().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Stock> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableProduits.comparatorProperty());
        tableProduits.setItems(liste);
    }

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



        ///////////////////////////CHARGER LE COMBOBOX CATEGORIE //////////////////////////

        categorie_Cbx.setItems(FXCollections.observableArrayList(RessourcesDAO.getIStock().getAllCategories()));

        ////////////////////EVENEMENT COMBOBOX DE RECHERCHE////////////////////////////////
        categorie_Cbx.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null && newValue != oldValue) {

                ObservableList<Stock> obArticle = FXCollections.observableArrayList(RessourcesDAO.getIStock().getArticleByIdCategorie(newValue.getId()));

                filteredData = new FilteredList<>(obArticle, u -> true);
                tableProduits.setItems(obArticle);
            }
        });


        Callback<TableColumn<Stock, String>, TableCell<Stock, String>> editFactory
                = //
                new Callback<TableColumn<Stock, String>, TableCell<Stock, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Stock, String> param) {
                        final TableCell<Stock, String> cell = new TableCell<Stock, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
//                                    Image image = new Image("/icons/editIcon.png");
                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.PENCIL_SQUARE);
                                    icon.setSize("2em");

                                    btn.setId("editbutton");
                                    //btn.setText("modifier");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Modifier cet article"));
                                    btn.setOnAction(event -> {
                                        try {
                                            selectedArticle= getTableView().getItems().get(getIndex());

                                            Stage stage = new Stage();
                                            Parent root = FXMLLoader.load(getClass().getResource(Rooting.EDITARTICLE));
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("-=- GES'COM | Modifier un article -=-");
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            stage.setResizable(false);
                                            stage.initOwner(
                                                    ((Node) event.getSource()).getScene().getWindow());
                                            stage.show();

                                            stage.setOnHidden((e) -> {
                                                List<Stock> articles;
                                                try {
                                                    articles = RessourcesDAO.getIStock().getAllArticles();
                                                    ObservableList<Stock> obArticle = FXCollections.observableArrayList(articles);
                                                    tableProduits.getSelectionModel().clearSelection();
                                                    tableProduits.setItems(obArticle);
                                                    tableProduits.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });

                                            stage.setOnCloseRequest((e) -> {
                                                List<Stock> articles;
                                                try {
                                                    articles = RessourcesDAO.getIStock().getAllArticles();
                                                    ObservableList<Stock> obArticle  = FXCollections.observableArrayList(articles);
                                                    tableProduits.getSelectionModel().clearSelection();
                                                    tableProduits.setItems(obArticle);
                                                    tableProduits.refresh();
                                                } catch (Exception ex) {

                                                }


                                            });
                                        } catch (Exception ex) {
                                            Logger.getLogger(GesUsersController.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };



        edit_column.setCellFactory(editFactory);

        ObservableList<Stock> obArticle  = FXCollections.observableArrayList(RessourcesDAO.getIStock().getAllArticles());


        tableProduits.getItems().clear();
        filteredData = new FilteredList<>(obArticle, u -> true);
        tableProduits.setItems(obArticle);
        tableProduits.refresh();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initTableView();

    }
}
