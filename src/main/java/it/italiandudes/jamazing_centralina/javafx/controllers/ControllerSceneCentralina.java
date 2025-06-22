package it.italiandudes.jamazing_centralina.javafx.controllers;

import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.scene.centralina.SceneCentralinaGraphs;
import it.italiandudes.jamazing_centralina.javafx.scene.centralina.SceneCentralinaSimulation;
import it.italiandudes.jamazing_centralina.utils.Defs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.ToggleSwitch;

public final class ControllerSceneCentralina {

    // Graphic Elements
    @FXML private GridPane gridPaneSwitchContainer;
    @FXML private Label labelSimulation;
    @FXML private Label labelGraph;
    @FXML private GridPane gp_main_centralina_pane;
    private ToggleSwitch toggleSwitchChangeMode;

    // Initialize
    @FXML
    private void initialize() {
        gp_main_centralina_pane.add(SceneCentralinaSimulation.getScene().getParent(),0,0);
        toggleSwitchChangeMode = new ToggleSwitch();
        toggleSwitchChangeMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                gp_main_centralina_pane.add(SceneCentralinaGraphs.getScene().getParent(),0,0);
                labelSimulation.setStyle("");
                labelGraph.setStyle("-fx-font-weight: bold;");
            } else {
                gp_main_centralina_pane.add(SceneCentralinaSimulation.getScene().getParent(),0,0);
                labelSimulation.setStyle("-fx-font-weight: bold;");
                labelGraph.setStyle("");
            }
        });
        toggleSwitchChangeMode.setMinWidth(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setMinHeight(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setPrefWidth(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setPrefHeight(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setMaxWidth(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setMaxHeight(ToggleSwitch.USE_COMPUTED_SIZE);
        gridPaneSwitchContainer.add(toggleSwitchChangeMode, 1, 0);
    }

    // EDT

}
