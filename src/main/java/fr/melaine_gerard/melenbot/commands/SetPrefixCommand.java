package fr.melaine_gerard.melenbot.commands;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de changer le prefix du bot";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        if (args.size() == 0){
            event.getChannel().sendMessage("Merci d'indiquer le nouveau prefix").queue();
            return;
        }
        String prefix = args.get(0);
        if (!DatabaseUtils.isGuildExists(event.getGuild().getId()))
            DatabaseUtils.createGuild(event.getGuild().getId());
        DatabaseUtils.updateValue(event.getGuild().getId(), "prefix",prefix);
        event.getChannel().sendMessage(String.format("Le nouveau prefix est : %s", prefix)).queue();
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return Collections.singletonList(Permission.ADMINISTRATOR);
    }

    @Override
    public Category getCategory() {
        return Category.UTILS;
    }
}
