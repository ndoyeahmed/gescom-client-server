package com.mthiam.gescom.controllers;

import com.jfoenix.controls.JFXButton;
import com.mthiam.gescom.config.AppConfiguration;
import com.mthiam.gescom.config.Message;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static com.mthiam.gescom.controllers.NouvelleVenteController.currentVente;
import static com.mthiam.gescom.controllers.SelectClientController.selectedClient;

public class SortieStockController implements Initializable {

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
    private TextField date_Tfd;

    @FXML
    private TextField montant_Tfd;

//    @FXML
//    private TextField restant_Tfd;
//
//    @FXML
//    private RadioButton regler_Rbt;
//
//    @FXML
//    private ToggleGroup estReglee;
//
//    @FXML
//    private RadioButton nonRegler_Rbt;
//
//    @FXML
//    private TextField montant_verse_Tfd;

    @FXML
    private TextField filter_Tfd;

    @FXML
    private TableView<Stock> tableArticle;

    @FXML
    private TableColumn<Stock, String> ref_article_column;

    @FXML
    private TableColumn<Stock, String> desi_article_column;

    @FXML
    private TextField quantite_Tfd;

    @FXML
    private TextField pu_Tfd;

    @FXML
    private Button addArticle_Btn;

    @FXML
    private TableView<ArticleVente> tableVente;

    @FXML
    private TableColumn<ArticleVente, String> designation_column;

    @FXML
    private TableColumn<ArticleVente, String> qte_column;

    @FXML
    private TableColumn<ArticleVente, String> pu_coumn;

    @FXML
    private TableColumn<ArticleVente, String> montant_coumn;

    @FXML
    private TableColumn<ArticleVente, String> remove_column;

    @FXML
    private JFXButton save_Btn;


    private ObservableList<Stock> obArticle;


    private ObservableList<ArticleVente> articleVentes;

    private List<ArticleVente> ventelist;



    private FilteredList<Stock> filteredData;

    private Stock selectedArticleFilter;

    @FXML
    void addArticle(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        for(ArticleVente a:ventelist){
            if(selectedArticleFilter.getArticle().getReference().equals(a.getStock().getArticle().getReference())){
                Message.alerteWithoutHeaderText("Informations","Cette article est déjà ajouté au panier!");
                selectedArticleFilter = null;
                quantite_Tfd.setText("");
                pu_Tfd.setText("");
                tableArticle.getSelectionModel().clearSelection();
                return;
            }
        }
        if(Integer.parseInt(quantite_Tfd.getText())>selectedArticleFilter.getQteRestante()){
            Message.alerteWithoutHeaderText("Informations","La quantité restante en stock est inférieure à la quantité demandée");
            //selectedArticleFilter = null;
            quantite_Tfd.setText("");
            pu_Tfd.setText(String.valueOf(selectedArticleFilter.getArticle().getPrixBase()));

            return;
        }else{
            ArticleVente a = new ArticleVente();

            a.setStock(selectedArticleFilter);
            a.setPu(Long.parseLong(pu_Tfd.getText()));
            a.setQuantite(Integer.parseInt(quantite_Tfd.getText()));

            ventelist.add(a);

            quantite_Tfd.setText("");
            pu_Tfd.setText("");
            quantite_Tfd.setDisable(true);
            pu_Tfd.setDisable(true);
            addArticle_Btn.setDisable(true);

            tableArticle.getSelectionModel().clearSelection();
            articleVentes= FXCollections.observableArrayList(ventelist);
            tableVente.setItems(articleVentes);
            long montant = 0;
            try{
                montant = Long.parseLong(montant_Tfd.getText());
            }catch (NumberFormatException ex){

            }

            montant_Tfd.setText(montant + a.getPu()*a.getQuantite()+"");
        }
    }

