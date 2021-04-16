package fr.melaine_gerard.melenbot.commands.utils;

import java.util.List;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SuggestionCommand implements ICommand {

    @Override
    public String getDescription() {
        return "Permet d'envoyer une suggestion";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }

        String suggestion = sb.toString();

        TextChannel channel = event.getGuild().getTextChannelById(DatabaseUtils.getSuggestionsChannel(event.getGuild().getId()));
        if(channel == null) {
            event.getChannel().sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "le salon des suggestions n'est pas défini !").build()).queue();
            return;
        }
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
        .setAuthor(event.getAuthor().getName(), event.getAuthor().getAvatarUrl(), event.getAuthor().getAvatarUrl())
        .setTitle("Nouvelle suggestion")
        .setDescription(suggestion);
        channel.sendMessage(eb.build()).queue();
        event.getChannel().sendMessage(EmbedUtils.createSuccessEmbed(event.getJDA(), "Ta suggestion a bien été envoyé !").build()).queue();
    }

    @Override
    public Category getCategory() {
        return Category.UTILS;
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
