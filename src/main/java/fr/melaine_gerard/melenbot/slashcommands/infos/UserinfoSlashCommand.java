package fr.melaine_gerard.melenbot.slashcommands.infos;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UserinfoSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir des information sur un membre du discord";
    }

    @Override
    public String getUsage() {
        return ISlashCommand.super.getUsage();
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Member member = event.getOption("user").getAsMember();
        if (member == null) {
            event.reply("Merci de mentionner un membre !").queue();
            return;
        }

        OffsetDateTime creationDateTime = member.getTimeCreated();
        String creation_date = String.format("Le %s %d %s %d à %d:%d", creationDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), creationDateTime.getDayOfMonth(), creationDateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), creationDateTime.getYear(), creationDateTime.getHour(), creationDateTime.getMinute());
        OffsetDateTime joinedDateTime = member.getTimeJoined();
        String joined_date = String.format("Le %s %d %s %d à %d:%d", joinedDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), joinedDateTime.getDayOfMonth(), joinedDateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), joinedDateTime.getYear(), joinedDateTime.getHour(), joinedDateTime.getMinute());
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
                .setTitle("Quelques informations sur " + member.getUser().getName())
                .addField("Tag :", member.getUser().getAsTag(), false)
                .addField("Date de création :", creation_date, false)
                .addField("Date d'arrivée :", joined_date, false);
        StringBuilder sb = new StringBuilder();

        for (Role role : member.getRoles()) {
            sb.append(role.getAsMention()).append(", ");
        }

        String roles = sb.length() != 0 ? sb.substring(0, sb.length() - 2) : "0";
        eb.addField("Roles :", roles.length() <= 1024 ? roles : String.valueOf(member.getRoles().size()), false);
        sb = new StringBuilder();

        for (Permission permission : member.getPermissions()) {
            sb.append(permission.getName()).append(", ");
        }

        String permissions = sb.length() != 0 ? sb.substring(0, sb.length() - 2) : "Aucune";
        eb.addField("Permissions :", "```" + permissions + "```", false);

        event.replyEmbeds(eb.build()).queue();
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.singletonList(new OptionData(OptionType.USER, "user", "L'utilisateur dont tu veux récupérer les informations", true));
    }
}
