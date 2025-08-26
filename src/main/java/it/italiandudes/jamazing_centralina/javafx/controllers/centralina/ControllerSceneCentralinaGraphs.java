package it.italiandudes.jamazing_centralina.javafx.controllers.centralina;

import it.italiandudes.jamazing_centralina.utils.models.DoubleArrayPile;
import it.italiandudes.jamazing_centralina.utils.models.IntArrayPile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

public final class ControllerSceneCentralinaGraphs implements Initializable {

    @FXML
    private LineChart<Integer, Integer> graph_distance;
    @FXML
    private LineChart<Integer, Double> graph_velocity;

    private XYChart.Series<Integer, Integer> distanceSeries = new XYChart.Series<>();
    private XYChart.Series<Integer, Double> velocitySeries = new XYChart.Series<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph_distance.getYAxis().setAutoRanging(true);
        graph_distance.getData().addAll(distanceSeries);

        graph_velocity.getYAxis().setAutoRanging(true);
        graph_velocity.getData().addAll(velocitySeries);
    }

    public void updateDistanceChart(@NotNull IntArrayPile distancePile){
        distanceSeries.getData().clear();
        int[] values = distancePile.getElements();
        for (int i = 0; i < values.length; i++){
            distanceSeries.getData().add(new XYChart.Data<>(i, values[i]));
        }
    }

    public void updateVelocityChart(@NotNull DoubleArrayPile velocityPile){
        //sSystem.out.println("Update chart called\nArray elements: " + velocityPile);
        velocitySeries.getData().clear();
        double[] values = velocityPile.getElements();
        for (int i = 0; i < values.length; i++){
            velocitySeries.getData().add(new XYChart.Data<>(i, values[i]));
        }
    }
}
