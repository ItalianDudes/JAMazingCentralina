package it.italiandudes.jamazing_centralina.javafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.ToggleSwitch;

public final class ControllerSceneCentralina {

    // Graphic Elements
    @FXML private GridPane gridPaneSwitchContainer;
    @FXML private Label labelSimulation;
    @FXML private Label labelGraph;
    private ToggleSwitch toggleSwitchChangeMode;

    // Initialize
    @FXML
    private void initialize() {
        toggleSwitchChangeMode = new ToggleSwitch();
        toggleSwitchChangeMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                labelSimulation.setStyle("");
                labelGraph.setStyle("-fx-font-weight: bold;");
            } else {
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
