package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapIdStorageTest.class,
        MapResumeStorageTest.class,
        ObjectStreamFileStorageTest.class,
        ObjectStreamPathStorageTest.class,
        XmlStreamFileStorageTest.class,
        XmlStreamPathStorageTest.class,
        JsonStreamFileStorageTest.class,
        JsonStreamPathStorageTest.class
})
public class AllStorageTest {
}
