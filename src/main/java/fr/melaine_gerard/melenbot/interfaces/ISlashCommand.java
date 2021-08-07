package fr.melaine_gerard.melenbot.interfaces;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ISlashCommand {
    default String getName() {
        return this.getClass().getSimpleName().toLowerCase().replaceAll("slashcommand", "");
    }

    String getDescription();

    default String getUsage() {
        return "";
    }

    void handle(SlashCommandEvent event);

    default Collection<Permission> permissionsNeeded(){
        return Collections.emptyList();
    }
}
