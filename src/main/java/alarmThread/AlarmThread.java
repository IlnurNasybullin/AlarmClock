package alarmThread;

import controllers.AlarmTask;
import javafx.application.Platform;

import javax.xml.ws.Service;
import java.time.LocalDateTime;

public class AlarmThread extends Thread {

    private final AlarmTask task;

    public AlarmThread(AlarmTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            while (task.isAlive()) {
                if (isNow()) {
                    wakeUp();
                    correctAlarm();
                    Thread.sleep(60_000);
                } else {
                    Thread.sleep(1_000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void correctAlarm() {
        if (task.isSingle()) {
            task.off();
        }
    }

    private void wakeUp() {
        Platform.runLater(task::wakeUp);
    }

    private boolean isNow() {
        if (!task.isActive()) {
            return false;
        }

        LocalDateTime dateTime = LocalDateTime.now();
        return task.isDateTime(dateTime);
    }
}
