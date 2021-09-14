package fr.melaine_gerard.melenbot.interfaces;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public interface ICommand {
    default String getName() {
        return this.getClass().getSimpleName().toLowerCase().replaceAll("command", "");
    }

    String getDescription();

    default String getUsage() {
        return "";
    }

    default CommandCategory getCategory(){
        return CommandCategory.OTHER;
    }

    void handle(GuildMessageReceivedEvent event, List<String> args);

    default boolean isOwnerCommand(){
        return false;
    }

    default Collection<Permission> permissionsNeeded(){
        return Collections.emptyList();
    }

    default boolean hasArgs(){
        return false;
    }

    default List<String> aliases(){
        return Collections.emptyList();
    }


}
