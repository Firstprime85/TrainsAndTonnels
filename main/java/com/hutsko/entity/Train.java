package com.hutsko.entity;

import com.hutsko.entity.pool.TrafficController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Train extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(Train.class);
    private int id;
    private String side;
    private TrafficController tController = TrafficController.CONTROLLER;
    private boolean inTunnel;

    public Train(int id, String side) {
        this.id = id;
        this.side = side;
    }

    @Override
    public void run() {

        try {
            TimeUnit.MILLISECONDS.sleep(50);
            System.out.println("Train №" + id + "(" + side + ")" + " awaiting");
            tController.getSlot(this);  // go if get permission
            String entranceSide = tController.getLastUsedTunnel();
            inTunnel = true;
            System.out.println("Train №" + id + "(" + side + ")" + " driving in the " +
                    entranceSide + "-tunnel.");
            if (side.equals("Left")) {
                tController.leftCounterDown();
            } else if (side.equals("Right")) {
                tController.rightCounterDown();
            }
            TimeUnit.MILLISECONDS.sleep(50); //imitation of moving in tunnel
            System.out.println("Train №" + id + "(" + side + ")" + " passed the " +
                    entranceSide + "-tunnel.");
            tController.returnSlot(this);
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e);
        }
    }

    public String getSide() {
        return side;
    }

    public boolean isInTunnel() {
        return inTunnel;
    }
}
