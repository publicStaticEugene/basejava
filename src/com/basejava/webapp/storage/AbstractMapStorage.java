package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.*;

public abstract class AbstractMapStorage<K, S> extends AbstractStorage<S> {
    protected Map<K, Resume> map = new HashMap<>();

    @Override
    protected boolean isExist(S searchKey) {
        return searchKey != null;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    protected List<Resume> doCopyList() {
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return map.size();
    }
}
