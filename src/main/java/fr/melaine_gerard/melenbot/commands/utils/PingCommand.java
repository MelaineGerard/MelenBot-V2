package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PingCommand implements ICommand {


    @Override
    public String getDescription() {
        return "Renvoie le ping du bot";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        Message msg = event.getChannel().sendMessage("Pong!").complete();
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA())
                .setTitle("Pong !")
                .addField("Gateway Ping : ", String.format("%dms", event.getJDA().getGatewayPing()), false)
                .addField("Rest Ping : ", String.format("%sms", event.getJDA().getRestPing().complete()), false);
        msg.editMessageEmbeds(eb.build()).queue();
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

}
