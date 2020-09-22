package controllers;

import alarmThread.AlarmThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainMenu {

    public static final String ALARM_ADD_FXML = "./src/main/resources/controllers/AlarmAdd.fxml";
    public static final String ALARM_TASK_FXML = "./src/main/resources/controllers/AlarmTask.fxml";

    public static final String ALARM_TASKS = "./src/main/resources/alarm_tasks.txt";

    @FXML
    private VBox AlarmList;

    @FXML
    private Button AddButton;

    private Stage stage;

    private Set<AlarmTask> alarmTaskSet;

    public MainMenu(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        initializeAlarmTasks();

        stage.setOnCloseRequest(event -> {
            saveData();
            close();
        });
        AddButton.setOnAction(event -> {
            try {
                addAlarm();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void close() {
        Stage stage = new Stage();
        stage.hide();
    }

    private void initializeAlarmTasks() {
        this.alarmTaskSet = readFromFile();
        for (AlarmTask alarmTask: alarmTaskSet) {
            addAlarmTask(alarmTask);
        }
    }

    private synchronized Set<AlarmTask> readFromFile() {
        Gson gson = getGson();
        try {
            String JSON = new String(Files.readAllBytes(Paths.get(ALARM_TASKS)));
            Set<AlarmTask.AlarmTaskData> data = gson.fromJson(JSON, new TypeToken<Set<AlarmTask.AlarmTaskData>>(){}.getType());
            return data.stream().map(AlarmTask::new).collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }

    private synchronized void saveData() {
        Gson gson = getGson();
        Set<AlarmTask.AlarmTaskData> data = alarmTaskSet.stream().map(AlarmTask::getAlarmTaskData).
                collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
        String JSON = gson.toJson(data);

        try {
            Files.write(Paths.get(ALARM_TASKS), JSON.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    private void remove(AlarmTask.RemoveData removeData) {
        alarmTaskSet.remove(removeData.getAlarmTask());
        AlarmList.getChildren().remove(removeData.getRoot());
    }

    private void addAlarmTask(AlarmTask alarmTask) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(alarmTask);
        try {
            loader.setLocation(new File(ALARM_TASK_FXML).toURI().toURL());
            Parent root = loader.load();
            alarmTask.setRoot(root);
            alarmTask.setSaveConsumer(this::saveTask);
            alarmTask.setRemoveConsumer(this::remove);
            AlarmList.getChildren().add(root);
            alarmTaskSet.add(alarmTask);
            runThread(alarmTask);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runThread(AlarmTask alarmTask) {
        Thread thread = new AlarmThread(alarmTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void saveTask(AlarmTask task) {
        saveData();
    }

    private void addAlarm() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        AlarmAdd alarmAdd = new AlarmAdd(this::addAlarmTask, alarmTaskSet.size() + 1);
        loader.setController(alarmAdd);
        loader.setLocation(new File(ALARM_ADD_FXML).toURI().toURL());

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initOwner(AlarmList.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
