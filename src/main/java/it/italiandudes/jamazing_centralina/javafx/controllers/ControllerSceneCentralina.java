package it.italiandudes.jamazing_centralina.javafx.controllers;

import it.italiandudes.jamazing_centralina.JAMazingCentralina;
import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.components.SceneController;
import it.italiandudes.jamazing_centralina.javafx.scene.centralina.SceneCentralinaGraphs;
import it.italiandudes.jamazing_centralina.javafx.scene.centralina.SceneCentralinaSimulation;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.ToggleSwitch;

public final class ControllerSceneCentralina {

    // Attributes
    private SceneController sceneControllerCentralinaSimulation;
    private SceneController sceneControllerCentralinaGraph;

    // Graphic Elements
    @FXML private GridPane gridPaneSwitchContainer;
    @FXML private AnchorPane ap_placeholder_pane;

    // Initialize
    @FXML
    private void initialize() {
        sceneControllerCentralinaSimulation = SceneCentralinaSimulation.getScene();
        sceneControllerCentralinaGraph = SceneCentralinaGraphs.getScene();
        JFXDefs.startServiceTask(JAMazingCentralina::startSerialReader);

        ToggleSwitch toggleSwitchChangeMode = new ToggleSwitch();
        toggleSwitchChangeMode.setMinWidth(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setMinHeight(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setPrefWidth(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setPrefHeight(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setMaxWidth(ToggleSwitch.USE_COMPUTED_SIZE);
        toggleSwitchChangeMode.setMaxHeight(ToggleSwitch.USE_COMPUTED_SIZE);

        ap_placeholder_pane.getChildren().setAll(sceneControllerCentralinaSimulation.getParent());
        AnchorPane.setTopAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);
        AnchorPane.setLeftAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);
        AnchorPane.setBottomAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);
        AnchorPane.setRightAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);

        toggleSwitchChangeMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                ap_placeholder_pane.getChildren().setAll(sceneControllerCentralinaGraph.getParent());
                AnchorPane.setTopAnchor(sceneControllerCentralinaGraph.getParent(), 0.0);
                AnchorPane.setLeftAnchor(sceneControllerCentralinaGraph.getParent(), 0.0);
                AnchorPane.setBottomAnchor(sceneControllerCentralinaGraph.getParent(), 0.0);
                AnchorPane.setRightAnchor(sceneControllerCentralinaGraph.getParent(), 0.0);
            } else {
                ap_placeholder_pane.getChildren().setAll(sceneControllerCentralinaSimulation.getParent());
                AnchorPane.setTopAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);
                AnchorPane.setLeftAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);
                AnchorPane.setBottomAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);
                AnchorPane.setRightAnchor(sceneControllerCentralinaSimulation.getParent(), 0.0);
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
