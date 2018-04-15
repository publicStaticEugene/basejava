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
        helper.executeTransaction(con -> {
            try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                pstmt.setString(1, r.getUuid());
                pstmt.setString(2, r.getFullName());
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO contact (type, value, resume_uuid) VALUES (?, ?, ?)")) {
                executeQueryForContacts(r, pstmt);
            }
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        LOG.info("update " + r);
        helper.executeTransaction(con -> {
            try (PreparedStatement pstmt = con.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                pstmt.setString(2, r.getUuid());
                pstmt.setString(1, r.getFullName());
                if (pstmt.executeUpdate() == 0) throw new NotExistStorageException(r.getUuid());
            }
            try (PreparedStatement pstmt = con.prepareStatement("UPDATE contact SET type = ?, value = ? WHERE resume_uuid = ?")) {
                executeQueryForContacts(r, pstmt);
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("get " + uuid);
        return helper.executeQuery("" +
                        "SELECT * FROM resume r " +
                        "  LEFT JOIN contact c " +
                        "    ON r.uuid = c.resume_uuid " +
                        " WHERE r.uuid = ?",
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
        return helper.executeTransaction(con -> {
            try (PreparedStatement pstmt = con.prepareStatement("" +
                    "SELECT * FROM resume r " +
                    "  LEFT JOIN contact c " +
                    "    ON r.uuid = c.resume_uuid ")) {
                Map<String, Resume> resumes = new HashMap<>();
                ResultSet rs = pstmt.executeQuery();
                String uuid;
                String type;
                Resume resume;
                while (rs.next()) {
                    uuid = rs.getString("uuid");
                    if (!resumes.containsKey(uuid)) {
                        resume = new Resume(uuid, rs.getString("full_name"));
                        type = rs.getString("type");
                        if (type != null) {
                            resume.addContact(ContactType.valueOf(type), rs.getString("value"));
                        }
                        resumes.put(uuid, resume);
                    } else {
                        resumes.get(uuid).addContact(
                                ContactType.valueOf(rs.getString("type")),
                                rs.getString("value"));
                    }
                }
                List<Resume> result = new ArrayList<>(resumes.values());
                Collections.sort(result);
                return result;
            }
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

    private void executeQueryForContacts(Resume r, PreparedStatement pstmt) throws SQLException {
        for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
            pstmt.setString(1, entry.getKey().name());
            pstmt.setString(2, entry.getValue());
            pstmt.setString(3, r.getUuid());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
    }
}
