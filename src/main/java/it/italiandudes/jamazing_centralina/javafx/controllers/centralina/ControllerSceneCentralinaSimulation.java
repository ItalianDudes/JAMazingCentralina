package it.italiandudes.jamazing_centralina.javafx.controllers.centralina;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

import java.net.URL;
import java.util.ResourceBundle;

public final class ControllerSceneCentralinaSimulation implements Initializable {
    @FXML private Button btn_onoff;
    @FXML private HBox hbox_signals;
    @FXML private SVGPath svgp_gasoline_signal;
    @FXML private ProgressBar pb_gasoline_percentage;
    @FXML private Label lbl_gasoline_percentage;
    @FXML private Label lbl_speed_counter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
