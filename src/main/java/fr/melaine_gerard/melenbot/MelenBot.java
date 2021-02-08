package fr.melaine_gerard.melenbot;

import fr.melaine_gerard.melenbot.listeners.GuildMessageReceivedListeners;
import fr.melaine_gerard.melenbot.managers.DatabaseManager;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import fr.melaine_gerard.melenbot.utils.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class MelenBot {
    private static YamlFile config;
    private static DatabaseManager databaseManager;

    public static void main(String[] args) {
        config = FileUtils.getYamlFile("config.yml");
        databaseManager = new DatabaseManager();
        getLogger().info("Connection in progress...");
        JDA jda = loadJda();
        registerListeners(jda);
        initializeDatabase(jda);
        getLogger().info(String.format("Connected as %s", jda.getSelfUser().getAsTag()));
    }

    public static YamlFile getConfig() {
        return config;
    }

    private static JDA loadJda() {
        try {
            JDABuilder jdaBuilder = JDABuilder.createDefault(getConfig().getString("token", "token"));
            jdaBuilder.setActivity(Activity.streaming("SkitDev", "https://twitch.tv/SkitDev"));
            return jdaBuilder.build().awaitReady();
        } catch (InterruptedException | LoginException e) {
            getLogger().error("Couldn't start the bot !");
            e.printStackTrace();
            getLogger().warn("Shutdown in progress...");
            FileUtils.saveYamlFile(config);
            databaseManager.close();
            System.exit(-1);
        }
        return null;
    }

    private static void initializeDatabase(JDA jda){
        DatabaseUtils.createGuildTable();
        for (Guild guild : jda.getGuilds()){
            if (!DatabaseUtils.isGuildExists(guild.getId()))
                DatabaseUtils.createGuild(guild.getId());
        }
        getLogger().info("Guild loaded !");
    }


    private static Logger getLogger() {
        return LoggerFactory.getLogger(MelenBot.class);
    }

    private static void registerListeners(JDA jda){
        jda.addEventListener(new GuildMessageReceivedListeners());
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
