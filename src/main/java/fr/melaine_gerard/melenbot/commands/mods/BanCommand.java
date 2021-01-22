package fr.melaine_gerard.melenbot.commands.mods;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BanCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de bannir un membre";
    }

    @Override
    public Category getCategory() {
        return Category.MODS;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("Merci de mentionner la personne à bannir !").queue();
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);

        String reason = "No reason";
        if (!args.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (String arg : args) sb.append(arg).append(" ");
            reason = sb.toString().replaceFirst(member.getAsMention(), " ");
        }



        if(!event.getMember().canInteract(member)){
            event.getChannel().sendMessage("Tu ne peux pas bannir cette personne !").queue();
            return;
        }
        if(!event.getGuild().getSelfMember().canInteract(member)){
            event.getChannel().sendMessage("Je ne peux pas bannir cette personne !").queue();
            return;
        }

        member.ban(30, reason).queue();
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return Collections.singletonList(Permission.BAN_MEMBERS);
    }
}
