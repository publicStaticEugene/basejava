package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        return Arrays.binarySearch(storage, 0, size, new Resume(uuid));
    }

    @Override
    protected void insertElement(Resume r, int index) {

    }

    @Override
    protected void fillDeletedElement(int index) {
        int len = size - 1 - index;
        if (len > 0)
            System.arraycopy(storage, index + 1, storage, index, len);
    }
}
