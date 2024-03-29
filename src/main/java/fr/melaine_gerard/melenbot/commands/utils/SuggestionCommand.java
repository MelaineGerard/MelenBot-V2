package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class SuggestionCommand implements ICommand {

    @Override
    public String getDescription() {
        return "Permet d'envoyer une suggestion";
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }

        String suggestion = sb.toString();
        String suggestionsChannelId = DatabaseUtils.getSuggestionsChannel(event.getGuild().getId());
        if(suggestionsChannelId == null) {
            event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "le salon des suggestions n'est pas défini !").build()).queue();
            return;
        }

        TextChannel channel = event.getGuild().getTextChannelById(suggestionsChannelId);
        if (channel == null) {
            event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "le salon des suggestions n'est pas défini !").build()).queue();
            return;
        }
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
                .setAuthor(event.getAuthor().getName(), event.getAuthor().getAvatarUrl(), event.getAuthor().getAvatarUrl())
                .setTitle("Nouvelle suggestion")
                .setDescription(suggestion);
        channel.sendMessageEmbeds(eb.build()).queue();
        event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Ta suggestion a bien été envoyé !").build()).queue();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public String getUsage() {
        return "<suggestion>";
    }

    @Override
    public boolean hasArgs() {
        return true;
    }
}
