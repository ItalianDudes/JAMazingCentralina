package it.italiandudes.jamazing_centralina.javafx.controllers.centralina;

import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.sfx.SFXManager;
import it.italiandudes.jamazing_centralina.utils.models.Simulation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public final class ControllerSceneCentralinaSimulation implements Initializable {

    private final DecimalFormat velocityFormatter = new DecimalFormat("#.##");

    @FXML private Button btn_onoff;
    @FXML private ProgressBar pb_gasoline_percentage;
    @FXML private Label lbl_gasoline_percentage;
    @FXML private Label lbl_speed_counter;
    @FXML private Label lbl_proximity_warning;
    @FXML private Label lbl_slope_assistance_warning;

    @FXML private ImageView imgv_parked;
    @FXML  private ImageView imgv_engine_temp;
    @FXML private ImageView imgv_gas_tank;
    @FXML private ImageView imgv_tire_low;

    private Simulation centralinaSim;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imgv_parked.setImage(new Image(JFXDefs.Resources.Image.IMAGE_CAR_PARKED));
        imgv_parked.setVisible(false);
        imgv_engine_temp.setImage(new Image(JFXDefs.Resources.Image.IMAGE_ENGINE_TEMP_TOO_HIGH));
        imgv_engine_temp.setVisible(false);
        imgv_gas_tank.setImage(new Image(JFXDefs.Resources.Image.IMAGE_GAS_TANK_NORMAL));
        imgv_gas_tank.setVisible(true);
        imgv_tire_low.setImage(new Image(JFXDefs.Resources.Image.IMAGE_TIRE_PRESSURE_LOW));
        imgv_tire_low.setVisible(false);
        lbl_proximity_warning.setText("Attenzione: ti stai avvicinando troppo ad una parete!");
        lbl_proximity_warning.setTextFill(Paint.valueOf(JFXDefs.SVGColor.WARNING_COLOR));
        lbl_proximity_warning.setVisible(false);
        lbl_slope_assistance_warning.setText("Aiuto guida attivo");
        lbl_proximity_warning.setTextFill(Paint.valueOf(JFXDefs.SVGColor.NORMAL_COLOR));
        lbl_proximity_warning.setVisible(false);

        imgv_gas_tank.imageProperty().addListener(observableValue -> {
            if (centralinaSim.isReserveOn()) SFXManager.playWarningSFX(); // Single Warning
        });

        btn_onoff.setText("AVVIA\nSPEGNI");
    }

    public void setCentralinaSim(Simulation sim){
        this.centralinaSim = sim;
    }

    @FXML
    public void onoff_pressed() {
        centralinaSim.setEngine();
        if (centralinaSim.isEngineOn()) SFXManager.playEngineStartupSFX();
        else SFXManager.stopWarningLoopSFX();
    }

    @FXML
    public void updateSimulation(){
        if (this.centralinaSim.isEngineOn()){
            this.pb_gasoline_percentage.setProgress(this.centralinaSim.getFuelPercentage());
            this.lbl_gasoline_percentage.setText(velocityFormatter.format((this.centralinaSim.getFuelPercentage() * 100.0)) + " %");

            if (this.centralinaSim.isReserveOn()){
                this.imgv_gas_tank.setImage(new Image(JFXDefs.Resources.Image.IMAGE_GAS_TANK_RESERVE));
            }else {
                this.imgv_gas_tank.setImage(new Image(JFXDefs.Resources.Image.IMAGE_GAS_TANK_NORMAL));
            }

            this.imgv_parked.setVisible(centralinaSim.isParked());
            this.imgv_engine_temp.setVisible(centralinaSim.isEngineTempTooHigh());
            this.imgv_tire_low.setVisible(centralinaSim.isTireLow());
            this.lbl_proximity_warning.setVisible(centralinaSim.isProximity());
            this.lbl_slope_assistance_warning.setVisible(centralinaSim.isTerrainSlope());

            if (centralinaSim.isProximity()) SFXManager.playWarningLoopSFX(); // Play Warning Loop
            else SFXManager.stopWarningLoopSFX(); // Stop Warning Loop

            this.lbl_speed_counter.setText(String.valueOf(centralinaSim.getVelocity()));
        }else{
            this.pb_gasoline_percentage.setProgress(0.0);
            this.lbl_gasoline_percentage.setText("");

            this.imgv_gas_tank.setImage(new Image(JFXDefs.Resources.Image.IMAGE_GAS_TANK_NORMAL));

            this.imgv_parked.setVisible(false);
            this.imgv_engine_temp.setVisible(false);
            this.imgv_tire_low.setVisible(false);
            this.lbl_proximity_warning.setVisible(false);
            this.lbl_slope_assistance_warning.setVisible(false);

            this.lbl_speed_counter.setText("");
        }
    }
}
