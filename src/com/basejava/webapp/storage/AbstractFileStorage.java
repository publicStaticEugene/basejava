package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AbstractFileStorage extends AbstractStorage<File> {
    private final File storage;
    private int size;

    public AbstractFileStorage(File storage) {
        Objects.requireNonNull(storage);
        if (!storage.isDirectory()) {
            throw new StorageException("Storage is not a directory", storage.getName());
        }
        this.storage = storage;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(uuid);
    }

    @Override
    protected void doSave(Resume r, File file) {

    }

    @Override
    protected void doUpdate(Resume r, File file) {

    }

    @Override
    protected void doDelete(File file) {
        file.delete();
    }

    @Override
    protected Resume doGet(File file) {
        return null;
    }

    @Override
    protected List<Resume> doCopyList() {
        return null;
    }

    @Override
    public void clear() {
        Arrays.stream(storage.listFiles())
                .forEach(File::delete);
    }

    @Override
    public int size() {
        return size;
    }
}
