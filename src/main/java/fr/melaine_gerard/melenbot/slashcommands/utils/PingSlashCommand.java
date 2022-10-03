package fr.melaine_gerard.melenbot.slashcommands.utils;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class PingSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Renvoie le ping du bot";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        InteractionHook interactionHook = event.reply("Pong!").setEphemeral(false).complete();
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
                .setTitle("Pong !")
                .addField("Gateway Ping : ", String.format("%dms", event.getJDA().getGatewayPing()), false)
                .addField("Rest Ping : ", String.format("%sms", event.getJDA().getRestPing().complete()), false);
        interactionHook.editOriginalEmbeds(eb.build()).queue();
    }
}
