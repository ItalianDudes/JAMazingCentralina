package it.italiandudes.jamazing_centralina.javafx.scene.centralina;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.jamazing_centralina.javafx.Client;
import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.components.SceneController;
import it.italiandudes.jamazing_centralina.javafx.controllers.centralina.ControllerSceneCentralinaSimulation;
import it.italiandudes.jamazing_centralina.utils.Defs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public final class SceneCentralinaSimulation {

    // Scene Generator
    @NotNull
    public static SceneController getScene() {
        return Objects.requireNonNull(genScene());
    }
    @Nullable
    private static SceneController genScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.Centralina.CENTRALINA_SIMULATION));
            Parent root = loader.load();
            ControllerSceneCentralinaSimulation controller = loader.getController();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Client.exit(-1);
            return null;
        }
    }
}
