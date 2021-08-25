package fr.melaine_gerard.melenbot.slashcommands.fun;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.FunctionsUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class HugSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Permet de faire un calin à quelqu'un";
    }

    @Override
    public void handle(SlashCommandEvent event) {
        String person = "lui-même";
        if (event.getOptions().size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (OptionMapping arg : event.getOptions()) {
                sb.append(arg.getAsString()).append(" ");
            }
            person = sb.toString();
        }
        String imageUrl = FunctionsUtils.getImageFromApi("https://melenbot-api.melaine-gerard.fr/hug", (TextChannel) event.getChannel());
        event.getHook().sendMessageEmbeds(EmbedUtils.createEmbed(event.getJDA()).setTitle(event.getUser().getName() + " fait un câlin à " + person).setImage(imageUrl).build()).setEphemeral(false).queue();
    }

    @Override
    public String getUsage() {
        return "[personne]";
    }
}
