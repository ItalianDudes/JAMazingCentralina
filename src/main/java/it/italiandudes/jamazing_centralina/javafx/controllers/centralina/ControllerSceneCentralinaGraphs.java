package it.italiandudes.jamazing_centralina.javafx.controllers.centralina;

import it.italiandudes.idl.common.Logger;
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

    // Graphics Components
    @FXML private GridPane gridPaneGraphContainer;
    private LineChart<Number, Number> distanceChart = null;
    private LineChart<Number, Number> velocityChart = null;
    private LineChart<Number, Number> temperatureChart = null;
    private LineChart<Number, Number> humidityChart = null;
    private LineChart<Number, Number> pressureChart = null;

    // Initialize
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        NumberAxis xDistanceAxis = new NumberAxis();
        xDistanceAxis.setLabel("Tempo [s]");
        xDistanceAxis.setTickUnit(100);
        xDistanceAxis.setForceZeroInRange(false);
        NumberAxis yDistanceAxis = new NumberAxis();
        yDistanceAxis.setLabel("Distanza [mm]");
        distanceChart = new LineChart<>(xDistanceAxis, yDistanceAxis);
        distanceChart.setTitle("Prossimità ad oggetti");
        distanceChart.setAnimated(false);
        distanceChart.getData().clear();

        NumberAxis xVelocityAxis = new NumberAxis();
        xVelocityAxis.setLabel("Tempo [s]");
        xVelocityAxis.setTickUnit(100);
        xVelocityAxis.setForceZeroInRange(false);
        NumberAxis yVelocityAxis = new NumberAxis();
        yVelocityAxis.setLabel("Velocità [mm/s]");
        velocityChart = new LineChart<>(xVelocityAxis, yVelocityAxis);
        velocityChart.setTitle("Velocità nel Tempo");
        velocityChart.setAnimated(false);
        velocityChart.getData().clear();

        NumberAxis xTemperatureAxis = new NumberAxis();
        xTemperatureAxis.setLabel("Tempo [s]");
        xTemperatureAxis.setTickUnit(100);
        xTemperatureAxis.setForceZeroInRange(false);
        NumberAxis yTemperatureAxis = new NumberAxis();
        yTemperatureAxis.setLabel("Temperatura [°C]");
        temperatureChart = new LineChart<>(xTemperatureAxis, yTemperatureAxis);
        temperatureChart.setTitle("Temperatura nel Tempo");
        temperatureChart.setAnimated(false);
        temperatureChart.getData().clear();

        NumberAxis xHumidityAxis = new NumberAxis();
        xHumidityAxis.setLabel("Tempo [s]");
        xHumidityAxis.setTickUnit(100);
        xHumidityAxis.setForceZeroInRange(false);
        NumberAxis yHumidityAxis = new NumberAxis();
        yHumidityAxis.setLabel("%Humidity");
        humidityChart = new LineChart<>(xHumidityAxis, yHumidityAxis);
        humidityChart.setTitle("% Umidità nel Tempo");
        humidityChart.setAnimated(false);
        humidityChart.getData().clear();

        NumberAxis xPressureAxis = new NumberAxis();
        xPressureAxis.setLabel("Tempo [s]");
        xPressureAxis.setTickUnit(100);
        xPressureAxis.setForceZeroInRange(false);
        NumberAxis yPressureAxis = new NumberAxis();
        yPressureAxis.setLabel("Pressione [hPa]");
        pressureChart = new LineChart<>(xPressureAxis, yPressureAxis);
        pressureChart.setTitle("Pressione nel Tempo");
        pressureChart.setAnimated(false);
        pressureChart.getData().clear();

        gridPaneGraphContainer.add(distanceChart, 0, 0);
        gridPaneGraphContainer.add(velocityChart, 0, 1);
        gridPaneGraphContainer.add(temperatureChart, 1, 0);
        gridPaneGraphContainer.add(humidityChart, 1, 1);
        gridPaneGraphContainer.add(pressureChart, 2, 0);
    }

    // EDT
    @FXML
    public void updateDistanceChart(@NotNull IntArrayPile distancePile, @NotNull DoubleArrayPile timePile){
        if (this.distanceChart == null) return;
        this.distanceChart.getData().clear();
        int[] distanceValues = distancePile.getElements();
        double[] timeValues = timePile.getElements();
        if (distanceValues.length != timeValues.length) {
            System.err.println("DISTANCE LENGTH: " + distanceValues.length);
            System.err.println("TIME LENGTH: " + timeValues.length);
        }
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i=0; i<timeValues.length; i++) {
            try {
                series.getData().add(new XYChart.Data<>(timeValues[i], distanceValues[i]));
            } catch (IndexOutOfBoundsException e) {
                Logger.log(e);
            }
        }
        this.distanceChart.getData().add(series);
    }

    @FXML
    public void updateVelocityChart(@NotNull DoubleArrayPile velocityPile, @NotNull DoubleArrayPile timePile){
        if (this.velocityChart == null) return;
        this.velocityChart.getData().clear();
        double[] values = velocityPile.getElements();
        double[] timeValues = timePile.getElements();
        if (values.length != timeValues.length) {
            System.err.println("DISTANCE LENGTH: " + values.length);
            System.err.println("TIME LENGTH: " + timeValues.length);
        }
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i=0; i<values.length; i++) {
            try {
                double value = values[i];
                if (value == Double.NEGATIVE_INFINITY || value == Double.POSITIVE_INFINITY) value = 0;
                value = (double) Math.round(value * 100) /100;
                series.getData().add(new XYChart.Data<>(timeValues[i], value));
            } catch (IndexOutOfBoundsException e) {
                Logger.log(e);
            }
        }
        this.velocityChart.getData().add(series);
    }

    @FXML
    public void updateTemperatureChart(@NotNull IntArrayPile temperaturePile, @NotNull DoubleArrayPile timePile){
        if (this.temperatureChart == null) return;
        this.temperatureChart.getData().clear();
        int[] values = temperaturePile.getElements();
        double[] timeValues = timePile.getElements();
        if (values.length != timeValues.length) {
            System.err.println("DISTANCE LENGTH: " + values.length);
            System.err.println("TIME LENGTH: " + timeValues.length);
        }
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i=0; i<values.length; i++) {
            try {
                series.getData().add(new XYChart.Data<>(timeValues[i], values[i]));
            } catch (IndexOutOfBoundsException e) {
                Logger.log(e);
            }
        }
        this.temperatureChart.getData().add(series);
    }

    @FXML
    public void updateHumidityChart(@NotNull IntArrayPile humidityPile, @NotNull DoubleArrayPile timePile){
        if (this.humidityChart == null) return;
        this.humidityChart.getData().clear();
        int[] values = humidityPile.getElements();
        double[] timeValues = timePile.getElements();
        if (values.length != timeValues.length) {
            System.err.println("DISTANCE LENGTH: " + values.length);
            System.err.println("TIME LENGTH: " + timeValues.length);
        }
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i=0; i<values.length; i++) {
            try {
                series.getData().add(new XYChart.Data<>(timeValues[i], values[i]));
            } catch (IndexOutOfBoundsException e) {
                Logger.log(e);
            }
        }
        this.humidityChart.getData().add(series);
    }

    @FXML
    public void updatePressureChart(@NotNull IntArrayPile pressurePile, @NotNull DoubleArrayPile timePile){
        if (this.pressureChart == null) return;
        this.pressureChart.getData().clear();
        int[] values = pressurePile.getElements();
        double[] timeValues = timePile.getElements();
        if (values.length != timeValues.length) {
            System.err.println("DISTANCE LENGTH: " + values.length);
            System.err.println("TIME LENGTH: " + timeValues.length);
        }
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i=0; i<values.length; i++) {
            try {
                series.getData().add(new XYChart.Data<>(timeValues[i], values[i]));
            } catch (IndexOutOfBoundsException e) {
                Logger.log(e);
            }
        }
        this.pressureChart.getData().add(series);
    }
}
