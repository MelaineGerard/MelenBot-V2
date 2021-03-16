package fr.melaine_gerard.melenbot.commands.infos;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ServerinfoCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le serveur";
    }

    @Override
    public Category getCategory() {
        return Category.INFOS;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        Guild guild = event.getGuild();
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
                .setTitle("Quelques informations sur le serveur " + guild.getName())
                .setThumbnail(guild.getIconUrl())
                .addField("Nom :", guild.getName(), false);
                if(guild.getOwner() != null) eb.addField("Owner :", guild.getOwner().getUser().getAsTag(), false);
                eb.addField("Date de création :", guild.getTimeCreated().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.localizedBy(Locale.FRANCE).withLocale(Locale.FRANCE)), false)
                .addField("Membres :", String.valueOf(guild.getMemberCount()), false)
                .addField("Salons :", String.valueOf(guild.getChannels().size()), false)
                .addField("Roles :", String.valueOf(guild.getRoles().size()), false)
                .addField("Région :", guild.getRegion().getName(), false)
                .addField("Boosts :", String.valueOf(guild.getBoostCount()), false)
                .addField("Région :", String.valueOf(guild.getBoostTier().getKey()), false);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
