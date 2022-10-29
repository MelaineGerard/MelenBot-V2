package fr.melaine_gerard.melenbot.managers;

import fr.melaine_gerard.melenbot.commands.music.*;
import fr.melaine_gerard.melenbot.commands.owner.*;
import fr.melaine_gerard.melenbot.commands.utils.*;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.*;
import fr.melaine_gerard.melenbot.utils.db.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager() {
        //Owner
        addCommand(new EvalCommand());
        addCommand(new GuildsCommand());
        addCommand(new ExecCommand());

        // Utils
        addCommand(new PollCommand());
        addCommand(new HelpCommand(this));
        addCommand(new SetPrefixCommand());
        addCommand(new SetChannelCommand());
        addCommand(new SetWelcomeCommand());
        addCommand(new SuggestionCommand());
        addCommand(new StackOverflowCommand());

        // Music
        addCommand(new JoinCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new QueueCommand());
        addCommand(new RepeatCommand());
        addCommand(new LeaveCommand());
        addCommand(new VolumeCommand());
        addCommand(new ShuffleCommand());
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getName())) {
            commands.put(command.getName(), command);
        }
    }


    public void handleCommand(MessageReceivedEvent event) {
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

            if (cmd.hasArgs() && args.size() == 0) {
                event.getChannel().sendMessage("Please indicate needed arguments !").queue();
                return;
            }
            try {
                cmd.handle(event, args);
            }catch(Exception e) {
                event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Une erreur est survenue lors de l'exécution de cette commande !\n" + e.getMessage()).build()).queue();
            }
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(String name) {
        return commands.get(name);
    }
}
