package it.italiandudes.jamazing_centralina.javafx.controllers;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.jamazing_centralina.javafx.Client;
import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.alerts.ErrorAlert;
import it.italiandudes.jamazing_centralina.javafx.alerts.InformationAlert;
import it.italiandudes.jamazing_centralina.javafx.scene.SceneMainMenu;
import it.italiandudes.jamazing_centralina.javafx.utils.Settings;
import it.italiandudes.jamazing_centralina.javafx.utils.ThemeHandler;
import it.italiandudes.jamazing_centralina.utils.Defs;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONException;

import java.io.IOException;

public final class ControllerSceneSettingsEditor {

    // Attributes
    private static final Image DARK_MODE = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_DARK_MODE));
    private static final Image LIGHT_MODE = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_LIGHT_MODE));

    // Graphic Elements
    @FXML private ImageView imageViewEnableDarkMode;
    @FXML private ToggleButton toggleButtonEnableDarkMode;

    // Initialize
    @FXML
    private void initialize() {
        toggleButtonEnableDarkMode.setSelected(Settings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DARK_MODE));
        if (toggleButtonEnableDarkMode.isSelected()) imageViewEnableDarkMode.setImage(DARK_MODE);
        else imageViewEnableDarkMode.setImage(LIGHT_MODE);
    }

    // EDT
    @FXML
    private void toggleEnableDarkMode() {
        if (toggleButtonEnableDarkMode.isSelected()) {
            imageViewEnableDarkMode.setImage(DARK_MODE);
            ThemeHandler.loadDarkTheme(Client.getStage().getScene());
        }
        else {
            imageViewEnableDarkMode.setImage(LIGHT_MODE);
            ThemeHandler.loadLightTheme(Client.getStage().getScene());
        }
    }
    @FXML
    private void backToMenu() {
        Client.setScene(SceneMainMenu.getScene());
    }
    @FXML
    private void save() {
        JFXDefs.startServiceTask(() -> {
            try {
                Settings.getSettings().put(Defs.SettingsKeys.ENABLE_DARK_MODE, toggleButtonEnableDarkMode.isSelected());
            } catch (JSONException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
            }
            ThemeHandler.setConfigTheme();
            try {
                Settings.writeJSONSettings();
                Platform.runLater(() -> new InformationAlert("SUCCESSO", "Salvataggio Impostazioni", "Impostazioni salvate e applicate con successo!"));
            } catch (IOException e) {
                Logger.log(e, Defs.LOGGER_CONTEXT);
                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante il salvataggio delle impostazioni."));
            }
        });
    }
}
