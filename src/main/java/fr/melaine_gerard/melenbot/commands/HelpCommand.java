package fr.melaine_gerard.melenbot.commands;

import fr.melaine_gerard.melenbot.enumerarions.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.managers.CommandManager;
import fr.melaine_gerard.melenbot.utils.Constants;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class HelpCommand implements ICommand {
    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String getDescription() {
        return "Affiche la liste des commandes";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        if(args.isEmpty()){
            EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA());
            eb.setTitle("Liste de toutes mes commandes");
            for (Category cat : Category.values()) {
                StringBuilder sb = new StringBuilder();
                for (ICommand cmd : commandManager.getCommands()){
                    if(cat == cmd.getCategory())
                        sb.append(cmd.getName()).append(", ");
                }
                if(sb.length() != 0)
                    eb.addField(cat.getName(), sb.substring(0, sb.length() - 2), false);
            }
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        ICommand cmd = commandManager.getCommand(args.get(0).toLowerCase());
        if(cmd == null){
            event.getChannel().sendMessage("Cette commande n'existe pas !").queue();
            return;
        }

        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA());
        eb.setTitle("Information sur la commande " + cmd.getName());
        eb.addField("Description :", cmd.getDescription(), true);
        eb.addField("Utilisation :", Constants.PREFIX + cmd.getName() + " " + cmd.getUsage(), true);
        if(cmd.getCategory() != null) eb.addField("Catégorie :", cmd.getCategory().getName(), true);
        eb.addField("Owner only: ", cmd.isOwnerCommand() ? "Oui" : "Non", true);
        event.getChannel().sendMessage(eb.build()).queue();

    }

    @Override
    public String getUsage() {
        return "[commande]";
    }

    @Override
    public Category getCategory() {
        return Category.UTILS;
    }
}
