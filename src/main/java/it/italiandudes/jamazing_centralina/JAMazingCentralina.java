package it.italiandudes.jamazing_centralina;

import com.fazecast.jSerialComm.SerialPort;
import it.italiandudes.idl.common.JarHandler;
import it.italiandudes.idl.common.TargetPlatform;
import it.italiandudes.jamazing_centralina.javafx.Client;
import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.controllers.ControllerSceneCentralina;
import it.italiandudes.jamazing_centralina.javafx.controllers.centralina.ControllerSceneCentralinaGraphs;
import it.italiandudes.jamazing_centralina.javafx.controllers.centralina.ControllerSceneCentralinaSimulation;
import it.italiandudes.jamazing_centralina.utils.Defs;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.jamazing_centralina.utils.models.LoadedDataHandler;
import it.italiandudes.jamazing_centralina.utils.models.ParsedSerialData;
import javafx.application.Platform;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.jar.Attributes;

public final class JAMazingCentralina {
    private static boolean threadStop = false;
    private static boolean isSerialOpen = false;

    // Main Method
    public static void main(String[] args) {

        // Setting Charset to UTF-8
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        // Initializing the logger
        try {
            Logger.init();
            Logger.log("Logger initialized!");
        } catch (IOException e) {
            Logger.log("An error has occurred during Logger initialization, exit...");
            return;
        }

        // Configure the shutdown hooks
        Logger.log("Configuring Shutdown Hooks...");
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
        Logger.log("Shutdown Hooks configured!");

        // Check Java Version
        Logger.log("Verifying for Java 21...");
        if (!SystemUtils.IS_JAVA_21) {
            Logger.log("The current java version is wrong, to run this jar you need Java 21!", new InfoFlags(true, true, true, true));
            Logger.close();
            System.exit(-1);
            return;
        }
        Logger.log("Java 21 Verified!");

        // Check OS
        Logger.log("Verifying OS...");
        Logger.log("OS Name: " + SystemUtils.OS_NAME);
        Logger.log("OS Arch: " + SystemUtils.OS_ARCH);
        Logger.log("Current OS Platform: " + (Defs.CURRENT_PLATFORM != null ? Defs.CURRENT_PLATFORM.getName() : "NOT RECOGNIZED"));
        if (Defs.CURRENT_PLATFORM == null) {
            Logger.log("WARNING: Current OS Platform not recognized! An attempt to start the app will be done anyway.", new InfoFlags(true, false, false, true));
        }
        try {
            Attributes manifestAttributes = JarHandler.ManifestReader.readJarManifest(Defs.JAR_POSITION);
            @Nullable String manifestTargetPlatform = JarHandler.ManifestReader.getValue(manifestAttributes, "Target-Platform");
            if (manifestTargetPlatform == null) {
                Logger.log("Target-Platform not specified in jar manifest, this jar shouldn't be used for release.", new InfoFlags(false, false, false, true));
            } else {
                @Nullable TargetPlatform targetPlatform = TargetPlatform.fromManifestTargetPlatform(manifestTargetPlatform);
                if (targetPlatform == null) {
                    Logger.log("Target-Platform provided \"" + manifestTargetPlatform + "\" not recognized, this jar shouldn't be used for release.", new InfoFlags(false, false, false, true));
                } else {
                    Logger.log("Jar Target-Platform: " + targetPlatform.getName());
                    if (Defs.CURRENT_PLATFORM != null && !targetPlatform.isCurrentOS()) {
                        Logger.log("Target-Platform \"" + targetPlatform.getName() + "\" incompatible with the current OS Platform!", new InfoFlags(true, true, true, true));
                        Logger.close();
                        System.exit(-1);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            Logger.log("An error has occurred while attempting to read jar manifest!", new InfoFlags(true, true, true, true));
            Logger.close();
            System.exit(-1);
            return;
        }

        // Start the client
        try {
            Logger.log("Starting UI...");
            Client.start(args);
        } catch (NoClassDefFoundError e) {
            Logger.log("ERROR: JAVAFX NOT FOUND!", new InfoFlags(true, true, true, true));
            Logger.log(e);
            Logger.close();
            System.exit(-1);
        }

        Logger.log("Starting serial thread...");
        try{
            startSerialReader();
            Logger.log("Serial thread started successfully");
        } catch (RuntimeException e){
            Logger.log(e, new InfoFlags(true, true, true, true));
            Logger.close();
            System.exit(-1);
        }
    }

    public static void startSerialReader() {
        if (isSerialOpen) return;
        isSerialOpen = true;
        SerialPort comPort = SerialPort.getCommPort("COM6"); // cambia porta se necessario (es. /dev/ttyUSB0 su Linux)
        comPort.setBaudRate(9600);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        if (!comPort.openPort()) {
            System.out.println();
            throw new RuntimeException("Unable to open the serial port. Selected port:\n" + comPort);
        }

        Thread serialThread = new Thread(() -> {
            try (InputStream in = comPort.getInputStream()) {
                Object controller = Client.getScene().getController();
                if (!(controller instanceof ControllerSceneCentralina controllerSceneCentralina)) return;
                ControllerSceneCentralinaSimulation simController = (ControllerSceneCentralinaSimulation) controllerSceneCentralina.getSceneControllerCentralinaSimulation().getController();
                ControllerSceneCentralinaGraphs graphsController = (ControllerSceneCentralinaGraphs) controllerSceneCentralina.getSceneControllerCentralinaGraph().getController();
                ParsedSerialData parsedSerialData = new ParsedSerialData();
                LoadedDataHandler loadedDataHandler = new LoadedDataHandler();
                StringBuilder buffer = new StringBuilder();
                boolean firstline = true;
                int data;
                while (!threadStop && (data = in.read()) != -1) {
                    try{
                        char ch = (char) data;
                        if (ch == '\n') {
                            String line = buffer.toString().trim();
                            buffer.setLength(0);
                            System.out.println(line);
                            parsedSerialData.parseData(line);

                            if(firstline){
                                loadedDataHandler.initData(parsedSerialData, 50, 50,
                                        50, 50, 50,
                                        50, 0.98);
                                firstline = false;
                            }else{
                                loadedDataHandler.updateData(parsedSerialData);
                            }

                            Platform.runLater(() -> {
                                graphsController.updateDistanceChart(loadedDataHandler.getDistanceDataBatch());
                                graphsController.updateVelocityChart(loadedDataHandler.getVelocityDataBatch());
                            });

                            //Platform.runLater(() -> outputArea.appendText(line + "\n"));
                        } else {
                            buffer.append(ch);
                        }

                    }catch (Exception e){
                        Logger.log("Discarded serial string because of unreadable data caused by an error in the communication", new InfoFlags(true, true, false,true));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                comPort.closePort();
            }
        });

        serialThread.setDaemon(true);
        JFXDefs.startServiceTask(serialThread);
    }

    // Exit Methods
    public static void exit() {
        exit(0);
    }
    public static void exit(final int code) {
        if (code != 0) {
            Logger.log("Exit Method Called with non-zero code " + code + ", this probably means an error has occurred.", Defs.LOGGER_CONTEXT);
        } else {
            Logger.log("Exit Method Called, exiting JAMazingCentralina...", Defs.LOGGER_CONTEXT);
        }
        threadStop = true;
        Platform.runLater(() -> Client.getStage().hide());
        Logger.close();
        Platform.exit();
        System.exit(code);
    }
}
