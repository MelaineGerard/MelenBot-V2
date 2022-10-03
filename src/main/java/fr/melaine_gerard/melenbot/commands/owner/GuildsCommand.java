package fr.melaine_gerard.melenbot.commands.owner;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class GuildsCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir la liste des serveurs du bot";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.OWNER;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (Guild g : event.getJDA().getGuilds()){
            sb.append(g.getName()).append(" (").append(g.getMemberCount()).append(" membres)\n");
        }
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
                .setDescription(sb.toString())
                .setTitle("Liste des serveurs ou je suis (" + event.getJDA().getGuilds().size() + " serveurs)");
        event.getChannel().sendMessageEmbeds(eb.build()).queue();

    }

    @Override
    public boolean isOwnerCommand() {
        return true;
    }
}
