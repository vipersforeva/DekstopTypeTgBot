package org.example.time_picker;

import org.example.user_data.AllData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleForTextFile {
    public void run() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = AllData::updateData;

        executor.scheduleAtFixedRate(task, 0, 10000, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));
    }
}
