package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        storage = new Resume[10_000];
        size = 0;
    }

    public void save(Resume r) {
        if (size == storage.length) {
            System.out.println("There are no free memory");
            return;
        } else if (r.getUuid() == null) {
            return;
        } else if (checkResume(r)) {
            System.out.println("This resume already exists");
            return;
        }
        storage[size] = r;
        size++;
    }

    public void update(Resume r) {
        if (r == null || !checkResume(r)) {
            System.out.println("This resume does not exist");
            return;
        }
        for (Resume resume : storage) {
            if (resume.getUuid().equals(r.getUuid())) {
                resume = r;
                return;
            }
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid))
                return storage[i];
        }
        System.out.println("This resume does not exist");
        return null;
    }

    public void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
                return;
            }
        }
        System.out.println("There are no resume to delete");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    private boolean checkResume(Resume r) {
        for (Resume resume : storage) {
            if (resume == r)
                return true;
        }
        return false;
    }
}
