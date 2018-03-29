package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;

    public AbstractPathStorage(Path directory) {
        Objects.requireNonNull(directory);
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory.toString() + " is not a directory");
        } else if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory.toString() + " is not readable/writeable");
        }
        this.directory = directory;
    }

    protected abstract void doWrite(Resume r, OutputStream out);

    protected abstract Resume doRead(InputStream in);

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(uuid);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
            doUpdate(r, path);
        } catch (IOException e) {
            throw new StorageException("File create error", path.toString(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            doWrite(r, new ObjectOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Write error", path.toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        File file = path.toFile();
        file.delete();
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(new ObjectInputStream(new FileInputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("Read error", path.toString(), e);
        }
    }

    @Override
    protected List<Resume> doCopyList() {
        try {
            return Files
                    .list(directory)
                    .map(this::doGet)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException(directory.toString() + "is not a directory", directory.toString(), e);
        }
    }

    @Override
    public void clear() {
        try {
            Files
                    .list(directory)
                    .forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException(directory.toString() + "is not a directory", directory.toString(), e);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files
                    .list(directory)
                    .count();
        } catch (IOException e) {
            throw new StorageException(directory.toString() + "is not a directory", directory.toString(), e);

        }
    }
}
