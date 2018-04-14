package com.basejava.webapp.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException() {
        this(null);
    }

    public ExistStorageException(String uuid) {
        super("Resume " + uuid + " is already exists", uuid);
    }
}
