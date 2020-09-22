package main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.AlarmTask;
import controllers.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Platform.setImplicitExit(false);

        URL url = new File("./src/main/resources/controllers/MainMenu.fxml").toURI().toURL();

        FXMLLoader loader = new FXMLLoader();
        MainMenu mainMenu = new MainMenu(stage);

        loader.setController(mainMenu);
        loader.setLocation(url);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Будильник");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(10_000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
