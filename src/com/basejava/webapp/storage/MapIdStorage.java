package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MapIdStorage extends AbstractMapStorage<Integer> {
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected Integer getSearchKey(String uuid) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().getUuid().equals(uuid))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        map.put(counter.incrementAndGet(), r);
    }

    @Override
    protected void doUpdate(Resume r, Object searchKey) {
        map.compute((Integer) searchKey, (k, v) -> r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        map.remove((Integer) searchKey);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return map.get((Integer) searchKey);
    }

}
