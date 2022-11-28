package fr.melaine_gerard.melenbot.slashcommands.fun;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.FunctionsUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PikachuSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Envoie une image de pikachu";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        event.deferReply().queue(interactionHook -> {
            String imageUrl = FunctionsUtils.getImageFromApi("https://melenbot-api.melaine-gerard.fr/api/image/pikachu");
            if(imageUrl.isEmpty()) {
                interactionHook.sendMessage("Impossible de récupérer une image !").queue();
                return;
            }
            interactionHook.sendMessageEmbeds(EmbedUtils.createEmbed(event.getJDA()).setTitle("Pikachu !").setImage(imageUrl).build()).queue();
        });

    }
}
