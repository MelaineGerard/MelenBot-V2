package fr.melaine_gerard.melenbot.commands.infos;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ServerinfoCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le serveur";
    }

    @Override
    public Category getCategory() {
        return Category.INFOS;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {

    }
}
