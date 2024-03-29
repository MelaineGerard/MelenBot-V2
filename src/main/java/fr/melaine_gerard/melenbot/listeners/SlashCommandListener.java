package fr.melaine_gerard.melenbot.listeners;

import fr.melaine_gerard.melenbot.managers.SlashCommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {
    private final SlashCommandManager slashCommandManager;

    public SlashCommandListener(JDA jda) {
        slashCommandManager = new SlashCommandManager();
        System.out.println(slashCommandManager.getSlashCommand("ping").getDescription());
        slashCommandManager.registerSlashCommands(jda);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getUser().isBot()) return;
        slashCommandManager.handleSlashCommand(event);
    }
}
