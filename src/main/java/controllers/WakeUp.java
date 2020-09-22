package controllers;

import com.sun.jndi.toolkit.url.Uri;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
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
        System.out.println(uri);

        Media media = new Media(uri.toASCIIString());
        mediaPlayer = new MediaPlayer(media);
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
