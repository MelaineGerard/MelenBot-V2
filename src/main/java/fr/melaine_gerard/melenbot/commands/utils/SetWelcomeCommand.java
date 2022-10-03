package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SetWelcomeCommand implements ICommand {

    @Override
    public String getDescription() {
        return "Permet de changer le message de bienvenue";
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String message = sb.toString();

        DatabaseUtils.updateWelcomeMessage(event.getGuild().getId(), message);
        Member member = event.getMember();
        if(member == null) {
            event.getChannel().sendMessage("Je n'arrive pas à récupérer ton profil !").queue();
            return;
        }


        message = message.replaceAll("%users%", String.valueOf(event.getGuild().getMembers().size()))
                .replaceAll("%member%", member.getAsMention())
                .replaceAll("%tag%", event.getAuthor().getAsTag())
                .replaceAll("%guild%", event.getGuild().getName())
                .replaceAll("\\|", System.lineSeparator());
        event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau message de bienvenue est :\n" + message).build()).queue();
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return Collections.singletonList(Permission.MANAGE_CHANNEL);
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public boolean hasArgs() {
        return true;
    }

    @Override
    public String getUsage() {
        return "<message>";
    }

}
