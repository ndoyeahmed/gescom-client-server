package com.mthiam.gescom.controllers;

import com.mthiam.gescom.models.StockCategorie;
import com.mthiam.gescom.services.RessourcesDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashbordController implements Initializable {

    @FXML
    private Label nb_articles;

    @FXML
    private Label nb_categorie;

    @FXML
    private Label nb_clients;

    @FXML
    private PieChart etat_stock_Pchart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{

            nb_articles.setText(String.valueOf(RessourcesDAO.getIStock().getAllArticles().size()));
            nb_categorie.setText(String.valueOf(RessourcesDAO.getIStock().getAllCategories().size()));
            nb_clients.setText(String.valueOf(RessourcesDAO.getIVente().getAllClient()
                    .stream().filter(c->!c.getNomC().equals("AU COMPTOIR")).collect(Collectors.toList()).size()));

        }catch (Exception ex){

        }

        List<PieChart.Data> pieChartData = new ArrayList<>();
        List<StockCategorie> liste=RessourcesDAO.getIVente().getNbStockByCategorie();
        for(int i=0;i<liste.size();i++){
            pieChartData.add(new PieChart.Data(liste.get(i).getNomCategorie(), liste.get(i).getNbArticles()));
        }
        etat_stock_Pchart.setTitle("Etat du stock par catégorie d'article");
        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " ", data.pieValueProperty(), " articles"
                        )
                )
        );


        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");

        for (final PieChart.Data data : etat_stock_Pchart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_DRAGGED,
                    e -> {
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        caption.setText(data.getPieValue()
                                + "%");
                    });
        }

        etat_stock_Pchart.setStartAngle(30);
        etat_stock_Pchart.setLegendSide(Side.LEFT);
        etat_stock_Pchart.setData(FXCollections.observableArrayList(pieChartData));




    }
}
