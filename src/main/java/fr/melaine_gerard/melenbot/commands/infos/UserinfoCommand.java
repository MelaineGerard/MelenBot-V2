package fr.melaine_gerard.melenbot.commands.infos;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class UserinfoCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir des information sur un membre du discord";
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        if (event.getMessage().getMentions().getMembers().isEmpty()) {
            event.getChannel().sendMessage("Merci de mentionner un membre !").queue();
            return;
        }
        Member member = event.getMessage().getMentions().getMembers().get(0);
        if (member == null) {
            event.getChannel().sendMessage("Merci de mentionner un membre !").queue();
            return;
        }
        OffsetDateTime creationDateTime = member.getTimeCreated();
        String creation_date = String.format("Le %s %d %s %d à %d:%d", creationDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), creationDateTime.getDayOfMonth(), creationDateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), creationDateTime.getYear(), creationDateTime.getHour(), creationDateTime.getMinute());
        OffsetDateTime joinedDateTime = member.getTimeJoined();
        String joined_date = String.format("Le %s %d %s %d à %d:%d", joinedDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), joinedDateTime.getDayOfMonth(), joinedDateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), joinedDateTime.getYear(), joinedDateTime.getHour(), joinedDateTime.getMinute());

        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA());
        eb.setTitle("Quelques informations sur " + member.getUser().getName())
                .addField("Tag :", member.getUser().getAsTag(), false)
                .addField("Date de création :", creation_date, false)
                .addField("Date d'arrivée :", joined_date, false);
        StringBuilder sb = new StringBuilder();
        for (Role role : member.getRoles()) {
            sb.append(role.getAsMention()).append(", ");
        }
        String roles = sb.substring(0, sb.length() - 2);
        eb.addField("Roles :", roles.length() <= 1024 ? roles : String.valueOf(member.getRoles().size()), false);
        sb = new StringBuilder();
        for (Permission permission : member.getPermissions()) {
            sb.append(permission.getName()).append(", ");
        }
        String permissions = sb.substring(0, sb.length() - 2);
        eb.addField("Permissions :", "```" + permissions + "```", false);

        event.getChannel().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.INFOS;
    }

    @Override
    public String getUsage() {
        return "<mention>";
    }
}
