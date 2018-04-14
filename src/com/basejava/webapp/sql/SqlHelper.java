package com.basejava.webapp.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void executeQuery(String url) {
        executeQuery(url, PreparedStatement::execute);
    }

    public <T> T executeQuery(String sql, QueryExecutor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return executor.execute(pstmt);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }
}
