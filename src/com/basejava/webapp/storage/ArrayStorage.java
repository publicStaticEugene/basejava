package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    public void save(Resume r) {
        if (r == null || r.getUuid() == null) {
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

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid))
                return i;
        }
        return -1;
    }
}
