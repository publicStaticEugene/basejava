package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractMapStorage<K> extends AbstractStorage {
    protected Map<K, Resume> map = new HashMap<>();

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        return map.values().stream()
                .sorted(Comparator.comparing(Resume::getFullName))
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        return map.size();
    }
}
