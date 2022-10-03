package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class PollCommand implements ICommand {
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        if (args.size() == 0) {
            event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer une question !").build()).queue();
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(" ");
        String question = sb.toString();

        event.getChannel().sendMessageEmbeds(
                EmbedUtils.createEmbed(event.getJDA())
                        .setTitle("Nouveau sondage de " + event.getAuthor().getName())
                        .setDescription(question)
                        .build()
        ).queue(msg -> {
            msg.addReaction(Emoji.fromUnicode("✅")).queue();
            msg.addReaction(Emoji.fromUnicode("❌")).queue();
        });
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }
}
