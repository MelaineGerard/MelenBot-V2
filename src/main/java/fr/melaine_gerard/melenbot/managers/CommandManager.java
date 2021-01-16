package fr.melaine_gerard.melenbot.managers;

import fr.melaine_gerard.melenbot.commands.EvalCommand;
import fr.melaine_gerard.melenbot.commands.HelpCommand;
import fr.melaine_gerard.melenbot.commands.PingCommand;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
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
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getName())){
            commands.put(command.getName(), command);
        }
    }


    public void handleCommand(GuildMessageReceivedEvent event) {
        String prefix = Constants.PREFIX;
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();


        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            ICommand cmd = commands.get(invoke);
            if (cmd.isOwnerCommand() && !event.getAuthor().getId().equals(Constants.OWNER_ID)){
                event.getChannel().sendMessage("This is an owner command !").queue();
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
