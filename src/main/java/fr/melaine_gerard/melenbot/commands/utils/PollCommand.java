package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PollCommand implements ICommand {
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        if (args.size() == 0){
            event.getChannel().sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer une question !").build()).queue();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String arg: args)
            sb.append(arg).append(" ");
        String question = sb.toString();

        event.getChannel().sendMessage(
                EmbedUtils.createEmbed(event.getJDA())
                        .setTitle("Nouveau sondage de " + event.getAuthor().getName())
                        .setDescription(question)
                        .build()
        ).queue(msg -> {
            msg.addReaction("✅").queue();
            msg.addReaction("❌").queue();
        });
    }

    @Override
    public Category getCategory() {
        return Category.UTILS;
    }
}
