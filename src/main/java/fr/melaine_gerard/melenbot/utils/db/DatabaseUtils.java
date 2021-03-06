package fr.melaine_gerard.melenbot.utils.db;

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
            PreparedStatement statement = melenConnection.prepareStatement("SELECT guildId FROM `guild` WHERE guildId = ?;");
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

    public static String getPrefix(String guildId){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("SELECT `prefix` FROM `guild` WHERE guildId = ?;");
            statement.setString(1, guildId);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePrefix(String guildId, String value){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("UPDATE `guild` SET `prefix` = ? WHERE `guildId` = ?;");
            statement.setString(1, value);
            statement.setString(2, guildId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getWelcomeMessage(String guildId){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("SELECT `welcomeMessage` FROM `guild` WHERE guildId = ?;");
            statement.setString(1, guildId);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getWelcomeChannel(String guildId){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("SELECT `welcomeId` FROM `guild` WHERE guildId = ?;");
            statement.setString(1, guildId);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSuggestionsChannel(String guildId){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("SELECT `suggestionsId` FROM `guild` WHERE guildId = ?;");
            statement.setString(1, guildId);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLogsChannel(String guildId){
        try {
            PreparedStatement statement = melenConnection.prepareStatement("SELECT `logsId` FROM `guild` WHERE guildId = ?;");
            statement.setString(1, guildId);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateWelcomeChannel(String guildId, String channelId) {
        try {
            PreparedStatement statement = melenConnection.prepareStatement("UPDATE `guild` SET  `welcomeId` = ? WHERE `guildId` = ?;");
            statement.setString(1, channelId);
            statement.setString(2, guildId);
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateLogsChannel(String guildId, String channelId) {
        try {
            PreparedStatement statement = melenConnection.prepareStatement("UPDATE `guild` SET  `logsId` = ? WHERE `guildId` = ?;");
            statement.setString(1, channelId);
            statement.setString(2, guildId);
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateSuggestionsChannel(String guildId, String channelId) {
        try {
            PreparedStatement statement = melenConnection.prepareStatement("UPDATE `guild` SET  `suggestionsId` = ? WHERE `guildId` = ?;");
            statement.setString(1, channelId);
            statement.setString(2, guildId);
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateWelcomeMessage(String guildId, String message) {
        try {
            PreparedStatement statement = melenConnection.prepareStatement("UPDATE `guild` SET  `welcomeMessage` = ? WHERE `guildId` = ?;");
            statement.setString(1, message);
            statement.setString(2, guildId);
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
