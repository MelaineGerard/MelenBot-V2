package fr.melaine_gerard.melenbot.slashcommands.infos;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.managers.SlashCommandManager;
import fr.melaine_gerard.melenbot.utils.DateHelper;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.FunctionsUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class BotinfoSlashCommand implements ISlashCommand {
    private final SlashCommandManager slashCommandManager;

    public BotinfoSlashCommand(SlashCommandManager slashCommandManager) {
        this.slashCommandManager = slashCommandManager;
    }

    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le bot";
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        final RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        final JDA jda = event.getJDA();
        final SelfUser bot = jda.getSelfUser();
        String createdDate = DateHelper.formatDateTime(bot.getTimeCreated());
        int users = 0;
        EmbedBuilder eb = EmbedUtils.createEmbed(jda)
                .setTitle("Information sur moi :")
                .addField("Tag :", bot.getAsTag(), false)
                .addField("Uptime :", FunctionsUtils.getDurationBreakdown(rb.getUptime()), false)
                .addField("Date de création :", createdDate, false)
                .addField("Guildes :", String.valueOf(jda.getGuilds().size()), false);

        for (Guild g : jda.getGuilds())
            users += g.getMemberCount();

        eb.addField("Utilisateurs :", String.valueOf(users), false)
                .addField("Roles :", String.valueOf(jda.getRoles().size()), false)
                .addField("Commandes :", String.valueOf(slashCommandManager.getSlashCommands().size()), false)
                .addField("Lien d'invitation :", jda.getInviteUrl(Permission.ADMINISTRATOR), false)
                .addField("Faire un don :", "https://paypal.me/pools/c/8oaI7Zjbhv", false)
                .addField("Discord :", "https://discord.gg/VmuVsce", false)
                .addField("Code source :", "https://github.com/MelaineGerard/MelenBot-V2", false)
                .addField("Site Web :", "Bientôt", false);

        event.replyEmbeds(eb.build()).queue();

    }
}
