package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;
import org.junit.Test;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    public void saveOverflow() {
        for (int i = 100; i < (10_000 - 10 + 100); i++) {
            storage.save(new Resume("uuid" + i, "fullName" + i));
        }
        assertSize(10_000);
    }
}
