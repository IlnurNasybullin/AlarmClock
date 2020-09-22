package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class AlarmAdd {

    private Consumer<AlarmTask> addAlarmTask;
    private int index;

    public AlarmAdd(Consumer<AlarmTask> addAlarmTask, int index) {
        this.addAlarmTask = addAlarmTask;
        this.index = index;
    }

    @FXML
    private ComboBox<Integer> Hour;

    @FXML
    private ComboBox<Integer> Minute;

    @FXML
    private Button OK;

    @FXML
    private Button Cancel;

    @FXML
    private TextField TaskName;

    public void initialize() {
        initializeHours();
        initializeMinutes();
        initializeButtons();
        initializeTaskName();
    }

    private void initializeTaskName() {
        TaskName.setPromptText("Будильник №" + index);
    }

    private void initializeButtons() {
        Cancel.setOnAction(actionEvent -> close());
        OK.setOnAction(actionEvent -> addTask());
    }

    private void initializeMinutes() {
        for (int i = 0; i < 60; i++) {
            Minute.getItems().add(i);
        }

        Minute.setValue(0);
    }

    private void initializeHours() {
        for (int i = 0; i < 24; i++) {
            Hour.getItems().add(i);
        }

        Hour.setValue(0);
    }

    @FXML
    private void addTask() {
        addAlarmTask.accept(new AlarmTask(Hour.getValue(), Minute.getValue(), TaskName.getText()));
        close();
    }

    private void close() {
        Stage stage = (Stage)(Hour.getScene().getWindow());
        stage.close();
    }
}
