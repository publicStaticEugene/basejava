package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.nio.file.Path;
import java.util.List;

public class AbstractPathStorage extends AbstractStorage<Path> {
    @Override
    protected boolean isExist(Path searchKey) {
        return false;
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return null;
    }

    @Override
    protected void doSave(Resume r, Path searchKey) {

    }

    @Override
    protected void doUpdate(Resume r, Path searchKey) {

    }

    @Override
    protected void doDelete(Path searchKey) {

    }

    @Override
    protected Resume doGet(Path searchKey) {
        return null;
    }

    @Override
    protected List<Resume> doCopyList() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }
}
