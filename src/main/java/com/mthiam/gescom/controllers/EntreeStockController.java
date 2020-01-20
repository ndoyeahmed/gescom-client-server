package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.Rooting;
import com.mthiam.gescom.models.*;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static com.mthiam.gescom.controllers.AddArticleController.newStock;
import static com.mthiam.gescom.controllers.NouvelleEntreeController.currentEntree;
import static com.mthiam.gescom.controllers.SelectFournisseurController.selectedFournisseur;
import static javafx.fxml.FXMLLoader.load;

public class EntreeStockController implements Initializable {

    @FXML
    private TextField codeF_Tfd;

    @FXML
    private TextField nomF_Tfd;

    @FXML
    private TextField adresseF_Tfd;

    @FXML
    private TextField telephoneF_Tfd;

    @FXML
    private TextField bon_recep_Tfd;

    @FXML
    private TextField num_facture_Tfd;

    @FXML
    private TextField date_Tfd;

    @FXML
    private TextField montant_Tfd;

    @FXML
    private TextField restant_Tfd;

    @FXML
    private RadioButton regler_Rbt;

    @FXML
    private ToggleGroup estReglee;

    @FXML
    private RadioButton nonRegler_Rbt;

    @FXML
    private TableView<ArticleEntree> tableEntree;

    @FXML
    private TableColumn<ArticleEntree, String> designation_column;

    @FXML
    private TableColumn<ArticleEntree, String> qte_column;

    @FXML
    private TableColumn<ArticleEntree, String> pu_coumn;

    @FXML
    private TableColumn<ArticleEntree, String> montant_coumn;

    @FXML
    private TableColumn<ArticleEntree, String> remove_column;

    @FXML
    private TableView<Stock> tableArticle;

    @FXML
    private TableColumn<Stock, String> ref_article_column;

    @FXML
    private TableColumn<Stock, String> desi_article_column;

    @FXML
    private TextField filter_Tfd;

    @FXML
    private TextField quantite_Tfd;

    @FXML
    private Button addArticle_Btn;

    @FXML
    private TextField pu_Tfd;


    private ObservableList<Stock> obArticle;


    private ObservableList<ArticleEntree> articleEntrees;

    private List<ArticleEntree> articleEntreeList;



    private FilteredList<Stock> filteredData;

    private Stock selectedArticleFilter;





    @FXML
    void nonRegler(ActionEvent event) {
        restant_Tfd.setEditable(true);
    }

    @FXML
    void regler(ActionEvent event) {
        restant_Tfd.setEditable(false);
    }

