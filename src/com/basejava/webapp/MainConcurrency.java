package com.basejava.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
//    private static int counter;
    private static final AtomicInteger counter = new AtomicInteger(0);
//    private static final Object LOCK = new Object();
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
            }
        };
        thread0.start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + ", "
                + Thread.currentThread().getState())).start();

        System.out.println(thread0.getState());
        final MainConcurrency mainConcurrency = new MainConcurrency();
        final CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        final ExecutorService executorService = Executors.newCachedThreadPool();
//        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);
        long start = System.currentTimeMillis();

        for (int i = 0; i < THREADS_NUMBER; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 100; j++) mainConcurrency.inc();
                latch.countDown();

            });
//            Thread thread = new Thread(() -> {
//                for (int j = 0; j < 100; j++) mainConcurrency.inc();
//                latch.countDown();
//            });
//            thread.start();
//            threads.add(thread);
        }

//        threads.forEach(t -> {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdownNow();
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(counter.get());
    }

    private void inc() {
//        synchronized (this) {
//        try {
//            LOCK.lock();
        counter.incrementAndGet();
//        } finally {
//            LOCK.unlock();
//        }
    }
}
