package fr.melaine_gerard.melenbot.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private final Logger logger = LoggerFactory.getLogger(DbConnection.class);
    private final DbCredentials dbCredentials;
    private Connection connection;

    public DbConnection(DbCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
        this.connect();
    }

    private void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.dbCredentials.toUri(), this.dbCredentials.getUser(), this.dbCredentials.getPass());
            logger.info("Connected to the database !");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException {
        if(this.connection != null){
            if(this.connection.isClosed()){
                this.connection.close();
                logger.info("Disconnected from the database !");
            }
        }
    }

    public Connection getConnection(){
        try {
            if (this.connection != null && !this.connection.isClosed())
                return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connect();
        return this.connection;
    }
}
