package fr.melaine_gerard.melenbot.commands.mods;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class KickCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'expulser un membre";
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        if (event.getMessage().getMentions().getMembers().isEmpty()) {
            event.getChannel().sendMessage("Merci de mentionner la personne à expulser !").queue();
            return;
        }
        Member member = event.getMessage().getMentions().getMembers().get(0);

        String reason = "No reason";
        if (!args.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (String arg : args) sb.append(arg).append(" ");
            reason = sb.toString().replaceFirst(member.getAsMention(), " ");
        }



        if(!Objects.requireNonNull(event.getMember()).canInteract(member)){
            event.getChannel().sendMessage("Tu ne peux pas expulser cette personne !").queue();
            return;
        }
        if(!event.getGuild().getSelfMember().canInteract(member)){
            event.getChannel().sendMessage("Je ne peux pas expulser cette personne !").queue();
            return;
        }

        member.kick().reason(reason).queue(mbr -> event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), member.getUser().getAsTag() + " a été expulsé avec succès !").build()).queue());
    }


    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODS;
    }

    @Override
    public String getUsage() {
        return "<@membre> [raison]";
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return Collections.singletonList(Permission.KICK_MEMBERS);
    }
}
