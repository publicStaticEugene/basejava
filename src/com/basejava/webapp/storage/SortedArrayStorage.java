package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume key = new Resume(uuid, "dummy");
        return Arrays.binarySearch(storage, 0, size, key, Comparator.comparing(Resume::getUuid));
    }

    @Override
    protected void insertElement(Resume r, int index) {
        index = -(index) - 1;
        int len = size - index;
        if (len > 0) {
            System.arraycopy(storage, index, storage, index + 1, len);
        }
        storage[index] = r;
    }

    @Override
    protected void fillDeletedElement(int index) {
        int len = size - 1 - index;
        if (len > 0) {
            System.arraycopy(storage, index + 1, storage, index, len);
        }
    }
}
