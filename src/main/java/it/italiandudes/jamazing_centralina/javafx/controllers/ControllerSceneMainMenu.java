package it.italiandudes.jamazing_centralina.javafx.controllers;

import com.sun.javafx.application.HostServicesDelegate;
import it.italiandudes.jamazing_centralina.javafx.Client;
import it.italiandudes.jamazing_centralina.javafx.JFXDefs;
import it.italiandudes.jamazing_centralina.javafx.alerts.ErrorAlert;
import it.italiandudes.jamazing_centralina.javafx.alerts.YesNoAlert;
import it.italiandudes.jamazing_centralina.javafx.scene.SceneSettingsEditor;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.jamazing_centralina.utils.Defs;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;

public final class ControllerSceneMainMenu {

    // Graphic Elements
    @FXML private ImageView imageViewLogo;

    // Initialize
    @FXML
    private void initialize() {
        imageViewLogo.setImage(JFXDefs.AppInfo.LOGO);
    }

    // EDT
    @FXML
    private void showReportBanner() {
        ClipboardContent link = new ClipboardContent();
        String url = Defs.REPO_URL + "/issues";
        link.putString(url);
        Client.getSystemClipboard().setContent(link);
        boolean result = new YesNoAlert("INFO", "Grazie!", "ItalianDudes e' sempre felice di ricevere segnalazioni da parte degli utenti circa le sue applicazioni.\nE' stato aggiunto alla tua clipboard di sistema il link per accedere alla pagina github per aggiungere il tuo report riguardante problemi o idee varie.\nPremi \"Si\" per aprire direttamente il link nel browser predefinito.\nGrazie ancora!").result;
        try {
            if (result && Client.getApplicationInstance() != null) HostServicesDelegate.getInstance(Client.getApplicationInstance()).showDocument(url);
        } catch (Exception e) {
            Logger.log(e);
            new ErrorAlert("ERRORE", "Errore Interno", "Si e' verificato un errore durante l'apertura del browser predefinito.\nIl link alla pagina e' comunque disponibile negli appunti di sistema.");
        }
    }
    @FXML
    private void openSettingsEditor() {
        Client.setScene(SceneSettingsEditor.getScene());
    }
}
