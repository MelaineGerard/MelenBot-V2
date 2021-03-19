package fr.melaine_gerard.melenbot.commands.mods;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UnmuteCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de rendre la parole à un membre";
    }

    @Override
    public String getUsage() {
        return "<@membre>";
    }

    @Override
    public Category getCategory() {
        return Category.MODS;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("Merci de mentionner la personne dont il faut rendre la parole !").queue();
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);


        if (!Objects.requireNonNull(event.getMember()).canInteract(member)) {
            event.getChannel().sendMessage("Tu ne peux pas rendre la parole à cette personne !").queue();
            return;
        }
        if (!event.getGuild().getSelfMember().canInteract(member)) {
            event.getChannel().sendMessage("Je ne peux pas rendre la parole à cette personne !").queue();
            return;
        }

        if (event.getGuild().getRolesByName("Muted", true).isEmpty()) {
            event.getGuild().createRole().setName("Muted").setMentionable(false).setPermissions(Permission.EMPTY_PERMISSIONS).complete();

        }
        Role role = event.getGuild().getRolesByName("Muted", true).get(0);
        for (GuildChannel channel : event.getGuild().getChannels()){
            if (!channel.getPermissionOverrides().contains(channel.getPermissionOverride(role))){
                channel.createPermissionOverride(role).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK).queue();
            }
        }

        if(!member.getRoles().contains(role)){
            event.getChannel().sendMessage("Cette personne n'est pas réduit au silence !").queue();
            return;
        }
        event.getGuild().removeRoleFromMember(member, role).queue();
        event.getChannel().sendMessage(EmbedUtils.createSuccessEmbed(event.getJDA(), member.getAsMention() + " vient de récupérer la parole !").build()).queue();

    }


    @Override
    public Collection<Permission> permissionsNeeded() {
        return Collections.singletonList(Permission.MESSAGE_MANAGE);
    }
}
