package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {

    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void update(Resume r) {
        int index;
        if (r == null || (index = getIndex(r.getUuid())) < 0) {
            System.out.println("This resume does not exist");
        } else {
            storage[index] = r;
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            return storage[index];
        } else {
            System.out.println("This resume does not exist");
            return null;
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    public int size() {
        return size;
    }

    protected boolean validateResume(Resume r) {
        if (r == null || r.getUuid() == null) {
            System.out.println("Incorrect resume");
            return false;
        } else if (size == STORAGE_LIMIT) {
            System.out.println("Storage overflow");
            return false;
        } else if (getIndex(r.getUuid()) > -1) {
            System.out.println("Resume " + r.getUuid() + " is exists");
            return false;
        } else {
            return true;
        }
    }

    protected abstract int getIndex(String uuid);
}
