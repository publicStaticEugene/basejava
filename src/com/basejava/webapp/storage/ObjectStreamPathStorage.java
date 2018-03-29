package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;

public class ObjectStreamPathStorage extends AbstractPathStorage {
    public ObjectStreamPathStorage(String dir) {
        super(dir);
    }

    @Override
    protected void doWrite(Resume r, OutputStream out) throws IOException {
        try (ObjectOutputStream objectOut = new ObjectOutputStream(out)) {
            objectOut.writeObject(r);
        }
    }

    @Override
    protected Resume doRead(InputStream in) throws IOException {
        try (ObjectInputStream objectIn = new ObjectInputStream(in)) {
            return (Resume) objectIn.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Read path error", e);
        }
    }
}
