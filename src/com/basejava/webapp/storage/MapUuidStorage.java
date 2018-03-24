package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

public class MapUuidStorage extends AbstractMapStorage<String, String> {

    @Override
    protected String getSearchKey(String uuid) {
        return map.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected void doSave(Resume r, String uuid) {
        map.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume r, String uuid) {
        map.put(uuid, r);
    }

    @Override
    protected void doDelete(String uuid) {
        map.remove(uuid);
    }

    @Override
    protected Resume doGet(String uuid) {
        return map.get(uuid);
    }
}
