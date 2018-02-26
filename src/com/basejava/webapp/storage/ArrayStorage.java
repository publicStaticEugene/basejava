package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        storage = new Resume[10_000];
        size = 0;
    }

    public void save(Resume r) {
        if (r.getUuid() == null) {
            System.out.println("Incorrect resume");
        } else if (size == storage.length) {
            System.out.println("Storage overflow");
        } else if (getIndex(r.getUuid()) > -1) {
            System.out.println("This resume already exists");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public void update(Resume r) {
        int index;
        if (r == null || (index = getIndex(r.getUuid())) < 0) {
            System.out.println("This resume does not exist");
        } else {
            storage[index] = r;
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index > -1)
            return storage[index];
        System.out.println("This resume does not exist");
        return null;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index > -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("There are no resume to delete");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid))
                return i;
        }
        return -1;
    }
}
