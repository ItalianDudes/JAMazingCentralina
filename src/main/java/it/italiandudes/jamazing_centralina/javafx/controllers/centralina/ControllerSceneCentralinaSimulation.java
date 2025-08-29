package it.italiandudes.jamazing_centralina.javafx.controllers.centralina;

import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.utils.models.Simulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
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

    private Simulation centralinaSim;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        svgp_gasoline_signal.setContent(JFXDefs.Resources.SVGCode.FUEL_TANK);
    }

    public void setCentralinaSim(Simulation sim){
        this.centralinaSim = sim;
    }

    public Simulation getCentralinaSim(){
        return this.centralinaSim;
    }

    public Button getBtn_onoff() {
        return btn_onoff;
    }

    public HBox getHbox_signals() {
        return hbox_signals;
    }

    public SVGPath getSvgp_gasoline_signal() {
        return svgp_gasoline_signal;
    }

    public ProgressBar getPb_gasoline_percentage() {
        return pb_gasoline_percentage;
    }

    public Label getLbl_gasoline_percentage() {
        return lbl_gasoline_percentage;
    }

    public Label getLbl_speed_counter() {
        return lbl_speed_counter;
    }

    @FXML
    public void onoff_pressed(ActionEvent actionEvent) {
        centralinaSim.setEngine();
    }

    @FXML
    public void updateSimulation(){
        if (this.centralinaSim.isEngineOn()){
            this.pb_gasoline_percentage.setProgress(this.centralinaSim.getFuelPercentage());
            this.lbl_gasoline_percentage.setText((this.centralinaSim.getFuelPercentage() * 100.0) + " %");

            if (this.centralinaSim.isReserveOn()){
                this.svgp_gasoline_signal.setFill(Paint.valueOf(JFXDefs.SVGColor.RESERVE_COLOR));
            }else {
                this.svgp_gasoline_signal.setFill(Paint.valueOf(JFXDefs.SVGColor.NORMAL_COLOR));
            }

            lbl_speed_counter.setText(String.valueOf(centralinaSim.getVelocity()));
        }else{
            //TODO
        }
    }
}
