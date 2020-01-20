package com.mthiam.gescom.controllers;

import com.mthiam.gescom.config.Message;
import com.mthiam.gescom.config.NumeroGenerator;
import com.mthiam.gescom.config.Validator;
import com.mthiam.gescom.models.Client;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class SelectClientController implements Initializable {

    @FXML
    private TextField code_Tfd;

    @FXML
    private TextField nom_Tfd;

    @FXML
    private TextField adresse_Tfd;

    @FXML
    private TextField telephone_Tfd;

    @FXML
    private Button save_Btn;

    @FXML
    private TableView<Client> tableClient;

    @FXML
    private TableColumn<Client, String> nom_column;

    @FXML
    private TableColumn<Client, String> telephone_column;

    @FXML
    private TextField filter_Tfd;

    private ObservableList<Client> obClient;

    public static Client selectedClient = null;

    private FilteredList<Client> filteredData;

    @FXML
    void addNewClient(ActionEvent event) throws IOException, SAXException, ParserConfigurationException, URISyntaxException {
        if("".equals(nom_Tfd.getText().trim())){
            Message.alerteWithoutHeaderText("Informations","Veuillez renseigner au moins le nom du client!");
        }else{
            if(!Validator.isNomWhithWhiteEspace(nom_Tfd.getText())){
                Message.alerteWithoutHeaderText("Infpormations","Format du nom invalide!");
                return;

            }

            if(!"".equals(adresse_Tfd.getText()) && !Validator.isAlphaNumericAndHyphen(adresse_Tfd.getText())){
                Message.alerteWithoutHeaderText("Infpormations","Format de l'adresse invalide!");
                return;
            }

            if(!"".equals(telephone_Tfd.getText()) && RessourcesDAO.getIVente().getClientByTel(telephone_Tfd.getText())!=null){
                Message.alerteWithoutHeaderText("Infpormations","Ce numéro de téléphone est déjà associé à un client!");
                return;

            }

            Client c = new Client();
            c.setCodeC(code_Tfd.getText());
            c.setNomC(nom_Tfd.getText());
            c.setAdresseC(adresse_Tfd.getText());
            c.setTelephoneC(telephone_Tfd.getText());

            try{
                boolean reponse = RessourcesDAO.getIVente().addClient(c);

                if(reponse){
                    Message.alerteWithoutHeaderText("Informations","Client enregistré avec succés!");
                    code_Tfd.setText(NumeroGenerator.generateCodeClient());
                    nom_Tfd.setText("");
                    adresse_Tfd.setText("");
                    telephone_Tfd.setText("");
                    obClient = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllClient()
                    .stream().filter(cli->!cli.getNomC().equals("AU COMPTOIR")).collect(Collectors.toList()));
                    tableClient.getSelectionModel().clearSelection();
                    tableClient.setItems(obClient);
                }
            }catch (Exception ex){

            }
        }
    }

    public void doubleclick() {
        tableClient.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    selectedClient= tableClient.getSelectionModel().getSelectedItem();
                    obClient=null;
                    ((Node)(event.getSource())).getScene().getWindow().hide();

                }
            }
        });
    }


    @FXML
    void filter(KeyEvent event) {

        filter_Tfd.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Client>) (Client c) -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;

                } else if (c.getNomC().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }else if(!"".equals(c.getTelephoneC()) && c.getTelephoneC().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                return false;
            });
        });
        SortedList<Client> liste = new SortedList<>(filteredData);
        liste.comparatorProperty().bind(tableClient.comparatorProperty());
        tableClient.setItems(liste);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        code_Tfd.setText(NumeroGenerator.generateCodeClient());

        ///////////////////////////////////////////////////////////////////////////////////
        ///INITIALISATION DES COLONNES DU TABLEVIEW


        nom_column.setCellValueFactory((TableColumn.CellDataFeatures<Client, String> parametre) -> {
            Client cli = parametre.getValue();
            return Bindings.createStringBinding(() -> cli.getNomC());
        });

        telephone_column.setCellValueFactory((TableColumn.CellDataFeatures<Client, String> parametre) -> {
            Client cli = parametre.getValue();
            return Bindings.createStringBinding(() -> cli.getTelephoneC());
        });

        obClient = FXCollections.observableArrayList(RessourcesDAO.getIVente().getAllClient()
                .stream().filter(c->!c.getNomC().equals("AU COMPTOIR")).collect(Collectors.toList()));
        filteredData = new FilteredList<>(obClient, u -> true);
        tableClient.setItems(obClient);

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        telephone_Tfd.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        telephone_Tfd.setText("");

        doubleclick();

    }
}
