package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.storage.AbstractStorageTest;
import com.basejava.webapp.storage.PathStorage;

public class JsonStreamPathStorageTest extends AbstractStorageTest {

    public JsonStreamPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new JsonStreamSerializer()));
    }
}