package fr.melaine_gerard.melenbot.commands.fun;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.FunctionsUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class PikachuCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Envoie une image de pikachu";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        String imageUrl = FunctionsUtils.getImageFromApi("https://melenbot-api.melaine-gerard.fr/pikachu");
        if(imageUrl.isEmpty()) return;
        event.getChannel().sendMessageEmbeds(EmbedUtils.createEmbed(event.getJDA()).setTitle("Pikachu !").setImage(imageUrl).build()).queue();
    }
}
