package com.basejava.webapp.storage;

import com.basejava.Config;

public class SqlStorageTest extends AbstractStorageTest {
    private static final Config config = Config.getInstance();

    public SqlStorageTest() {
        super(new SqlStorage(
                config.getDbUrl(),
                config.getDbUser(),
                config.getDbPassword()
        ));
    }
}