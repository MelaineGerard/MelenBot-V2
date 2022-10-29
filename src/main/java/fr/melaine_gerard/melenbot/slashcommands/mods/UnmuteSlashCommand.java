package fr.melaine_gerard.melenbot.slashcommands.mods;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UnmuteSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Permet de rendre la parole à un utilisateur";
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

        if (!roles.isEmpty()) {
            role = event.getGuild().createRole().setName("Muted").setMentionable(false).setPermissions(Permission.EMPTY_PERMISSIONS).complete();
        }else {
            event.reply("Cette personne n'est pas réduite au silence !").queue();
            return;
        }

        if(!memberToMute.getRoles().contains(role)){
            event.reply("Cette personne n'est pas réduite au silence !").queue();
            return;
        }

        event.getGuild().removeRoleFromMember(memberToMute, role).queue();
        event.replyEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), memberToMute.getAsMention() + " vient de récupérer la parole !").build()).queue();
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        return List.of(Permission.MESSAGE_MANAGE);
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.USER, "member", "Le membre à qui tu veux rendre la parole", true)
        );
    }
}
