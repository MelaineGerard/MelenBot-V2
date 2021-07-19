package fr.melaine_gerard.melenbot.commands.fun;

import fr.melaine_gerard.melenbot.MelenBot;
import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.FunctionsUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class HugCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet e faire un calin à quelqu'un";
    }

    @Override
    public String getUsage() {
        return "[personne]";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.FUN;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        String person = "lui-même";
        if(args.size() != 0){
            StringBuilder sb = new StringBuilder();
            for(String arg : args){
                sb.append(arg).append(" ");
            }
            person = sb.toString();
        }
        String imageUrl = FunctionsUtils.getImageFromApi("https://melenbot-api.melaine-gerard.fr/hug", event.getChannel());
        event.getChannel().sendMessageEmbeds(EmbedUtils.createEmbed(event.getJDA()).setTitle(event.getAuthor().getName() + " fait un câlin à " + person).setImage(imageUrl).build()).queue();
    }

}
