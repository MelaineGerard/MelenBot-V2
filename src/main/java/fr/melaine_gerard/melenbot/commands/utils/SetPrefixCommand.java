package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de changer le prefix du bot";
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        if (args.size() == 0){
            event.getChannel().sendMessage("Merci d'indiquer le nouveau prefix").queue();
            return;
        }
        String prefix = args.get(0);
        if (!DatabaseUtils.isGuildExists(event.getGuild().getId()))
            DatabaseUtils.createGuild(event.getGuild().getId());
        DatabaseUtils.updatePrefix(event.getGuild().getId(), prefix);
        event.getChannel().sendMessage(String.format("Le nouveau prefix est : %s", prefix)).queue();
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return Collections.singletonList(Permission.ADMINISTRATOR);
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public String getUsage() {
        return "<prefix>";
    }
}
