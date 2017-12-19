package com.hutsko.entity.pool;

import com.hutsko.entity.Train;
import com.hutsko.entity.Tunnel;

import java.util.ArrayList;

//  thread-safe pool
public enum TrafficController {
    CONTROLLER(2); // max tonnels

    private ArrayList<Tunnel> tunnels = new ArrayList<>();
    private int leftTurnCounter = 0;
    private int rightTurnCounter = 0;
    private String lastUsedTunnel;

    TrafficController(int tunnelQuantity) {
        int n = 65;      // loop for naming tunnels. Start from 'A'
        for (int i = 0; i < tunnelQuantity; i++) {
            Tunnel temp = new Tunnel();
            temp.setName(String.valueOf((char) n++));
            tunnels.add(temp);
        }
    }

    public boolean getSlot(Train train) {
        boolean permit = false;
        for (Tunnel tunnel : tunnels) {
            if (tunnel.isEmpty() && !train.isInTunnel()) {
                if (train.getSide().equals(getPriority())) {
                    permit = tunnel.tryAcquire();
                    tunnel.inviteTrain(train);
                    lastUsedTunnel = tunnel.getName();
                    return permit;
                }
            } else if (!tunnel.isEmpty() && !train.isInTunnel()) {
                if (train.getSide().equals(tunnel.getFlag())) {
                    if (permit = tunnel.tryAcquire()) {
                        tunnel.inviteTrain(train);
                        lastUsedTunnel = tunnel.getName();
                    }
                }
            }
        }
        return permit;
    }

    public void returnSlot(Train train) {
        for (Tunnel tunnel : tunnels) {
            if (tunnel.contain(train)) {
                tunnel.releaseTrain();
            }
        }
    }

    private String getPriority() {
        return (leftTurnCounter - rightTurnCounter >= 0) ? "Left" : "Right";
    }

    public void leftCounterUp() {
        leftTurnCounter++;
    }

    public void rightCounterUp() {
        rightTurnCounter++;
    }

    public void leftCounterDown() {
        leftTurnCounter--;
    }

    public void rightCounterDown() {
        rightTurnCounter--;
    }

    public int getLeftTurnCounter() {
        return leftTurnCounter;
    }

    public int getRightTurnCounter() {
        return rightTurnCounter;
    }

    public String getLastUsedTunnel() {
        return lastUsedTunnel;
    }
}

