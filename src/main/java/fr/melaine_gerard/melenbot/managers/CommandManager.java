package fr.melaine_gerard.melenbot.managers;

import fr.melaine_gerard.melenbot.commands.mods.BanCommand;
import fr.melaine_gerard.melenbot.commands.owner.EvalCommand;
import fr.melaine_gerard.melenbot.commands.utils.HelpCommand;
import fr.melaine_gerard.melenbot.commands.infos.PingCommand;
import fr.melaine_gerard.melenbot.commands.utils.SetPrefixCommand;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.Constants;
import fr.melaine_gerard.melenbot.utils.DatabaseUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager(){
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new EvalCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new BanCommand());
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getName())){
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
            if (cmd.isOwnerCommand() && !event.getAuthor().getId().equals(Constants.OWNER_ID)){
                event.getChannel().sendMessage("This is an owner command !").queue();
                return;
            }
            if(!event.getMember().hasPermission(cmd.permissionsNeeded())){
                event.getChannel().sendMessage("You don't have permission to do that !").queue();
                return;
            }
            cmd.handle(event, args);
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }
}
