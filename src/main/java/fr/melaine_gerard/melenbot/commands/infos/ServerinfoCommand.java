package fr.melaine_gerard.melenbot.commands.infos;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class ServerinfoCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le serveur";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INFOS;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        Guild guild = event.getGuild();
        OffsetDateTime dateTime = guild.getTimeCreated();
        String date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
                .setTitle("Quelques informations sur le serveur " + guild.getName())
                .setThumbnail(guild.getIconUrl())
                .addField("Nom :", guild.getName(), false);
        if (guild.getOwner() != null) eb.addField("Owner :", guild.getOwner().getUser().getAsTag(), false);
        eb.addField("Date de création :", date, false)
                .addField("Membres :", String.valueOf(guild.getMemberCount()), false)
                .addField("Salons :", String.valueOf(guild.getChannels().size()), false)
                .addField("Roles :", String.valueOf(guild.getRoles().size()), false)
                .addField("Boosts :", String.valueOf(guild.getBoostCount()), false)
                .addField("Région :", String.valueOf(guild.getBoostTier().getKey()), false);
        event.getChannel().sendMessageEmbeds(eb.build()).queue();
    }
}
