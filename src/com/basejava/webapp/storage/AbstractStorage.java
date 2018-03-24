package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage<S> implements Storage {

    protected abstract boolean isExist(S searchKey);

    protected abstract S getSearchKey(String uuid);

    protected abstract void doSave(Resume r, S searchKey);

    protected abstract void doUpdate(Resume r, S searchKey);

    protected abstract void doDelete(S searchKey);

    protected abstract Resume doGet(S searchKey);

    protected abstract List<Resume> doCopyList();

    @Override
    public void save(Resume r) {
        S searchKey = getNotExistSearchKey(r.getUuid());
        doSave(r, searchKey);
    }

    @Override
    public void update(Resume r) {
        S searchKey = getExistSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    @Override
    public void delete(String uuid) {
        S searchKey = getExistSearchKey(uuid);
        doDelete(searchKey);
    }

    @Override
    public Resume get(String uuid) {
        S searchKey = getExistSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = doCopyList();
        Collections.sort(list);
        return list;
    }

    private S getExistSearchKey(String uuid) {
        S searchKey = getSearchKey(uuid);
        if(!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private S getNotExistSearchKey(String uuid) {
        S searchKey = getSearchKey(uuid);
        if(isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
