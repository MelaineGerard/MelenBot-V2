package fr.melaine_gerard.melenbot.managers;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.fun.HugSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.fun.PikachuSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.infos.BotinfoSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.infos.ServerinfoSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.infos.UserinfoSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.mods.BanSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.mods.KickSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.mods.MuteSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.mods.UnmuteSlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.utils.PingSlashCommand;
import fr.melaine_gerard.melenbot.utils.Constants;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SlashCommandManager {
    private final Map<String, ISlashCommand> slashCommands = new HashMap<>();


    public SlashCommandManager() {
        //Fun
        addSlashCommand(new HugSlashCommand());
        addSlashCommand(new PikachuSlashCommand());

        // Infos
        addSlashCommand(new BotinfoSlashCommand(this));
        addSlashCommand(new ServerinfoSlashCommand());
        addSlashCommand(new UserinfoSlashCommand());

        // Mods
        addSlashCommand(new BanSlashCommand());
        addSlashCommand(new KickSlashCommand());
        addSlashCommand(new MuteSlashCommand());
        addSlashCommand(new UnmuteSlashCommand());

        // Utils
        addSlashCommand(new PingSlashCommand());
    }

    private void addSlashCommand(ISlashCommand slashCommand) {
        if (!slashCommands.containsKey(slashCommand.getName())) {
            slashCommands.put(slashCommand.getName(), slashCommand);
        }
    }

    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        final String invoke = event.getName();

        if (slashCommands.containsKey(invoke)) {
            ISlashCommand cmd = getSlashCommand(invoke);
            if (!event.isFromGuild()) {
                event.reply("Tu dois utiliser cette commande sur un serveur Discord !").queue();
                return;
            }

            if (!Objects.requireNonNull(event.getMember()).hasPermission(cmd.permissionsNeeded()) && !event.getUser().getId().equals(Constants.OWNER_ID)) {
                event.reply("You don't have permission to do that !").queue();
                return;
            }
            try {
                cmd.handle(event);
            } catch (Exception e) {
                event.replyEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Une erreur est survenue lors de l'exécution de cette commande !\n" + e.getMessage()).build()).queue();
            }
        }
    }

    public ISlashCommand getSlashCommand(String name) {
        return slashCommands.get(name);
    }

    public Map<String, ISlashCommand> getSlashCommands() {
        return slashCommands;
    }

    public void registerSlashCommands(JDA jda){
        getSlashCommands().forEach((name, slashCommand) -> {
            CommandCreateAction command = jda.upsertCommand(slashCommand.getName(), slashCommand.getDescription());
            if(!slashCommand.getOptions().isEmpty()) {
                command.addOptions(slashCommand.getOptions());
            }
            command.queue();
        });
    }
}
