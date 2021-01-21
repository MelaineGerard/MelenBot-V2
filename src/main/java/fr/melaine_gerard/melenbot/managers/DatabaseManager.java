package fr.melaine_gerard.melenbot.managers;

import fr.melaine_gerard.melenbot.MelenBot;
import fr.melaine_gerard.melenbot.db.DbConnection;
import fr.melaine_gerard.melenbot.db.DbCredentials;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private final DbConnection melenDatabase;

    public DatabaseManager() {
        this.melenDatabase = new DbConnection(new DbCredentials(
                MelenBot.getConfig().getString("mysql.host"),
                MelenBot.getConfig().getString("mysql.user"),
                MelenBot.getConfig().getString("mysql.pass"),
                MelenBot.getConfig().getString("mysql.database"),
                MelenBot.getConfig().getInt("mysql.port")
        ));
    }

    public void close(){
        try {
            this.melenDatabase.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getMelenDatabaseConnection() {
        return melenDatabase.getConnection();
    }
}
