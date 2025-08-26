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
import java.util.ResourceBundle;

public final class ControllerSceneCentralinaGraphs implements Initializable {

    // Constants
    private final double SIGNAL_SCALE_FACTOR = 0.5;
    private final double Y_AXIS_UPPER_BOUND = 1000;
    private final double Y_NO_SIGNAL = Y_AXIS_UPPER_BOUND / 2;

    // Attributes
    private XYChart.Data<Number, Number>[] distanceSeriesData = null;
    // private XYChart.Data<Number, Number>[] velocitySeriesData = null;
    // private XYChart.Series<Number, Number> velocitySeries = new XYChart.Series<>();

    // Graphics Components
    @FXML private GridPane gridPaneGraphContainer;
    private LineChart<Number, Number> distanceChart = null;
    // private LineChart<Number, Number> graph_velocity = null;

    // Initialize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberAxis xAxis = new NumberAxis(0, 0.75, 0.75);
        xAxis.setTickLabelsVisible(false);
        NumberAxis yAxis = new NumberAxis(0, Y_AXIS_UPPER_BOUND, Y_AXIS_UPPER_BOUND);
        yAxis.setTickLabelsVisible(false);

        distanceChart = new LineChart<>(xAxis, yAxis);
        distanceChart.setCreateSymbols(false);
        distanceChart.setLegendVisible(false);
        distanceChart.setAnimated(false);

        XYChart.Series<Number, Number> distanceSeries = new XYChart.Series<>();
        //noinspection unchecked
        distanceSeriesData = new XYChart.Data[(int) xAxis.getUpperBound()];
        for (int i = 0; i < distanceSeriesData.length; i++) {
            distanceSeriesData[i] = new XYChart.Data<>(i, Y_NO_SIGNAL);
            distanceSeries.getData().add(distanceSeriesData[i]);
        }

        distanceChart.getData().add(distanceSeries);

        gridPaneGraphContainer.add(distanceChart, 0, 1);

        /*
        graph_distance.getYAxis().setAutoRanging(true);
        graph_distance.getData().addAll(distanceSeries);

        graph_velocity.getYAxis().setAutoRanging(true);
        graph_velocity.getData().addAll(velocitySeries);*/
    }

    // EDT
    public void updateDistanceChart(@NotNull IntArrayPile distancePile){
        // distanceSeries.getData().clear();
        int[] values = distancePile.getElements();
        for (int i = 0; i < values.length; i++){
            distanceSeriesData[i].setYValue(values[i]);
        }
    }
    public void updateVelocityChart(@NotNull DoubleArrayPile velocityPile){
        /*
        //sSystem.out.println("Update chart called\nArray elements: " + velocityPile);
        velocitySeries.getData().clear();
        double[] values = velocityPile.getElements();
        for (int i = 0; i < values.length; i++){
            velocitySeries.getData().add(new XYChart.Data<>(i, values[i]));
        }*/
    }
}
