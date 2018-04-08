package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    private final ConnectionFactory connectionFactory;
    private int size;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void save(Resume r) {
        LOG.info("save " + r);
        executeQuery("INSERT INTO resume (uuid, full_name) VALUES (?, ?)",
                pstmt -> {
                    pstmt.setString(1, r.getUuid());
                    pstmt.setString(2, r.getFullName());
                    try {
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        throw new ExistStorageException(r.getUuid());
                    }
                    size++;
                });
    }

    @Override
    public void update(Resume r) {
        LOG.info("update " + r);
        executeQuery("UPDATE resume SET full_name = ? WHERE uuid = ?",
                pstmt -> {
                    pstmt.setString(2, r.getUuid());
                    pstmt.setString(1, r.getFullName());
                    pstmt.executeUpdate();
                });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("get " + uuid);
        return executeQueryWithResult("SELECT * FROM resume WHERE uuid = ?",
                pstmt -> {
                    pstmt.setString(1, uuid);
                    ResultSet rs = pstmt.executeQuery();
                    if (!rs.next()) throw new NotExistStorageException(uuid);
                    return new Resume(uuid, rs.getString("full_name"));
                });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("delete " + uuid);
        executeQuery("DELETE FROM resume WHERE uuid = ?",
                pstmt -> {
                    pstmt.setString(1, uuid);
                    if (pstmt.executeUpdate() == 0) throw new NotExistStorageException(uuid);
                    size--;
                });

    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        return executeQueryWithResult("SELECT * FROM resume",
                pstmt -> {
                    List<Resume> resumes = new ArrayList<>();
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next())
                        resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
                    Collections.sort(resumes);
                    return resumes;
                });
    }

    @Override
    public void clear() {
        LOG.info("clear");
        executeQuery("DELETE FROM resume",
                pstmt -> {
                    pstmt.executeUpdate();
                    size = 0;
                });
    }

    @Override
    public int size() {
        LOG.info("get size");
        return size;
    }

    private void executeQuery(String sql, QueryExecutor executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            executor.execute(pstmt);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private <T> T executeQueryWithResult(String sql, QueryExecutorWithResult<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return executor.execute(pstmt);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private interface QueryExecutorWithResult<T> {

        T execute(PreparedStatement pstmt) throws SQLException;
    }

    private interface QueryExecutor {

        void execute(PreparedStatement pstmt) throws SQLException;
    }
}
