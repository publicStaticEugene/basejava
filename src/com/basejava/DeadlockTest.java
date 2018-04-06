package com.basejava;

public class DeadlockTest {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                give();
            } catch (InterruptedException e) { /* NOP */ }
        }).start();

        new Thread(() -> {
            try {
                take();
            } catch (InterruptedException e) { /* NOP */ }
        }).start();

        Thread.sleep(1000);
        System.out.println(counter == 2 ? "Complete" : "Deadlock");
    }

    private static void give() throws InterruptedException {
        synchronized (lock1) {
            Thread.sleep(100);
            synchronized (lock2) {
                counter++;
            }
        }
    }

    private static void take() throws InterruptedException {
        synchronized (lock2) {
            Thread.sleep(100);
            synchronized (lock1) {
                counter++;
            }
        }
    }
}
