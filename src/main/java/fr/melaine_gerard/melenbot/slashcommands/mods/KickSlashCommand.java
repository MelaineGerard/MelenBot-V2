package fr.melaine_gerard.melenbot.slashcommands.mods;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class KickSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Permet d'expulser un utilisateur du serveur";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Member member = event.getOption("user").getAsMember();

        String reason = event.getOption("reason").getAsString();

        if(!event.getMember().canInteract(member)){
            event.reply("Tu ne peux pas expulser cette personne !").queue();
            return;
        }
        if(!event.getGuild().getSelfMember().canInteract(member)){
            event.reply("Je ne peux pas expulser cette personne !").queue();
            return;
        }

        member.kick().reason(reason).queue(mbr -> event.replyEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), member.getUser().getAsTag() + " a été expulsé avec succès !").build()).queue());
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "L'utilisateur à expulser", true));
        options.add(new OptionData(OptionType.STRING, "reason", "La raison de l'expulsion", true));


        return options;
    }
}
