package com.basejava;

public class LazySingleton {
    /* Initialization-on-demand holder
    private LazySingleton() {}

    public static LazySingleton getInstance() {
        return LazySingletonHolder.INSTANCE;
    }

    private static class LazySingletonHolder {
        private static final LazySingleton INSTANCE = new LazySingleton();
    } */

    // Double checked locking
    private static volatile LazySingleton INSTANCE;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (LazySingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LazySingleton();
                }
            }
        }
        return INSTANCE;
    }
}
