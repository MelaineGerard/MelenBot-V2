package fr.melaine_gerard.melenbot.utils;

import fr.melaine_gerard.melenbot.MelenBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private static final Connection melenConnection = MelenBot.getDatabaseManager().getMelenDatabaseConnection();

    public static void createGuildTable() {
        try {
            Statement statement = melenConnection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS `guild` ( `guildId` VARCHAR(255) NOT NULL , `prefix` VARCHAR(255) NULL , `welcomeId` VARCHAR(255) NULL , `welcomeMessage` TEXT NULL , `logsId` VARCHAR(255) NULL , `suggestionsId` VARCHAR(255) NULL , PRIMARY KEY (`guildId`)) ENGINE = InnoDB;");
            logger.info("Guild table created !");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean isGuildExists(String guildId){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("SELECT * FROM `guild` WHERE guildId = ?;");
            statement.setString(1, guildId);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createGuild(String guildId){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("INSERT INTO `guild` (`guildId`, `prefix`, `welcomeId`, `welcomeMessage`, `logsId`, `suggestionsId`) VALUES (?, NULL, NULL, NULL, NULL, NULL);");
            statement.setString(1, guildId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String guildId, String column){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("SELECT ? FROM `guild` WHERE guildId = ?;");
            statement.setString(1, column);
            statement.setString(2, guildId);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString("prefix");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateValue(String guildId, String column, String value){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("UPDATE `guild` SET ? = ? WHERE `guild`.`guildId` = ?;");
            statement.setString(1, column);
            statement.setString(2, value);
            statement.setString(3, guildId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
