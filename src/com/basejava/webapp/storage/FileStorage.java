package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.serializer.SerializerStrategy;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private SerializerStrategy serializerStrategy;

    public FileStorage(File dir, SerializerStrategy serializerStrategy) {
        Objects.requireNonNull(dir, "directory must not be null");
        Objects.requireNonNull(serializerStrategy, "strategy must not be null");
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not a dir");
        } else if (!dir.canRead() || !dir.canWrite()) {
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = dir;
        this.serializerStrategy = serializerStrategy;
    }

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
            serializerStrategy.write(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", file.getAbsolutePath(), e);
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
            return serializerStrategy.read(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getAbsolutePath(), e);
        }
    }

    @Override
    protected List<Resume> doCopyList() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory file read error", directory.getAbsolutePath());
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
            throw new StorageException("Directory file read error", directory.getName());
        }
        return list.length;
    }
}
