package com.basejava.webapp.exception;

import java.sql.SQLException;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception cause) {
        this(message, null, cause);
    }

    public StorageException(String message, String uuid, Exception cause) {
        super(message, cause);
        this.uuid = uuid;
    }

    public StorageException(Exception e) {
        this(e.getMessage(), e);
    }

    public String getUuid() {
        return uuid;
    }
}
