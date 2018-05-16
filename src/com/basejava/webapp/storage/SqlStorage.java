package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.*;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    private final SqlHelper helper;

    public SqlStorage(final String dbUrl, final String dbUser, final String dbPassword) {
        this.helper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void save(final Resume r) {
        LOG.info("save " + r);
        helper.executeTransaction(conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                pstmt.setString(1, r.getUuid());
                pstmt.setString(2, r.getFullName());
                pstmt.executeUpdate();
            }
            insertContact(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void update(final Resume r) {
        LOG.info("update " + r);
        helper.executeTransaction(conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                pstmt.setString(2, r.getUuid());
                pstmt.setString(1, r.getFullName());
                pstmt.executeUpdate();
            }
            deleteContact(conn, r);
            insertContact(conn, r);
            deleteSection(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(final String uuid) {
        LOG.info("get " + uuid);
        return helper.executeQuery("" +
                        "   SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "       ON r.uuid = c.resume_uuid " +
                        "LEFT JOIN text_section t " +
                        "       ON r.uuid = t.resume_uuid " +
                        "LEFT JOIN list_section ls " +
                        "       ON r.uuid = ls.resume_uuid" +
                        "    WHERE r.uuid = ?",
                pstmt -> {
                    pstmt.setString(1, uuid);
                    final ResultSet rs = pstmt.executeQuery();
                    if (!rs.next()) throw new NotExistStorageException(uuid);
                    final Resume resume = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContact(rs, resume);
                        addTextSection(rs, resume);
                        addListSection(rs, resume);
                    } while (rs.next());
                    return resume;
                });
    }

    @Override
    public void delete(final String uuid) {
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
                        "LEFT JOIN text_section t " +
                        "       ON r.uuid = t.resume_uuid " +
                        "LEFT JOIN list_section ls " +
                        "       ON r.uuid = ls.resume_uuid " +
                        " ORDER BY full_name, uuid",
                pstmt -> {
                    final ResultSet rs = pstmt.executeQuery();
                    final Map<String, Resume> resumes = new LinkedHashMap<>();
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
                        addTextSection(rs, resume);
                        addListSection(rs, resume);
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
            final ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void deleteContact(final Connection conn, final Resume r) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            pstmt.setString(1, r.getUuid());
            pstmt.execute();
        }
    }

    private void insertContact(final Connection conn, final Resume r) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO contact (contact_type, value, resume_uuid) VALUES (?, ?, ?)")) {
            for (final Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                pstmt.setString(1, entry.getKey().name());
                pstmt.setString(2, entry.getValue());
                pstmt.setString(3, r.getUuid());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private void deleteSection(final Connection conn, final Resume r) throws SQLException {
        deleteTextSection(conn, r.getUuid());
        deleteListSectiob(conn, r.getUuid());
    }

    private void deleteTextSection(final Connection conn, final String uuid) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM text_section WHERE resume_uuid = ?")) {
            pstmt.setString(1, uuid);
            pstmt.execute();
        }
    }

    private void deleteListSectiob(final Connection conn, final String uuid) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM list_section WHERE resume_uuid = ?")) {
            pstmt.setString(1, uuid);
            pstmt.execute();
        }
    }

    private void insertSection(final Connection conn, final Resume r) throws SQLException {
        for (final Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
            switch (entry.getKey()) {
                case PERSONAL:
                case OBJECTIVE:
                    insertTextSection(conn, r.getUuid(), entry);
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    insertListSection(conn, r.getUuid(), entry);
                    break;
            }
        }
    }

    private void insertTextSection(final Connection conn, final String uuid, final Map.Entry<SectionType, Section> entry) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO text_section (text_type, content, resume_uuid) VALUES (?, ?, ?)")) {
            pstmt.setString(1, entry.getKey().name());
            pstmt.setString(2, ((TextSection) entry.getValue()).getContent());
            pstmt.setString(3, uuid);
            pstmt.executeUpdate();
        }
    }

    private void insertListSection(final Connection conn, final String uuid, final Map.Entry<SectionType, Section> entry) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO list_section (list_type, items, resume_uuid) VALUES (?, ?, ?)")) {
            final StringBuilder items = new StringBuilder();
            for (final String item : ((ListSection) entry.getValue()).getItems()) {
                items.append(item).append("\n");
            }
            pstmt.setString(1, entry.getKey().name());
            pstmt.setString(2, items.toString());
            pstmt.setString(3, uuid);
            pstmt.executeUpdate();
        }
    }

    private void addContact(final ResultSet rs, final Resume resume) throws SQLException {
        final String type = rs.getString("contact_type");
        if (type != null)
            resume.addContact(ContactType.valueOf(type), rs.getString("value"));
    }

    private void addTextSection(final ResultSet rs, final Resume resume) throws SQLException {
        final String type = rs.getString("text_type");
        if (type != null) {
            resume.addSection(SectionType.valueOf(type), new TextSection(rs.getString("content")));
        }
    }

    private void addListSection(final ResultSet rs, final Resume resume) throws SQLException {
        final String type = rs.getString("list_type");
        if (type != null) {
            final String items = rs.getString("items");
            resume.addSection(SectionType.valueOf(type), new ListSection(items.split("\n")));
        }
    }
}
