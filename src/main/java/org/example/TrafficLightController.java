package org.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum LightColor{
    RED,GREEN,YELLOW
}
public class TrafficLightController {
    private final Lock lightLock= new ReentrantLock();
    private final Condition nsCondition= lightLock.newCondition();
    private final Condition ewCondition= lightLock.newCondition();

    private final Lock intersectionLock= new ReentrantLock();

    private LightColor nsLight= LightColor.GREEN;
    private LightColor ewLight= LightColor.RED;

    private String getTimeStamp(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public void pass(String direction) throws InterruptedException{
        lightLock.lock();
        try{
            while(!canPass(direction)){
                if(direction.equals("North")||direction.equals("South")){
                    nsCondition.await();
                }else{
                    ewCondition.await();
                }
            }
        }finally{
            lightLock.unlock();
        }

        intersectionLock.lock();

        try{
            System.out.println("[" + getTimeStamp() + "] " + Thread.currentThread().getName() +
                    ": " + direction + " is GREEN - 🚗 Passing...");
            Thread.sleep(1000);
        }finally {
            intersectionLock.unlock();
        }
    }

    private boolean canPass(String direction){
        return((direction.equals("North")||direction.equals("South"))&&nsLight==LightColor.GREEN)
                || ((direction.equals("East")||direction.equals("West"))&&ewLight==LightColor.GREEN);
    }

    public void switchLights(){
        lightLock.lock();
        try{
            System.out.println("\n[" + getTimeStamp() + "] Switching Lights...");
            if(nsLight==LightColor.GREEN){
                nsLight=LightColor.YELLOW;
                ewLight=LightColor.RED;
            }else if(nsLight==LightColor.YELLOW){
                nsLight=LightColor.RED;
                ewLight=LightColor.GREEN;
            }else if(ewLight==LightColor.GREEN){
                ewLight=LightColor.YELLOW;
                nsLight=LightColor.RED;
            }else if(ewLight==LightColor.YELLOW){
                ewLight=LightColor.RED;
                nsLight=LightColor.GREEN;
                nsCondition.signalAll();
            }
            printStatus();
        }finally{
            lightLock.unlock();
        }
    }

    private void printStatus(){
        System.out.println("========= TRAFFIC LIGHT STATUS =========");
        System.out.println("North : " + getColorEmoji(nsLight));
        System.out.println("South : " + getColorEmoji(nsLight));
        System.out.println("East  : " + getColorEmoji(ewLight));
        System.out.println("West  : " + getColorEmoji(ewLight));
        System.out.println("========================================\n");
    }

    private String getColorEmoji(LightColor color){
        return switch (color){
            case GREEN -> "\uD83D\uDC9A GREEN";
            case YELLOW -> "\uD83D\uDC9B YELLOW";
            case RED -> "❤\uFE0F RED";
        };
    }
}
