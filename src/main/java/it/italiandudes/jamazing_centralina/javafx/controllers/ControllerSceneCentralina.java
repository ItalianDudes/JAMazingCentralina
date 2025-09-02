package it.italiandudes.jamazing_centralina.javafx.controllers;

import it.italiandudes.jamazing_centralina.JAMazingCentralina;
import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.components.SceneController;
import it.italiandudes.jamazing_centralina.javafx.scene.centralina.SceneCentralinaGraphs;
import it.italiandudes.jamazing_centralina.javafx.scene.centralina.SceneCentralinaSimulation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.ToggleSwitch;

public final class ControllerSceneCentralina {

    // Attributes
    private SceneController sceneControllerCentralinaSimulation;
    private SceneController sceneControllerCentralinaGraph;

    // Graphic Elements
    @FXML private GridPane gridPaneSwitchContainer;
    @FXML private Label labelSimulation;
    @FXML private Label labelGraph;
    @FXML private GridPane gp_main_centralina_pane;
    @FXML private StackPane sp_placeholder_pane;
    private ToggleSwitch toggleSwitchChangeMode;

    // Initialize
    @FXML
    private void initialize() {
        sceneControllerCentralinaSimulation = SceneCentralinaSimulation.getScene();
        sceneControllerCentralinaGraph = SceneCentralinaGraphs.getScene();
        JFXDefs.startServiceTask(JAMazingCentralina::startSerialReader);

        toggleSwitchChangeMode = new ToggleSwitch();
        gp_main_centralina_pane.add(sceneControllerCentralinaSimulation.getParent(), 0, 0);
        toggleSwitchChangeMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sp_placeholder_pane.getChildren().setAll(sceneControllerCentralinaGraph.getParent());
            } else {
                sp_placeholder_pane.getChildren().setAll(sceneControllerCentralinaSimulation.getParent());
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
    public SceneController getSceneControllerCentralinaSimulation() {
        return sceneControllerCentralinaSimulation;
    }
    public SceneController getSceneControllerCentralinaGraph() {
        return sceneControllerCentralinaGraph;
    }

}
