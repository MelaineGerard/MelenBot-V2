package fr.melaine_gerard.melenbot.listeners;

import fr.melaine_gerard.melenbot.managers.CommandManager;
import fr.melaine_gerard.melenbot.managers.SlashCommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {
    private final SlashCommandManager slashCommandManager;

    public SlashCommandListener(JDA jda){
         slashCommandManager = new SlashCommandManager();
         slashCommandManager.registerSlashCommands(jda);
    }
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getUser().isBot()) return;
        slashCommandManager.handleSlashCommand(event);
    }
}
