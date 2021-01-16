package fr.melaine_gerard.melenbot.interfaces;

import fr.melaine_gerard.melenbot.enumerations.Category;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public interface ICommand {
    default String getName() {
        return this.getClass().getSimpleName().toLowerCase().replaceAll("command", "");
    }

    String getDescription();

    default String getUsage() {
        return "";
    }

    default Category getCategory(){
        return Category.OTHER;
    }

    void handle(GuildMessageReceivedEvent event, List<String> args);

    default boolean isOwnerCommand(){
        return false;
    }


}
