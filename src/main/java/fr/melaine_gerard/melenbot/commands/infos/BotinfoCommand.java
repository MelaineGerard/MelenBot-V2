package fr.melaine_gerard.melenbot.commands.infos;

import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.managers.CommandManager;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.FunctionsUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class BotinfoCommand implements ICommand {
    private final CommandManager commandManager;

    public BotinfoCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le bot";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        final RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        final JDA jda = event.getJDA();
        final SelfUser bot = jda.getSelfUser();
        EmbedBuilder eb = EmbedUtils.createEmbed(jda)
                .setTitle("Information sur moi :")
                .addField("Tag :", bot.getAsTag(), false)
                .addField("Uptime :", FunctionsUtils.getDurationBreakdown(rb.getUptime()), false)
                .addField("Date de création :", bot.getTimeCreated().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME.localizedBy(Locale.FRANCE)), false)
                .addField("Guildes :", String.valueOf(jda.getGuilds().size()), false);
        int users = 0;
        for (Guild g : jda.getGuilds())
            users += g.getMemberCount();
        eb.addField("Utilisateurs :", String.valueOf(users), false)
                .addField("Roles :", String.valueOf(jda.getRoles().size()), false)
                .addField("Commandes :", String.valueOf(commandManager.getCommands().size()), false)
                .addField("Lien d'invitation :", bot.getJDA().getInviteUrl(Permission.ADMINISTRATOR), false)
                .addField("Faire un don :", "https://paypal.me/pools/c/8oaI7Zjbhv", false)
                .addField("Discord :", "https://discord.gg/VmuVsce", false)
                .addField("Code source :", "https://gitea.skitdev.icu/SkitDev/MelenBot-V2", false)
                .addField("Site Web :", "Bientôt", false);


        event.getChannel().sendMessage(eb.build()).queue();
    }
}
