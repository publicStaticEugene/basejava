package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    private final String[] EXISTS_UUIDs = {
            "uuid28", "uuid5", "uuid1", "uuid2",
            "uuid10", "uuid7", "uuid35", "uuid3",
            "uuid33", "uuid21"
    };
    private final String NOT_EXISTS_UUID = "uuid12";

    private final Resume NOT_EXISTS_RESUME = new Resume(NOT_EXISTS_UUID, "fullName" + NOT_EXISTS_UUID);
    private final Resume EXISTS_RESUME = new Resume(EXISTS_UUIDs[5], "fullName" + EXISTS_UUIDs[5]);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        init();
    }

    @Test
    public void save() {
        storage.save(NOT_EXISTS_RESUME);
        assertSize(11);
        assertEquals(NOT_EXISTS_RESUME, storage.get(NOT_EXISTS_RESUME.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveAlreadyExist() {
        storage.save(EXISTS_RESUME);
        assertSize(10);
    }

    @Test
    public void update() {
        storage.update(EXISTS_RESUME);
        assertEquals(EXISTS_RESUME, storage.get(EXISTS_RESUME.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(NOT_EXISTS_RESUME);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(EXISTS_RESUME.getUuid());
        assertSize(9);
        storage.get(EXISTS_RESUME.getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(NOT_EXISTS_UUID);
    }

    @Test
    public void get() {
        Resume gottenResume = storage.get(EXISTS_RESUME.getUuid());
        assertEquals(EXISTS_RESUME, gottenResume);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(NOT_EXISTS_RESUME.getUuid());
    }

    @Test
    public void getAllSorted() {
        List<Resume> actual = storage.getAllSorted();
        List<Resume> expected = getExpectedList();
        assertEquals(actual.size(), expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }

    @Test
    public void size() {
        storage.save(NOT_EXISTS_RESUME);
        assertSize(11);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    private void init() {
        for (String uuid : EXISTS_UUIDs) {
            storage.save(new Resume(uuid, "fullName" + uuid));
        }
    }

    protected void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private List<Resume> getExpectedList() {
        List<Resume> result = new ArrayList<>();
        for (int i = 0; i < EXISTS_UUIDs.length; i++) {
            result.add(new Resume(EXISTS_UUIDs[i], "fullName" + EXISTS_UUIDs[i]));
        }
        result.sort(Comparator.comparing(Resume::getFullName));
        return result;
    }
}