    @FXML
    void creerProduit(ActionEvent event) {

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

//    @FXML
//    void handlePrint(ActionEvent event) throws IOException, SAXException, ParserConfigurationException {
//
//        Map<String, Object> parameters = new HashMap<String, Object>();
//        parameters.put("entrepriseName", AppConfiguration.getEntrepriseName());
//        parameters.put("idvente", currentVente.getId());
//        parameters.put("logo", AppConfiguration.getEntrepriseLogo());
//        parameters.put("adresse", AppConfiguration.getEntrepriseAddress()+" - "+
//                AppConfiguration.getEntrepriseFixeCall()+" - "+AppConfiguration.getEntrepriseMobileCall());
//        parameters.put("ninea","NINEA: "+AppConfiguration.getEntrepriseNinea()+" - RC: "+AppConfiguration.getEntrepriseRegistreDeCommerce());
//        GenerateFacture.showOrdonnance(parameters);
//
//    }

    @FXML
    void handleSave(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

        if(articleVentes.size()==0){
            Message.alerteWithoutHeaderText("Informations","Veuillez selectionner des articles!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations");
        alert.setHeaderText("Vous êtes sur le point de valider vente.");
        alert.setContentText("Cette action est irréversible.\nEtes-vous d'accord?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image("file:"+ AppConfiguration.getEntrepriseLogo()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            currentVente.setMontantvente(Long.parseLong(montant_Tfd.getText()));

            currentVente.setStatusVente(1);
            List<LigneVente> ligneVentes=new ArrayList<>();

            for(ArticleVente a:articleVentes){
                LigneVente l = new LigneVente();

                l.setPu(a.getPu());
                l.setQuantite(a.getQuantite());
                int qR= a.getStock().getQteRestante();

                qR-=a.getQuantite();

                a.getStock().setQteRestante(qR);
                l.setStock(a.getStock());

                ligneVentes.add(l);

            }

            boolean rep=false;
            try{
                rep = RessourcesDAO.getIVente().addVente(currentVente,ligneVentes);
            }catch (Exception ex){
                ex.printStackTrace();
            }

            if(rep){
                Message.alerteWithoutHeaderText("Informations","Vente enregistrée avec succés!");
                articleVentes = null;
                ventelist = null;
                selectedArticleFilter = null;
                selectedClient=null;
                tableArticle.getSelectionModel().clearSelection();
                tableVente.getSelectionModel().clearSelection();




                obArticle=null;

                save_Btn.setDisable(true);
                filter_Tfd.setDisable(true);



            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }



    }

//    @FXML
//    void nonRegler(ActionEvent event) {
//        montant_verse_Tfd.setDisable(false);
//    }
//
//    @FXML
//    void regler(ActionEvent event) {
//        montant_verse_Tfd.setText("");
//        montant_verse_Tfd.setDisable(true);
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * La liste des articles qui existait en stock à approvisionner
         */
        ventelist = new ArrayList<>();




        /**
         * Initialiser le champ montant à 0
         */
        montant_Tfd.setText(String.valueOf(0));

        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        if(currentVente!=null){
            if(currentVente.getClient()!=null){
                codeF_Tfd.setText(currentVente.getClient().getCodeC());
                nomF_Tfd.setText(currentVente.getClient().getNomC());
                adresseF_Tfd.setText(currentVente.getClient().getAdresseC());
                telephoneF_Tfd.setText(currentVente.getClient().getTelephoneC());
                bon_recep_Tfd.setText(currentVente.getNumeroVente());
                date_Tfd.setText(String.valueOf(dt1.format(currentVente.getVenteDate())));
            }else{
                codeF_Tfd.setText("---");
                nomF_Tfd.setText("---");
                adresseF_Tfd.setText("---");
                telephoneF_Tfd.setText("---");

                bon_recep_Tfd.setText(currentVente.getNumeroVente());

                date_Tfd.setText(String.valueOf(dt1.format(currentVente.getVenteDate())));
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




        designation_column.setCellValueFactory((TableColumn.CellDataFeatures<ArticleVente, String> parametre) -> {
            ArticleVente a = parametre.getValue();
            return Bindings.createStringBinding(() -> {
                return a.getStock().getArticle().getDesignation();
            });
        });

        qte_column.setCellValueFactory((TableColumn.CellDataFeatures<ArticleVente, String> parametre) -> {
            ArticleVente a = parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(a.getQuantite()));
        });

        pu_coumn.setCellValueFactory((TableColumn.CellDataFeatures<ArticleVente, String> parametre) -> {
            ArticleVente a= parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(a.getPu()) );
        });

        montant_coumn.setCellValueFactory((TableColumn.CellDataFeatures<ArticleVente, String> parametre) -> {
            ArticleVente a= parametre.getValue();
            return Bindings.createStringBinding(() -> String.valueOf(a.getPu()*a.getQuantite()) );
        });

        Callback<TableColumn<ArticleVente, String>, TableCell<ArticleVente, String>> removeFactory
                = //
                new Callback<TableColumn<ArticleVente, String>, TableCell<ArticleVente, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ArticleVente, String> param) {
                        final TableCell<ArticleVente, String> cell = new TableCell<ArticleVente, String>() {

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
                                            ArticleVente a = getTableView().getItems().get(getIndex());

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
                                                articleVentes.remove(a);
                                                ventelist.remove(a);



                                                articleVentes= FXCollections.observableArrayList(articleVentes);
                                                tableVente.setItems(articleVentes);
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
        articleVentes = FXCollections.observableArrayList(ventelist);
        tableVente.setItems(articleVentes);

        /**
         * ////////////////////////////////////////////////////////////////////////////
         */

        tableArticle.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        if (newValue != null && newValue != oldValue) {
                            quantite_Tfd.setDisable(false);
                            addArticle_Btn.setDisable(false);

                            pu_Tfd.setDisable(false);

                            pu_Tfd.setText(String.valueOf(newValue.getArticle().getPrixBase()));

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








//        montant_verse_Tfd.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if(!oldValue.equals(newValue)){
//                    try{
//                        if(Long.parseLong(newValue)<=Long.parseLong(montant_Tfd.getText())){
//                            restant_Tfd.setText(String.valueOf(Long.parseLong(montant_Tfd.getText())-Long.parseLong(newValue)));
//                        }else{
//                            try {
//                                Message.alerteWithoutHeaderText("Informations","Le montant versé ne peut pas être supérieur au montant total de la facture!");
//                                return;
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            return;
//                        }
//                    }catch (NumberFormatException nb){
//
//                    }
//                }
//            }
//        });
    }
}
