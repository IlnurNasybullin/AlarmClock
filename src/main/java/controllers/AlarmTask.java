package controllers;

import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Consumer;

public class AlarmTask {

    private final static String SETTINGS_URL = "./src/main/resources/controllers/Settings.fxml";
    private final static String WAKE_UP = "./src/main/resources/controllers/WakeUp.fxml";

    private Parent root;
    private Consumer<RemoveData> removeConsumer;
    private Consumer<AlarmTask> saveConsumer;

    public static class RemoveData {

        private AlarmTask alarmTask;

        private Parent root;

        public RemoveData(AlarmTask alarmTask, Parent root) {
            this.alarmTask = alarmTask;
            this.root = root;
        }

        public AlarmTask getAlarmTask() {
            return alarmTask;
        }

        public Parent getRoot() {
            return root;
        }

    }
    public static class AlarmTaskData {

        private int hour;
        private int minute;
        private EnumSet<DayOfWeek> dayOfWeeks;
        private String musicPath;
        private boolean isActive;
        private boolean isAlive;

        private String taskName;

        public AlarmTaskData(int hour, int minute, String taskName) {
            this.hour = hour;
            this.minute = minute;
            this.taskName = taskName;
            this.isActive = true;
            this.isAlive = true;
        }

        public int getHour() {
            return hour;
        }
        public int getMinute() {
            return minute;
        }
        public EnumSet<DayOfWeek> getDayOfWeeks() {
            return dayOfWeeks;
        }

        public String getMusicPath() {
            return musicPath;
        }
        public void setDayOfWeeks(EnumSet<DayOfWeek> dayOfWeeks) {
            this.dayOfWeeks = dayOfWeeks;
        }

        public void setMusicPath(String musicPath) {
            this.musicPath = musicPath;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public String getStringHour() {
            return hour < 10 ? "0" + hour : Integer.toString(hour);
        }

        public String getStringMinute() {
            return minute < 10 ? "0" + minute : Integer.toString(minute);
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public boolean isAlive() {
            return isAlive;
        }

        public void setAlive(boolean alive) {
            isAlive = alive;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AlarmTaskData that = (AlarmTaskData) o;
            return hour == that.hour &&
                    minute == that.minute &&
                    Objects.equals(dayOfWeeks, that.dayOfWeeks) &&
                    Objects.equals(musicPath, that.musicPath) &&
                    Objects.equals(taskName, that.taskName);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(hour, minute, musicPath, dayOfWeeks, taskName);
            return result;
        }

    }
    private AlarmTaskData alarmTaskData;

    @FXML
    private Label HourLabel;

    @FXML
    private Label MinuteLabel;

    @FXML
    private Label TaskName;

    @FXML
    private JFXToggleButton OffOnButton;

    @FXML
    private Button SettingsButton;

    public AlarmTask(AlarmTaskData data) {
        this.alarmTaskData = data;
    }

    public AlarmTask(int hour, int minute, String taskName) {
        this.alarmTaskData = new AlarmTaskData(hour, minute, taskName);
    }

    public void setRemoveConsumer(Consumer<RemoveData> removeConsumer) {
        this.removeConsumer = removeConsumer;
    }

    public void setSaveConsumer(Consumer<AlarmTask> saveConsumer) {
        this.saveConsumer = saveConsumer;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    public void initialize() {
        updateLabels();

        OffOnButton.setSelected(alarmTaskData.isActive());
        OffOnButton.setOnAction(event -> {
            alarmTaskData.setActive(OffOnButton.isSelected());
        });

        SettingsButton.setOnAction(event -> openSettings());
    }

    private void remove(AlarmTaskData data) {
        data.setAlive(false);
        removeConsumer.accept(new RemoveData(this, root));
    }

    private void openSettings() {
        Settings settings = new Settings(alarmTaskData, this::remove);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(settings);
        try {
            loader.setLocation(new File(SETTINGS_URL).toURI().toURL());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initOwner(HourLabel.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            updateLabels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLabels() {
        HourLabel.setText(alarmTaskData.getStringHour());
        MinuteLabel.setText(alarmTaskData.getStringMinute());
        TaskName.setText(alarmTaskData.getTaskName());
    }

    public AlarmTaskData getAlarmTaskData() {
        return alarmTaskData;
    }

    public boolean isAlive() {
        return alarmTaskData.isAlive();
    }

    public boolean isSingle() {
        EnumSet<DayOfWeek> enumSet = alarmTaskData.getDayOfWeeks();
        return Objects.isNull(enumSet) || enumSet.isEmpty();
    }

    public void off() {
        alarmTaskData.setActive(false);
        OffOnButton.setSelected(false);
        saveConsumer.accept(this);
    }

    public boolean isActive() {
        return alarmTaskData.isActive();
    }

    public boolean isDateTime(LocalDateTime dateTime) {
        EnumSet<DayOfWeek> enumSet = alarmTaskData.getDayOfWeeks();
        if (Objects.isNull(enumSet) || enumSet.isEmpty()) {
            return isTime(dateTime);
        }

        return enumSet.contains(dateTime.getDayOfWeek()) && isTime(dateTime);
    }

    private boolean isTime(LocalDateTime dateTime) {
        return dateTime.getHour() == alarmTaskData.getHour() && dateTime.getMinute() == alarmTaskData.getMinute();
    }

    public void wakeUp() {
        FXMLLoader loader = new FXMLLoader();
        WakeUp wakeUp = new WakeUp(alarmTaskData.getMusicPath());
        loader.setController(wakeUp);
        try {
            loader.setLocation(new File(WAKE_UP).toURI().toURL());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> wakeUp.stop());
            wakeUp.start();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
