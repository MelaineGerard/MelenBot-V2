package fr.melaine_gerard.melenbot.slashcommands.mods;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BanSlashCommand implements ISlashCommand {
    @Override
    public String getDescription() {
        return "Permet de bannir un utilisateur";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        Member author = event.getMember();
        Member member = event.getOption("user").getAsMember();

        String reason = event.getOption("reason").getAsString();


        if (!author.canInteract(member)) {
            event.reply("Tu ne peux pas bannir cette personne !").queue();
            return;
        }
        if (!event.getGuild().getSelfMember().canInteract(member)) {
            event.reply("Je ne peux pas bannir cette personne !").queue();
            return;
        }

        member.ban(7, TimeUnit.DAYS).reason(reason).queue(mbr -> event.replyEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), member.getUser().getAsTag() + " a été banni avec succès !").build()).queue());
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "L'utilisateur à bannir", true));
        options.add(new OptionData(OptionType.STRING, "reason", "La raison du bannissement", true));


        return options;
    }
}
