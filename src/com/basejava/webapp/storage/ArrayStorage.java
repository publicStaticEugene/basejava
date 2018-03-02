package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid))
                return i;
        }
        return -(size) - 1;
    }

    @Override
    protected void insertElement(Resume r, int index) {
        storage[- index - 1] = r;
    }

    @Override
    protected void fillDeletedElement(int index) {
        if (size - 1 - index > 0)
            storage[index] = storage[size - 1];
    }
}
