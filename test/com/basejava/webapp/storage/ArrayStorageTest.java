package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import static org.junit.Assert.*;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Override
    protected void sortArray(Resume[] r) { /* NOP */ }
}