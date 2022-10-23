package fr.melaine_gerard.melenbot.slashcommands.infos;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.DateHelper;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class ServerinfoSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le serveur";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        String date = DateHelper.formatDateTime(guild.getTimeCreated());
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

        event.replyEmbeds(eb.build()).queue();
    }
}
