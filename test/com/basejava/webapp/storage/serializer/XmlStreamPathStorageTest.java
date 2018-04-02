package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.storage.AbstractStorageTest;
import com.basejava.webapp.storage.PathStorage;

public class XmlStreamPathStorageTest extends AbstractStorageTest {

    public XmlStreamPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new XmlStreamSerializer()));
    }
}