package com.basejava;

public class WaitNotifyDeadlockTest {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                await();
            } catch (InterruptedException e) {/* NOP */}
        }).start();

        new Thread(WaitNotifyDeadlockTest::inc).start();

        Thread.sleep(1000);
        System.out.println(counter == 2 ? "Complete" : "Deadlock");
    }

    private static void inc() {
        synchronized (lock1) {
            counter++;
            lock1.notifyAll();
        }
    }

    private static void await() throws InterruptedException {
        synchronized (lock1) {
            while (counter <= 1) {
                lock1.wait();
            }
            counter++;
        }
    }
}
