package com.hutsko.main;

import com.hutsko.entity.Train;
import com.hutsko.entity.pool.TrafficController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final int MAX_TRAIN = 10;

    public static void main(String[] args) {
        TrafficController tController = TrafficController.CONTROLLER;
        int idGenerator = 0;

        for (int i = 0; i < MAX_TRAIN; i++) {
            int rand = (int) (Math.random() * 1.7); // "1.7" coefficient makes proportion like 60:40
            if (rand == 0) {
                tController.leftCounterUp();
                new Train(++idGenerator, "Left").start();
            }
            if (rand == 1) {
                tController.rightCounterUp();
                new Train(++idGenerator, "Right").start();
            }
        }
        LOGGER.log(Level.INFO, "all trains have started");
        try {
            for (int i = 0; i < 4; i++) {       //  print intermediate queue check.
                System.out.println("Left side trains - " + tController.getLeftTurnCounter() + " : " +
                        tController.getRightTurnCounter() + " - Right side trains");
                TimeUnit.MILLISECONDS.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}