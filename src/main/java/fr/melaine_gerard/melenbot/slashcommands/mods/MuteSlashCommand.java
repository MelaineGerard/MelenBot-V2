package fr.melaine_gerard.melenbot.slashcommands.mods;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MuteSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Permet de réduire au silence un utilisateur";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Member memberToMute = Objects.requireNonNull(event.getOption("member")).getAsMember();
        Member member = event.getMember();

        if(memberToMute == null || member == null || event.getGuild() == null) {
            event.reply("Il me manque des informations pour réduire au silence une personne, contactez le créateur si besoin !").queue();
            return;
        }


        if (!member.canInteract(memberToMute)) {
            event.reply("Tu ne peux pas réduire au silence cette personne !").queue();
            return;
        }
        if (!event.getGuild().getSelfMember().canInteract(memberToMute)) {
            event.reply("Je ne peux pas réduire au silence cette personne !").queue();
            return;
        }
        List<Role> roles = event.getGuild().getRolesByName("Muted", true);
        Role role;

        if (roles.isEmpty()) {
            role = event.getGuild().createRole().setName("Muted").setMentionable(false).setPermissions(Permission.EMPTY_PERMISSIONS).complete();
        }else {
            role = roles.get(0);
        }

        for (GuildChannel channel : event.getGuild().getChannels()){
            if (!channel.getPermissionContainer().getPermissionOverrides().contains(channel.getPermissionContainer().getPermissionOverride(role))){
                channel.getPermissionContainer().upsertPermissionOverride(role).deny(Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK, Permission.MESSAGE_SEND_IN_THREADS).queue();
            }
        }

        if(memberToMute.getRoles().contains(role)){
            event.reply("Cette personne est déjà réduit au silence !").queue();
            return;
        }
        event.getGuild().addRoleToMember(memberToMute, role).queue();
        event.replyEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), memberToMute.getAsMention() + " vient d'être réduit au silence !").build()).queue();
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return List.of(Permission.MESSAGE_MANAGE);
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.USER, "member", "Le membre que tu veux réduire au silence", true)
        );
    }
}
