package fr.melaine_gerard.melenbot.commands;

import fr.melaine_gerard.melenbot.enumerarions.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class PingCommand implements ICommand {


    @Override
    public String getDescription() {
        return "Renvoie le ping du bot";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        event.getChannel().sendMessage("Pong!").queue(msg -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Pong !")
                    .setFooter(event.getJDA().getSelfUser().getName(), event.getJDA().getSelfUser().getAvatarUrl())
                    .setColor(new Color(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()))
                    .addField("Gateway Ping : ", String.format("%dms", event.getJDA().getGatewayPing()), false)
                    .addField("Rest Ping : ", String.format("%sms", event.getJDA().getRestPing().complete()), false);
            msg.editMessage(eb.build()).queue();
        });
    }

    @Override
    public Category getCategory() {
        return Category.INFOS;
    }

}
