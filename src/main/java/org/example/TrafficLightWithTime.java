package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//traffic light that show time
public class TrafficLightWithTime {
    public static void main(String[] args) {
        TrafficLightController controller = new TrafficLightController();

        new TrafficThread(controller,"North").start();
        new TrafficThread(controller,"South").start();
        new TrafficThread(controller,"East").start();
        new TrafficThread(controller,"West").start();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(controller::switchLights,0,2, TimeUnit.SECONDS);
    }
}
