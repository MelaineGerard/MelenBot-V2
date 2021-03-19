package fr.melaine_gerard.melenbot.managers;

import fr.melaine_gerard.melenbot.commands.infos.BotinfoCommand;
import fr.melaine_gerard.melenbot.commands.infos.ServerinfoCommand;
import fr.melaine_gerard.melenbot.commands.infos.UserinfoCommand;
import fr.melaine_gerard.melenbot.commands.mods.BanCommand;
import fr.melaine_gerard.melenbot.commands.mods.KickCommand;
import fr.melaine_gerard.melenbot.commands.mods.MuteCommand;
import fr.melaine_gerard.melenbot.commands.mods.UnmuteCommand;
import fr.melaine_gerard.melenbot.commands.music.*;
import fr.melaine_gerard.melenbot.commands.owner.EvalCommand;
import fr.melaine_gerard.melenbot.commands.owner.GuildsCommand;
import fr.melaine_gerard.melenbot.commands.utils.HelpCommand;
import fr.melaine_gerard.melenbot.commands.utils.PingCommand;
import fr.melaine_gerard.melenbot.commands.utils.PollCommand;
import fr.melaine_gerard.melenbot.commands.utils.SetPrefixCommand;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.Constants;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager() {
        //Owner
        addCommand(new EvalCommand());
        addCommand(new GuildsCommand());

        // Mods
        addCommand(new BanCommand());
        addCommand(new KickCommand());
        addCommand(new MuteCommand());
        addCommand(new UnmuteCommand());

        // Utils
        addCommand(new PollCommand());
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new SetPrefixCommand());

        // Music
        addCommand(new JoinCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new QueueCommand());
        addCommand(new RepeatCommand());
        addCommand(new LeaveCommand());

        // Infos
        addCommand(new BotinfoCommand(this));
        addCommand(new ServerinfoCommand());
        addCommand(new UserinfoCommand());
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getName())) {
            commands.put(command.getName(), command);
        }
    }


    public void handleCommand(GuildMessageReceivedEvent event) {
        String temp = DatabaseUtils.getPrefix(event.getGuild().getId());
        String prefix = temp != null ? temp : Constants.PREFIX;
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();


        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            ICommand cmd = commands.get(invoke);
            if (cmd.isOwnerCommand() && !event.getAuthor().getId().equals(Constants.OWNER_ID)) {
                event.getChannel().sendMessage("This is an owner command !").queue();
                return;
            }
            if (!Objects.requireNonNull(event.getMember()).hasPermission(cmd.permissionsNeeded()) && !event.getAuthor().getId().equals(Constants.OWNER_ID)) {
                event.getChannel().sendMessage("You don't have permission to do that !").queue();
                return;
            }
            try {
                cmd.handle(event, args);
            }catch(Exception e) {
                event.getChannel().sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Une erreur est survenue lors de l'exécution de cette commande !\n" + e.getMessage()).build()).queue();
            }
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }
}
