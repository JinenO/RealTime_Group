package org.example;

public class TrafficThread extends Thread{
    private final TrafficLightController controller;
    private final String direction;

    public TrafficThread(TrafficLightController controller, String direction) {
        this.controller = controller;
        this.direction = direction;
        setName("Thread-"+ direction);
    }

    public void run() {
        try{
            while(true){
                controller.pass(direction);
                Thread.sleep(3000);
            }
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

}
