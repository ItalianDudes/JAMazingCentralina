package it.italiandudes.jamazing_centralina.javafx.sfx;

import it.italiandudes.idl.common.InfoFlags;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.jamazing_centralina.utils.Defs;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

@SuppressWarnings("unused")
public final class SFXManager {

    // Constants
    private static final String WAV_DIR = Defs.Resources.PROJECT_RESOURCES_ROOT + "sfx/";
    private static final String FILE_EXT = ".wav";

    // Path Composer
    public static String composeSFXPath(String sfxName) {
        return Defs.Resources.get(WAV_DIR + sfxName + FILE_EXT).toExternalForm();
    }

    // Attributes
    private static MediaPlayer warningMediaPlayer = null;

    // Players
    private static void initWarningMediaPlayer() {
        if (warningMediaPlayer != null) return;
        warningMediaPlayer = new MediaPlayer(new Media(composeSFXPath("warning")));
        warningMediaPlayer.setOnEndOfMedia(() -> {
            warningMediaPlayer.seek(Duration.ZERO);
            warningMediaPlayer.play();
        });
    }
    public static void switchWarningLoopSFX() {
        try {
            initWarningMediaPlayer();
            if (warningMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                warningMediaPlayer.pause();
            } else {
                warningMediaPlayer.seek(Duration.ZERO);
                warningMediaPlayer.play();
            }
        } catch (Exception e) {
            Logger.log("SFX Warning Loop Error", new InfoFlags(true, false, false , true));
            Logger.log(e);
        }
    }
    public static void stopWarningLoopSFX() {
        try {
            initWarningMediaPlayer();
            if (warningMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                warningMediaPlayer.pause();
            }
        } catch (Exception e) {
            Logger.log("SFX Warning Loop Error", new InfoFlags(true, false, false , true));
            Logger.log(e);
        }
    }
    public static void playWarningLoopSFX() {
        try {
            initWarningMediaPlayer();
            if (warningMediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                warningMediaPlayer.seek(Duration.ZERO);
                warningMediaPlayer.play();
            }
        } catch (Exception e) {
            Logger.log("SFX Warning Loop Error", new InfoFlags(true, false, false , true));
            Logger.log(e);
        }
    }
    public static void playWarningSFX() {
        try {
            new MediaPlayer(new Media(composeSFXPath("warning"))).play();
        } catch (Exception e) {
            Logger.log("SFX Warning Play Error", new InfoFlags(true, false, false , true));
            Logger.log(e);
        }
    }
    public static void playEngineStartupSFX() {
        try {
            new MediaPlayer(new Media(composeSFXPath("engine_startup"))).play();
        } catch (Exception e) {
            Logger.log("SFX Engine Startup Play Error", new InfoFlags(true, false, false , true));
            Logger.log(e);
        }
    }
}
