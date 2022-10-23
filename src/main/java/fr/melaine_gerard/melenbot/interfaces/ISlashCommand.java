package fr.melaine_gerard.melenbot.interfaces;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

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

    void handle(SlashCommandInteractionEvent event);

    default Collection<Permission> permissionsNeeded(){
        return Collections.emptyList();
    }

    default List<OptionData> getOptions() {
        return Collections.emptyList();
    }
}
