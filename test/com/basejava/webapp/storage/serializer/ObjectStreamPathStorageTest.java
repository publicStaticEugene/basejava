package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.storage.AbstractStorageTest;
import com.basejava.webapp.storage.PathStorage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerializer()));
    }
}