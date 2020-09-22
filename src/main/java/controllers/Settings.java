package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Settings {

    private AlarmTask.AlarmTaskData data;

    private CheckBox[] weekDays;

    @FXML
    private CheckBox Monday;

    @FXML
    private CheckBox Tuesday;

    @FXML
    private CheckBox Wednesday;

    @FXML
    private CheckBox Thursday;

    @FXML
    private CheckBox Friday;

    @FXML
    private CheckBox Saturday;

    @FXML
    private CheckBox Sunday;

    @FXML
    private ComboBox<Integer> Hour;

    @FXML
    private ComboBox<Integer> Minute;

    @FXML
    private Label MusicName;

    @FXML
    private Button Choose;

    @FXML
    private Button Delete;

    @FXML
    private Button OK;

    private FileChooser fileChooser;

    private Consumer<AlarmTask.AlarmTaskData> removeConsumer;

    public Settings(AlarmTask.AlarmTaskData data, Consumer<AlarmTask.AlarmTaskData> removeConsumer) {
        this.data = data;
        this.removeConsumer = removeConsumer;
        this.fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
    }

    public void initialize() {
        this.weekDays = new CheckBox[]{Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday};

        initializeDaysOfWeek();
        initializeTime();
        initializeMelody();
        initializeButtons();
    }

    private void initializeButtons() {
        Choose.setOnAction(event -> chooseFile());
        Delete.setOnAction(event -> removeTask());
        OK.setOnAction(event -> {
            saveData();
            close();
        });
    }

    private void saveData() {
        data.setMusicPath(MusicName.getText());
        data.setHour(Hour.getValue());
        data.setMinute(Minute.getValue());

        EnumSet<DayOfWeek> weekSet = EnumSet.of(DayOfWeek.MONDAY);
        weekSet.remove(DayOfWeek.MONDAY);

        for (int i = 0; i < weekDays.length; i++) {
            if (weekDays[i].isSelected()) {
                weekSet.add(DayOfWeek.of(i + 1));
            }
        }

        data.setDayOfWeeks(weekSet);
    }

    private void removeTask() {
        removeConsumer.accept(data);
        close();
    }

    private void close() {
        Stage stage = (Stage)(Delete.getScene().getWindow());
        stage.close();
    }

    private void chooseFile() {
        File file = fileChooser.showOpenDialog(Choose.getScene().getWindow());
        if (file != null) {
            MusicName.setText(file.getAbsolutePath());
        }
    }

    private void initializeMelody() {
        String musicName = data.getMusicPath();
        MusicName.setText(musicName != null ? musicName : "");
    }

    private void initializeTime() {
        for (int i = 0; i < 24; i++) {
            Hour.getItems().add(i);
        }

        for (int i = 0; i < 60; i++) {
            Minute.getItems().add(i);
        }

        Hour.setValue(data.getHour());
        Minute.setValue(data.getMinute());
    }

    private void initializeDaysOfWeek() {
        EnumSet<DayOfWeek> dayOfWeeks = data.getDayOfWeeks();
        if (!Objects.isNull(dayOfWeeks)) {
            for (DayOfWeek dayOfWeek: dayOfWeeks) {
                this.weekDays[dayOfWeek.getValue() - 1].setSelected(true);
            }
        }
    }
}
