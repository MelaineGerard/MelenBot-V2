package fr.melaine_gerard.melenbot.slashcommands;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PingSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Give you the ping of the bot";
    }

    @Override
    public void handle(SlashCommandEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true)
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
                ).queue();
    }
}
