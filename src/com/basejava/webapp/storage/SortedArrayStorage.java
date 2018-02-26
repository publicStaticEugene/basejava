package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    public void save(Resume r) {

    }

    @Override
    protected int getIndex(String uuid) {
        return 0;
    }
}
