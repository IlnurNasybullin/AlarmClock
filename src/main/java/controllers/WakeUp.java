package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URI;
import java.nio.file.Paths;

public class WakeUp {

    private String musicPath;
    private MediaPlayer mediaPlayer;

    @FXML
    private Button Off;

    public WakeUp(String musicPath) {
        this.musicPath = musicPath;
    }

    public void initialize() {
        Off.setOnAction(event -> stop());

    }

    public void start() {
        Stage stage = (Stage) (Off.getScene().getWindow());
        stage.setOnHidden(event -> stop());

        URI uri = Paths.get(musicPath).toUri();

        Media media = new Media(uri.toASCIIString());
        mediaPlayer = new MediaPlayer(media);

        play(60);
    }

    private void play(long minSeconds) {
        long tEnd = System.currentTimeMillis() + minSeconds * 1_000L;

        mediaPlayer.setOnEndOfMedia(() -> {
            long time = System.currentTimeMillis();

            if (time < tEnd) {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });

        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    private void close() {
        Stage stage = (Stage) (Off.getScene().getWindow());
        stage.close();
    }
}
