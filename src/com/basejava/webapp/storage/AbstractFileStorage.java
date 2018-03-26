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
    private int size;

    public AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
        } else if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writeable");
        }
        this.directory = directory;
    }

    protected abstract void doWrite(Resume r, File file);

    protected abstract Resume doRead(File file);

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
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        doWrite(r, file);
    }

    @Override
    protected void doDelete(File file) {
        file.delete();
    }

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    @Override
    protected List<Resume> doCopyList() {
        return Arrays.stream(directory.listFiles())
                .map(this::doRead)
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        Arrays.stream(directory.listFiles())
                .forEach(File::delete);
    }

    @Override
    public int size() {
        return size;
    }
}
