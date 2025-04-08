package it.italiandudes.jamazing_centralina;

import it.italiandudes.jamazing_centralina.javafx.Client;
import it.italiandudes.jamazing_centralina.utils.Defs;
import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public final class JAMazingCentralina {

    // Main Method
    public static void main(String[] args) {

        // Setting Charset to UTF-8
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.setErr(new PrintStream(System.err, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.log("An error has occurred while setting charset to UTF-8.", Defs.LOGGER_CONTEXT);
        }

        // Initializing the logger
        try {
            Logger.init();
            Logger.log("Logger initialized!", Defs.LOGGER_CONTEXT);
        } catch (IOException e) {
            Logger.log("An error has occurred during Logger initialization, exit...", Defs.LOGGER_CONTEXT);
            return;
        }

        // Configure the shutdown hooks
        Logger.log("Configuring Shutdown Hooks...", Defs.LOGGER_CONTEXT);
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
        Logger.log("Shutdown Hooks configured!", Defs.LOGGER_CONTEXT);

        // Start the client
        try {
            Logger.log("Starting UI...", Defs.LOGGER_CONTEXT);
            Client.start(args);
        } catch (NoClassDefFoundError e) {
            Logger.log("ERROR: TO RUN THIS JAR YOU NEED JAVA 8 WITH BUILT-IN JAVAFX!", new InfoFlags(true, true, true, true), Defs.LOGGER_CONTEXT);
            Logger.log(e, Defs.LOGGER_CONTEXT);
            Logger.close();
            System.exit(-1);
        }
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
        Platform.runLater(() -> Client.getStage().hide());
        Logger.close();
        Platform.exit();
        System.exit(code);
    }
}
