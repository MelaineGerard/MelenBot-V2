package fr.melaine_gerard.melenbot.interfaces;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Collection;
import java.util.Collections;

public interface ISlashCommand {
    default String getName() {
        return this.getClass().getSimpleName().toLowerCase().replaceAll("slashcommand", "");
    }

    String getDescription();

    default String getUsage() {
        return "";
    }

    void handle(SlashCommandInteractionEvent event);

    default Collection<Permission> permissionsNeeded(){
        return Collections.emptyList();
    }
}
