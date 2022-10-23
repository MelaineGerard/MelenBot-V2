package fr.melaine_gerard.melenbot.commands.fun;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.FunctionsUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class HugCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de faire un calin à quelqu'un";
    }

    @Override
    public String getUsage() {
        return "[personne]";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        String person = "lui-même";
        if (args.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            person = sb.toString();
        }
        String imageUrl = FunctionsUtils.getImageFromApi("https://melenbot-api.melaine-gerard.fr/hug");
        event.getChannel().sendMessageEmbeds(EmbedUtils.createEmbed(event.getJDA()).setTitle(event.getAuthor().getName() + " fait un câlin à " + person).setImage(imageUrl).build()).queue();
    }

}
