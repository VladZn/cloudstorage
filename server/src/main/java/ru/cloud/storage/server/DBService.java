package ru.cloud.storage.server;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.Properties;

public class DBService {
    static Connection connection = null;
    private String dbServer;
    private int dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    public void connect() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        try (Reader in = new InputStreamReader(this.getClass().getResourceAsStream("/db.properties"))) {
            Properties properties = new Properties();
            properties.load(in);
            dbServer = properties.getProperty("dbServerAddr");
            dbPort = Integer.parseInt(properties.getProperty("dbServerPort"));
            dbName = properties.getProperty("dbName");
            dbUser = properties.getProperty("dbUser");
            dbPassword = properties.getProperty("dbPassword");
            String conURL = "jdbc:mysql://" + dbServer + ":" + dbPort + "/" + dbName +
                    "?verifyServerCertificate=false&useSSL=true" +
                    "&useLegacyDatetimeCode=false" +
                    "&serverTimezone=UTC";
            connection = DriverManager.getConnection(conURL, dbUser, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `cloudstorage`.`users` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `username` VARCHAR(45) NOT NULL,\n" +
                    "  `password` VARBINARY(100) NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE INDEX `id_UNIQUE` (`id` ASC),\n" +
                    "  UNIQUE INDEX `username_UNIQUE` (`username` ASC));");
        }
    }

    boolean userExists(String username) throws SQLException {
        //TODO вынести тексты запросов в константы
        PreparedStatement ps = connection.prepareStatement("SELECT username FROM cloudstorage.users WHERE users.username = ?;");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public void addUser(String username, byte[] passw) throws SQLException {
        //TODO сделать отдельные PreparedStatement для каждого запроса
        PreparedStatement ps = connection.prepareStatement("INSERT INTO `cloudstorage`.`users` (`username`, `password`) VALUES (?, ?);");
        ps.setString(1, username);
        ps.setBytes(2, passw);
        ps.executeUpdate();
    }

    public void updatePassword(int userID, String newPassw) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE `cloudstorage`.`users` SET `password`=? WHERE `id`=?;");
        ps.setString(1, newPassw);
        ps.setInt(2, userID);
        ps.executeUpdate();

    }

    int getUserId(String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT id FROM cloudstorage.users WHERE users.username = ?");
        ps.setString(1, login);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("id");
    }

    byte[] getPassword(String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT password FROM cloudstorage.users WHERE users.username = ?");
        ps.setString(1, login);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getBytes("password");
    }
}
