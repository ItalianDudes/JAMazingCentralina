package it.italiandudes.jamazing_centralina.javafx;

import it.italiandudes.jamazing_centralina.utils.Defs;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import org.jetbrains.annotations.NotNull;

public final class JFXDefs {

    // Service Starter
    public static void startServiceTask(@NotNull final Runnable runnable) {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        runnable.run();
                        return null;
                    }
                };
            }
        }.start();
    }

    // App Info
    public static final class AppInfo {
        public static final String NAME = "JAMazing Centralina";
        public static final Image LOGO = new Image(Resources.Image.APP_LOGO);
    }

    // System Info
    public static final class SystemGraphicInfo {
        public static final Rectangle2D SCREEN_RESOLUTION = Screen.getPrimary().getBounds();
        public static final double SCREEN_WIDTH = SCREEN_RESOLUTION.getWidth();
        public static final double SCREEN_HEIGHT = SCREEN_RESOLUTION.getHeight();
    }

    // Resource Locations
    public static final class Resources {

        // FXML Location
        public static final class FXML {
            private static final String FXML_DIR = Defs.Resources.PROJECT_RESOURCES_ROOT + "fxml/";
            public static final String FXML_LOADING = FXML_DIR + "SceneLoading.fxml";
            public static final String FXML_MAIN_MENU = FXML_DIR + "SceneMainMenu.fxml";
            public static final String FXML_SETTINGS_EDITOR = FXML_DIR + "SceneSettingsEditor.fxml";
            public static final String FXML_CENTRALINA = FXML_DIR + "SceneCentralina.fxml";
            public static final class Centralina {
                private static final String CENTRALINA_DIR = FXML_DIR + "centralina/";
                public static final String CENTRALINA_SIMULATION = CENTRALINA_DIR + "SceneCentralinaSimulation.fxml";
                public static final String CENTRALINA_GRAPHS = CENTRALINA_DIR + "SceneCentralinaGraphs.fxml";
            }
        }

        // GIF Location
        public static final class GIF {
            private static final String GIF_DIR = Defs.Resources.PROJECT_RESOURCES_ROOT + "gif/";
            public static final String GIF_LOADING = GIF_DIR+"loading.gif";
        }

        // CSS Location
        public static final class CSS {
            private static final String CSS_DIR = Defs.Resources.PROJECT_RESOURCES_ROOT + "css/";
            public static final String CSS_LIGHT_THEME = CSS_DIR + "light_theme.css";
            public static final String CSS_DARK_THEME = CSS_DIR + "dark_theme.css";
        }

        // Image Location
        public static final class Image {
            private static final String IMAGE_DIR = Defs.Resources.PROJECT_RESOURCES_ROOT + "image/";
            public static final String APP_LOGO = IMAGE_DIR + "app-logo.png";
            public static final String IMAGE_CAR_PARKED = IMAGE_DIR + "car-parked.png";
            public static final String IMAGE_ENGINE_TEMP_TOO_HIGH = IMAGE_DIR + "engine-coolant-temp-too-high.png";
            public static final String IMAGE_GAS_TANK_NORMAL = IMAGE_DIR + "gas-tank-normal.png";
            public static final String IMAGE_GAS_TANK_RESERVE = IMAGE_DIR + "gas-tank-reserve.png";
            public static final String IMAGE_TIRE_PRESSURE_LOW = IMAGE_DIR + "tire-pressure-low.png";
        }

    }

    public static final class SVGColor {
        public static final String NORMAL_COLOR = "#111111";
        public static final String WARNING_COLOR = "#FF0000";
    }

}