    @FXML
    void creerProduit(ActionEvent event) {

        Stage stage = new Stage();
        Parent root = null;
        try {
            root = load(getClass().getResource(Rooting.ADDARTICLE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.setTitle("-=- GES'COM | Créer un nouveau article-=-");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        try {
            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.centerOnScreen();
        stage.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        stage.show();

        stage.setOnHidden((e) -> {

            if(null != newStock){



                articleEntreeList.add(newStock);
                tableArticle.getSelectionModel().clearSelection();
                articleEntrees = FXCollections.observableArrayList(articleEntreeList);
                tableEntree.setItems(articleEntrees);
                long montant = 0;
                try{
                    montant = Long.parseLong(montant_Tfd.getText());
                }catch (NumberFormatException ex){

                }

                montant_Tfd.setText(montant + newStock.getPu()*newStock.getQuantite()+"");
                newStock = null;


            }

        });



    }




    @FXML
    void handleSave(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

        if(articleEntreeList.size()==0){
            Message.alerteWithoutHeaderText("Informations","Veuillez selectionner des articles!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Vous êtes sur le point de valider cette entrée en stock.");
        alert.setContentText("Cette action est irréversible.\nEtes-vous d'accord?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            currentEntree.setMontantEntree(Long.parseLong(montant_Tfd.getText()));

            if(nonRegler_Rbt.isSelected()){
                if("".equals(restant_Tfd.getText())){
                    Message.alerteWithoutHeaderText("Informations","Veuillez renseigner le montant restant des articles!");
                    return;
                }else{
                    if(Long.parseLong(restant_Tfd.getText()) >= Long.parseLong(montant_Tfd.getText())){
                        Message.alerteWithoutHeaderText("Informations","Le montant restant ne peut pas être supérieur ou égal au \n" +
                                "au montant total des articles!");
                    }else{
                        currentEntree.setRestant(Long.parseLong(restant_Tfd.getText()));
                    }
                }
            }else{
                currentEntree.setRestant((long) 0);

            }

            //long rep = RessourcesDAO.getIEntree().addEntree(currentEntree);

            List<LigneEntree> ligneEntrees=new ArrayList<>();

            for(ArticleEntree a:articleEntreeList){
                LigneEntree l = new LigneEntree();


                if(null!=RessourcesDAO.getIEntree().getArticleById(Long.parseLong(String.valueOf(a.getIdArticle())))){
                    Stock s = RessourcesDAO.getIEntree().getArticleById(Long.parseLong(String.valueOf(a.getIdArticle())));


                    int qR= s.getQteRestante();
                    int qI = qR+a.getQuantite();

                    qR=qI;

                    s.setQteInitiale(qI);
                    s.setQteRestante(qR);
                    l.setStock(s);
                    l.setPu(a.getPu());
                    l.setQuantite(a.getQuantite());
                    ligneEntrees.add(l);
                }else{

                    Stock s = new Stock();
                    s.getArticle().setCodearticle(a.getCodearticle());
                    s.getArticle().setReference(a.getReference());
                    s.getArticle().setDesignation(a.getDesignation());
                    s.getArticle().setCategorie(a.getCategorie());
                    s.getArticle().setPrixBase(a.getPrixBase());
                    s.setQteAlerte(a.getQteAlerte());
                    s.setQteRestante(a.getQteInitiale());
                    s.setQteInitiale(a.getQteInitiale());
                    l.setStock(s);
                    l.setPu(a.getPu());
                    l.setQuantite(a.getQuantite());
                    ligneEntrees.add(l);


                }




            }

            boolean rep=false;
            try{
                rep = RessourcesDAO.getIEntree().addEntree(currentEntree,ligneEntrees);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            if(rep){
                Message.alerteWithoutHeaderText("Informations","Entrée en stock enregistrée avec succés!");
                articleEntreeList = null;
                articleEntrees = null;
                currentEntree = null;
                selectedArticleFilter = null;
                selectedFournisseur=null;
                montant_Tfd.setText("");
                restant_Tfd.setText("");
                tableArticle.getSelectionModel().clearSelection();
                tableEntree.getSelectionModel().clearSelection();
                codeF_Tfd.setText("");
                nomF_Tfd.setText("");
                adresseF_Tfd.setText("");
                telephoneF_Tfd.setText("");

                bon_recep_Tfd.setText("");
                num_facture_Tfd.setText("");
                date_Tfd.setText(String.valueOf(LocalDate.now()));
                obArticle=null;

//            BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
//
//            try {
//                contentarea = FXMLLoader.load(getClass().getResource(Rooting.GESSTOCK));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            border_pane.setCenter(contentarea);

            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }





    }

    @FXML
    void filterArticle(KeyEvent event) {
        filter_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Stock>) (Stock s) -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;

                } else if (s.getArticle().getDesignation().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }else if(s.getArticle().getReference().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                return false;
            });
        });
        SortedList<Stock> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableArticle.comparatorProperty());
        tableArticle.setItems(liste);
    }

    @FXML
    void addArticle(ActionEvent event) {

        ArticleEntree a = new ArticleEntree();

        a.setDesignation(selectedArticleFilter.getArticle().getDesignation());
        a.setIdArticle(selectedArticleFilter.getArticle().getId());
        a.setQuantite(Integer.parseInt(quantite_Tfd.getText()));
        a.setPu(Long.parseLong(pu_Tfd.getText()));

        articleEntreeList.add(a);

        quantite_Tfd.setText("");
        pu_Tfd.setText("");
        quantite_Tfd.setDisable(true);
        pu_Tfd.setDisable(true);
        addArticle_Btn.setDisable(true);

        tableArticle.getSelectionModel().clearSelection();
        articleEntrees = FXCollections.observableArrayList(articleEntreeList);
        tableEntree.setItems(articleEntrees);
        long montant = 0;
        try{
            montant = Long.parseLong(montant_Tfd.getText());
        }catch (NumberFormatException ex){

        }

        montant_Tfd.setText(montant + a.getPu()*a.getQuantite()+"");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         * La liste des articles qui existait en stock à approvisionner
         */
        articleEntreeList = new ArrayList<>();




        /**
         * Initialiser le champ montant à 0
         */
        montant_Tfd.setText(String.valueOf(0));


       if(currentEntree!=null){
           if(currentEntree.getFournisseur()!=null){
               codeF_Tfd.setText(currentEntree.getFournisseur().getCodeF());
               nomF_Tfd.setText(currentEntree.getFournisseur().getNomF());
               adresseF_Tfd.setText(currentEntree.getFournisseur().getAdresseF());
               telephoneF_Tfd.setText(currentEntree.getFournisseur().getTelephoneF());

               bon_recep_Tfd.setText(currentEntree.getNumeroEntree());
               num_facture_Tfd.setText(currentEntree.getNumeroFacture());
               date_Tfd.setText(String.valueOf(currentEntree.getEntreeDate()));
           }else{
               codeF_Tfd.setText("---");
               nomF_Tfd.setText("---");
               adresseF_Tfd.setText("---");
               telephoneF_Tfd.setText("---");

               bon_recep_Tfd.setText(currentEntree.getNumeroEntree());
               num_facture_Tfd.setText("---");
               date_Tfd.setText(String.valueOf(currentEntree.getEntreeDate()));
           }
       }


        desi_article_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getArticle().getDesignation()) );
        });

        ref_article_column.setCellValueFactory((TableColumn.CellDataFeatures<Stock, String> parametre) -> {
            Stock s = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(s.getArticle().getReference()));
        });

        obArticle = FXCollections.observableArrayList(RessourcesDAO.getIStock().getAllArticles());

        tableArticle.getSelectionModel().clearSelection();
        filteredData = new FilteredList<>(obArticle, u -> true);
        tableArticle.setItems(obArticle);


        ///////////////////////////////////////////////////////////////////////////////////
        ///INITIALISATION DES COLONNES DU TABLEVIEW ENTREES




        designation_column.setCellValueFactory((TableColumn.CellDataFeatures<ArticleEntree, String> parametre) -> {
            ArticleEntree a = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(a.getDesignation()) );
        });

        qte_column.setCellValueFactory((TableColumn.CellDataFeatures<ArticleEntree, String> parametre) -> {
            ArticleEntree a = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(a.getQuantite()));
        });

        pu_coumn.setCellValueFactory((TableColumn.CellDataFeatures<ArticleEntree, String> parametre) -> {
            ArticleEntree a= parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(a.getPu()) );
        });

        montant_coumn.setCellValueFactory((TableColumn.CellDataFeatures<ArticleEntree, String> parametre) -> {
            ArticleEntree a= parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(a.getPu()*a.getQuantite()) );
        });

        Callback<TableColumn<ArticleEntree, String>, TableCell<ArticleEntree, String>> removeFactory
                = //
                new Callback<TableColumn<ArticleEntree, String>, TableCell<ArticleEntree, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ArticleEntree, String> param) {
                        final TableCell<ArticleEntree, String> cell = new TableCell<ArticleEntree, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {

                                    final JFXButton btn = new JFXButton();
                                    FontAwesomeIcon icon = new FontAwesomeIcon();
                                    icon.setIcon(FontAwesomeIconName.TRASH);
                                    icon.setSize("1.5em");
                                    btn.setId("editbutton");
                                    btn.setGraphic(icon);
                                    btn.setTooltip(new Tooltip("Retirer cet article"));
                                    btn.setOnAction(event -> {
                                        try {
                                             ArticleEntree a = getTableView().getItems().get(getIndex());

                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Informations");
                                            alert.setHeaderText("Vous êtes sur le point de retirer cet article");
                                            alert.setContentText("Etes-vous d'accord?");
                                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                                            try {
                                                stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            Optional<ButtonType> result = alert.showAndWait();
                                            if (result.get() == ButtonType.OK){
                                                long montant = 0;
                                                try{
                                                    montant = Long.parseLong(montant_Tfd.getText());
                                                }catch (NumberFormatException ignored){

                                                }

                                                montant_Tfd.setText(montant - a.getPu()*a.getQuantite()+"");
                                                articleEntreeList.remove(a);


                                                System.out.println(articleEntreeList.size());
                                                articleEntrees = FXCollections.observableArrayList(articleEntreeList);
                                                tableEntree.setItems(articleEntrees);
                                            } else {
                                                // ... user chose CANCEL or closed the dialog
                                            }
                                        }catch (Exception ex){

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



        remove_column.setCellFactory(removeFactory);
        articleEntrees = FXCollections.observableArrayList(articleEntreeList);
        tableEntree.setItems(articleEntrees);

        /**
         * ////////////////////////////////////////////////////////////////////////////
         */

        tableArticle.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        if (newValue != null && newValue != oldValue) {
                            quantite_Tfd.setDisable(false);
                            addArticle_Btn.setDisable(false);

                            pu_Tfd.setDisable(false);

                            selectedArticleFilter = newValue;


                        } else {

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        );


        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };



        quantite_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        quantite_Tfd.setText("");

        pu_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        pu_Tfd.setText("");

        restant_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        restant_Tfd.setText("");


    }
}
