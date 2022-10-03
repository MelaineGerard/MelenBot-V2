package fr.melaine_gerard.melenbot.listeners;

import fr.melaine_gerard.melenbot.managers.CommandManager;
import fr.melaine_gerard.melenbot.utils.Constants;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMessageReceivedListeners extends ListenerAdapter {

    private final CommandManager commandManager = new CommandManager();


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage()) return;
        String temp = DatabaseUtils.getPrefix(event.getGuild().getId());
        String prefix = temp != null ? temp : Constants.PREFIX;
        if (event.getMessage().getContentRaw().startsWith(prefix) && event.getGuild().getSelfMember().hasPermission(event.getChannel().asGuildMessageChannel(), Permission.MESSAGE_SEND)) {
            commandManager.handleCommand(event);
        }
    }


}
