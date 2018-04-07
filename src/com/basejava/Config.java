package com.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private static final String PROPS_PATH = "config\\resumes.properties";
    private final Properties PRORS = new Properties();
    private File storage;

    private Config() {
        try (InputStream is = new FileInputStream(PROPS_PATH)) {
            PRORS.load(is);
            storage = new File(PRORS.getProperty("storage.dir"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS_PATH);
        }
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public File getStorage() {
        return storage;
    }
}
