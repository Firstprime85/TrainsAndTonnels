package com.hutsko.entity;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tunnel {
    private static final int MAX_TUNNEL_CAPACITY = 3;
    private String name;
    private String flag;
    private Queue<Train> trains = new ArrayDeque<>();
    private final Semaphore semaphore = new Semaphore(MAX_TUNNEL_CAPACITY, true);
    private Lock lock = new ReentrantLock();
    private Condition isFree = lock.newCondition();

    public void inviteTrain(Train train) {
        lock.lock();
        if (flag == null) {
            flag = train.getSide();
        }
        trains.offer(train);
        isFree.signal();
        lock.unlock();
    }

    public void releaseTrain() {
        lock.lock();
        trains.poll();
        semaphore.release();
        if (trains.isEmpty()) {
            flag = null;
        }
        isFree.signal();
        lock.unlock();
    }

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public boolean isEmpty() {
        return trains.isEmpty();
    }

    public boolean contain(Train train) {
        return trains.contains(train);
    }

    public String getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
