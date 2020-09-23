package main;

import controllers.MainMenu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

public class Main extends Application {

    public static final String MAIN_MENU_FXML = "./src/main/resources/controllers/MainMenu.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Platform.setImplicitExit(false);

        URL url = new File(MAIN_MENU_FXML).toURI().toURL();

        FXMLLoader loader = new FXMLLoader();
        MainMenu mainMenu = new MainMenu(stage);

        loader.setController(mainMenu);
        loader.setLocation(url);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Будильник");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            while (true) {
                Thread.sleep(10_000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
