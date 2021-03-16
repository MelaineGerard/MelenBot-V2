package fr.melaine_gerard.melenbot.commands.infos;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class UserinfoCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir des information sur un membre du discord";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        if(event.getMessage().getMentionedMembers().isEmpty()){
            event.getChannel().sendMessage("Merci de mentionner un membre !").queue();
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);
        if (member == null){
            event.getChannel().sendMessage("Merci de mentionner un membre !").queue();
            return;
        }
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA());
        eb.setTitle("Quelques informations sur " + member.getUser().getName())
            .addField("Tag :",member.getUser().getAsTag(), false)
            .addField("Date de création :", member.getTimeCreated().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.localizedBy(Locale.FRANCE).withLocale(Locale.FRANCE)), false)
            .addField("Date d'arrivée :", member.getTimeJoined().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.localizedBy(Locale.FRANCE).withLocale(Locale.FRANCE)), false);
        StringBuilder sb = new StringBuilder();
        for (Role role : member.getRoles()){
            sb.append(role.getAsMention()).append(", ");
        }
        String roles = sb.substring(0, sb.length() - 2);
        eb.addField("Roles :", roles.length() <= 1024 ? roles : String.valueOf(member.getRoles().size()), false);
        sb = new StringBuilder();
        for (Permission permission : member.getPermissions()){
            sb.append(permission.getName()).append(", ");
        }
        String permissions = sb.substring(0, sb.length() - 2);
        eb.addField("Permissions :", "```" + permissions + "```", false);

        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public Category getCategory() {
        return Category.INFOS;
    }

    @Override
    public String getUsage() {
        return "<mention>";
    }
}
