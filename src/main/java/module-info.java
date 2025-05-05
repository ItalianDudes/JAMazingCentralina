module JAMazingCentralina.main {
    requires it.italiandudes.idl;
    requires java.desktop;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires org.jetbrains.annotations;
    requires org.json;
    requires org.controlsfx.controls;

    opens com.sun.javafx.application;

    exports it.italiandudes.jamazing_centralina;
    exports it.italiandudes.jamazing_centralina.utils;
    exports it.italiandudes.jamazing_centralina.javafx;
    exports it.italiandudes.jamazing_centralina.javafx.alerts;
    exports it.italiandudes.jamazing_centralina.javafx.components;
    exports it.italiandudes.jamazing_centralina.javafx.controllers;
    exports it.italiandudes.jamazing_centralina.javafx.controllers.centralina;
    exports it.italiandudes.jamazing_centralina.javafx.scene;
    exports it.italiandudes.jamazing_centralina.javafx.scene.centralina;
    exports it.italiandudes.jamazing_centralina.javafx.utils;
}