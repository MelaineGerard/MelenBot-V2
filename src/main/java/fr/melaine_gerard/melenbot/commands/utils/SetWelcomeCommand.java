package fr.melaine_gerard.melenbot.commands.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SetWelcomeCommand implements ICommand{

    @Override
    public String getDescription() {
        return "Permet de changer le message de bienvenue";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String message = sb.toString();

        DatabaseUtils.updateWelcomeMessage(event.getGuild().getId(), message);
        message = message.replaceAll("%users%", String.valueOf(event.getGuild().getMembers().size()))
        .replaceAll("%member%", event.getMember().getAsMention())
        .replaceAll("%tag%", event.getAuthor().getAsTag())
        .replaceAll("%guild%", event.getGuild().getName())
        .replaceAll("\\|", System.lineSeparator());
        event.getChannel().sendMessage(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau message de bienvenue est :\n" + message).build()).queue();
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return Collections.singletonList(Permission.MANAGE_CHANNEL);
    }

    @Override
    public Category getCategory() {
        return Category.UTILS;
    }

    @Override
    public boolean hasArgs() {
        return true;
    }

    @Override
    public String getUsage() {
        return "<message>";
    }
    
}
