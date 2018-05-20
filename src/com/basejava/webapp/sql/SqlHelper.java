package com.basejava.webapp.sql;

import com.basejava.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void executeQuery(String sql) {
        executeQuery(sql, PreparedStatement::execute);
    }

    public <T> T executeQuery(String sql, QueryExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {
            return executor.execute(pstat);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    public <T> T executeTransaction(SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public Connection getConnection() {
        try {
            return connectionFactory.getConnection();
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }
}
