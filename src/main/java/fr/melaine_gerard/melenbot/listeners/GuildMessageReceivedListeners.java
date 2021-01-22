package fr.melaine_gerard.melenbot.listeners;

import fr.melaine_gerard.melenbot.managers.CommandManager;
import fr.melaine_gerard.melenbot.utils.Constants;
import fr.melaine_gerard.melenbot.utils.DatabaseUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildMessageReceivedListeners extends ListenerAdapter {

    private final CommandManager commandManager = new CommandManager();
    private final Logger logger = LoggerFactory.getLogger(GuildMessageReceivedListeners.class);


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage()) return;
        String temp = DatabaseUtils.getPrefix(event.getGuild().getId());
        logger.info(temp);
        String prefix = temp != null ? temp : Constants.PREFIX;
        logger.info(prefix);
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            event.getChannel().sendTyping().queue();
            commandManager.handleCommand(event);
        }
    }
}
