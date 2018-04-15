package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;
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
        helper.executeTransaction(conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                pstmt.setString(1, r.getUuid());
                pstmt.setString(2, r.getFullName());
                pstmt.executeUpdate();
            }
            insertContact(conn, r);
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        LOG.info("update " + r);
        helper.executeTransaction(conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                pstmt.setString(2, r.getUuid());
                pstmt.setString(1, r.getFullName());
                pstmt.executeUpdate();
            }
            deleteContact(conn, r);
            insertContact(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("get " + uuid);
        return helper.executeQuery("" +
                        "   SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "       ON r.uuid = c.resume_uuid " +
                        "    WHERE r.uuid = ?",
                pstmt -> {
                    pstmt.setString(1, uuid);
                    ResultSet rs = pstmt.executeQuery();
                    if (!rs.next()) throw new NotExistStorageException(uuid);
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    String value;
                    ContactType type;
                    do {
                        value = rs.getString("value");
                        if (value == null) break;
                        type = ContactType.valueOf(rs.getString("type"));
                        resume.addContact(type, value);
                    } while (rs.next());
                    return resume;
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
        return helper.executeQuery("" +
                        "   SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "       ON r.uuid = c.resume_uuid " +
                        " ORDER BY full_name, uuid",
                pstmt -> {
                    ResultSet rs = pstmt.executeQuery();
                    Map<String, Resume> resumes = new LinkedHashMap<>();
                    String uuid;
                    Resume resume;
                    while (rs.next()) {
                        uuid = rs.getString("uuid");
                        resume = resumes.get(uuid);
                        if (resume == null) {
                            resume = new Resume(uuid, rs.getString("full_name"));
                            resumes.put(uuid, resume);
                        }
                        addContact(rs, resume);
                    }
                    return new ArrayList<>(resumes.values());
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

    private void deleteContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            pstmt.setString(1, r.getUuid());
            pstmt.execute();
        }
    }

    private void insertContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO contact (type, value, resume_uuid) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                pstmt.setString(1, entry.getKey().name());
                pstmt.setString(2, entry.getValue());
                pstmt.setString(3, r.getUuid());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("type");
        if (type != null)
            resume.addContact(ContactType.valueOf(type), rs.getString("value"));
    }
}
