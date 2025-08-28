package it.italiandudes.jamazing_centralina.javafx.controllers.centralina;

import it.italiandudes.jamazing_centralina.utils.models.DoubleArrayPile;
import it.italiandudes.jamazing_centralina.utils.models.IntArrayPile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public final class ControllerSceneCentralinaGraphs implements Initializable {

    private static final DecimalFormat velocityFormatter = new DecimalFormat("#.##");

    // Graphics Components
    @FXML private GridPane gridPaneGraphContainer;
    private LineChart<Number, Number> distanceChart = null;
    private LineChart<Number, Number> velocityChart = null;

    // Initialize
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        NumberAxis xDistanceAxis = new NumberAxis();
        xDistanceAxis.setLabel("Tempo");
        NumberAxis yDistanceAxis = new NumberAxis();
        yDistanceAxis.setLabel("Distanza");
        distanceChart = new LineChart<>(xDistanceAxis, yDistanceAxis);
        distanceChart.setTitle("Distanza nel Tempo");
        distanceChart.setAnimated(false);
        distanceChart.getData().clear();

        NumberAxis xVelocityAxis = new NumberAxis();
        xVelocityAxis.setLabel("Tempo");
        NumberAxis yVelocityAxis = new NumberAxis();
        yVelocityAxis.setLabel("Velocità");
        velocityChart = new LineChart<>(xVelocityAxis, yVelocityAxis);
        velocityChart.setTitle("Velocità nel Tempo");
        velocityChart.setAnimated(false);
        velocityChart.getData().clear();

        gridPaneGraphContainer.add(distanceChart, 0, 1);
        gridPaneGraphContainer.add(velocityChart, 1, 1);
    }

    // EDT
    @FXML
    public void updateDistanceChart(@NotNull IntArrayPile distancePile){
        if (distanceChart == null) return;
        distanceChart.getData().clear();
        int[] values = distancePile.getElements();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i=0; i<values.length; i++) {
            series.getData().add(new XYChart.Data<>(i, values[i]));
        }
        distanceChart.getData().add(series);
    }
    @FXML
    public void updateVelocityChart(@NotNull DoubleArrayPile velocityPile){
        if (velocityChart == null) return;
        velocityChart.getData().clear();
        double[] values = velocityPile.getElements();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i=0; i<values.length; i++) {
            double value = values[i];
            if (value == Double.NEGATIVE_INFINITY || value == Double.POSITIVE_INFINITY) value = 0;
            //System.out.println("Value: " + value);
            value = (double) Math.round(value * 100) /100;
            series.getData().add(new XYChart.Data<>(i, value));
        }
        velocityChart.getData().add(series);
    }
}
