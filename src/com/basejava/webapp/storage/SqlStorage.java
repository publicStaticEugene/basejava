package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.helper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void save(Resume r) {
        LOG.info("save " + r);
        helper.executeQuery("INSERT INTO resume (uuid, full_name) VALUES (?, ?)", pstmt -> {
            pstmt.setString(1, r.getUuid());
            pstmt.setString(2, r.getFullName());
            pstmt.executeUpdate();
            return null;
        });
        executeQueryForContacts(r, "INSERT INTO contact (type, value, resume_uuid) VALUES (?, ?, ?)");
    }

    @Override
    public void update(Resume r) {
        LOG.info("update " + r);
        helper.executeQuery("UPDATE resume SET full_name = ? WHERE uuid = ?", pstmt -> {
            pstmt.setString(2, r.getUuid());
            pstmt.setString(1, r.getFullName());
            if (pstmt.executeUpdate() == 0) throw new NotExistStorageException(r.getUuid());
            return null;
        });
        executeQueryForContacts(r, "UPDATE contact SET type = ?, value = ? WHERE resume_uuid = ?");
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("get " + uuid);
        return helper.executeQuery("SELECT * FROM resume WHERE uuid = ?", pstmt -> {
            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) throw new NotExistStorageException(uuid);
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("delete " + uuid);
        helper.executeQuery("DELETE FROM resume WHERE uuid = ?", pstmt -> {
            pstmt.setString(1, uuid);
            if (pstmt.executeUpdate() == 0) throw new NotExistStorageException(uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        return helper.executeQuery("SELECT * FROM resume ORDER BY full_name, uuid", pstmt -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            return resumes;
        });
    }

    @Override
    public void clear() {
        LOG.info("clear");
        helper.executeQuery("DELETE FROM resume");
    }

    @Override
    public int size() {
        LOG.info("get size");
        return helper.executeQuery("SELECT count(*) FROM resume", pstmt -> {
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void executeQueryForContacts(Resume r, String url) {
        r.getContacts().forEach((key, value) -> helper.executeQuery(url, pstmt -> {
            pstmt.setString(1, r.getUuid());
            pstmt.setString(2, key.name());
            pstmt.setString(3, value);
            return null;
        }));
    }
}
