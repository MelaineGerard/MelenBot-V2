package fr.melaine_gerard.melenbot.managers;

import fr.melaine_gerard.melenbot.interfaces.ISlashCommand;
import fr.melaine_gerard.melenbot.slashcommands.PingSlashCommand;
import fr.melaine_gerard.melenbot.utils.Constants;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SlashCommandManager {
    private final Map<String, ISlashCommand> slashCommands = new HashMap<>();


    public SlashCommandManager() {
        addSlashCommand(new PingSlashCommand());
    }

    private void addSlashCommand(ISlashCommand slashCommand) {
        if (!slashCommands.containsKey(slashCommand.getName())) {
            slashCommands.put(slashCommand.getName(), slashCommand);
        }
    }

    public void handleSlashCommand(SlashCommandEvent event) {
        final String invoke = event.getName();

        if (slashCommands.containsKey(invoke)) {
            ISlashCommand cmd = getSlashCommand(invoke);
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
        getSlashCommands().forEach((name, slashCommand) -> jda.upsertCommand(slashCommand.getName(), slashCommand.getDescription()).queue());
    }
}
