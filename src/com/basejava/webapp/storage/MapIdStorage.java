package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MapIdStorage extends AbstractMapStorage<Integer, Integer> {
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
    protected void doSave(Resume r, Integer id) {
        map.put(counter.incrementAndGet(), r);
    }

    @Override
    protected void doUpdate(Resume r, Integer id) {
        map.put(id, r);
    }

    @Override
    protected void doDelete(Integer id) {
        map.remove(id);
    }

    @Override
    protected Resume doGet(Integer id) {
        return map.get(id);
    }

}
