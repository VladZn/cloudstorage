package ru.cloud.storage.server;

import java.sql.*;
import java.util.HashMap;

public class SQLServer {
    static Connection connection = null;

    public void connect() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

        //TODO использовать Properties
        String dbServer = "localhost";
        String dbName = "cloudstorage";
        String dbUser = "root";
        String dbPassword = "Qwerty123";

        connection = DriverManager.getConnection("jdbc:mysql://"+ dbServer +"/"+ dbName +"?user="+ dbUser +"&password="+ dbPassword);
    }

    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
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

    public boolean userExists(String username) throws SQLException {
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

    public void getUserData() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM cloudstorage.users WHERE users.username = ?;");
        ResultSet rs = ps.executeQuery();

    }
}
