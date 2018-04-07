package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.storage.AbstractStorageTest;
import com.basejava.webapp.storage.PathStorage;

public class DataStreamPathStorageTest extends AbstractStorageTest {

    public DataStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerializer()));
    }
}