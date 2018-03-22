package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    protected void sortArray(Resume[] r) {}
}