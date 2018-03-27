package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    public AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        } else if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writeable");
        }
        this.directory = directory;
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("Write error", file.getAbsolutePath(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete())
            throw new StorageException("File delete error", file.getAbsolutePath());
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("Read error", file.getAbsolutePath(), e);
        }
    }

    @Override
    protected List<Resume> doCopyList() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error", directory.getAbsolutePath());
        }
        return Arrays.stream(files)
                .map(this::doGet)
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .forEach(this::doDelete);
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Directory read error", directory.getName());
        }
        return list.length;
    }
}
