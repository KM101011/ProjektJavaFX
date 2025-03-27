package com.example.projekt_1.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String DATABASE_CONFIG_FILE = "conf/database.properties";

    public static Connection establishConnection() throws SQLException, IOException {
        return getConnection();
    }

    private static Connection getConnection() throws SQLException, IOException {

        Properties properties = new Properties();

        properties.load(new FileReader(DATABASE_CONFIG_FILE));

        String databaseUrl = properties.getProperty("db.connection");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        Connection connection = DriverManager.getConnection(databaseUrl, username, password);

        logger.debug("Connection established");

        return connection;
    }
}