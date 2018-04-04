package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.storage.AbstractStorageTest;
import com.basejava.webapp.storage.FileStorage;

public class DataStreamFileStorageTest extends AbstractStorageTest {

    public DataStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR_FILE, new DataStreamSerializer()));
    }
}