package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.MelenBot;
import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class StackOverflowCommand implements ICommand {

    @Override
    public String getDescription() {
        return "Permet de récupérer 5 questions correspondant ce que vous cherchez sur Stackoverflow";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(" ");
        String query = sb.toString();
        HttpResponse<JsonNode> response = Unirest.get("https://api.stackexchange.com/2.2/search?pagesize=5&order=desc&sort=activity&intitle=" + query + "&site=stackoverflow")
                .header("accept", "application/json")
                .header("key", MelenBot.getConfig().getString("stackoverflow.key"))
                .asJson();
        JSONArray items = response.getBody().getObject().getJSONArray("items");
        if(items.length() == 0){
            event.getChannel().sendMessage("Aucun résultat trouvé !").queue();
            return;
        }
        EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA());
        eb.setTitle("Les " + items.length() + " premiers résultats trouvé pour ta recherche sur StackOverflow");
        StringBuilder desc = new StringBuilder();
        for (int i = 0 ; i < items.length(); i++){
            desc.append("▫️ [").append(items.getJSONObject(i).getString("title")).append("](").append(items.getJSONObject(i).getString("link")).append(")\n");
        }
        eb.setDescription(desc.toString());
        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public boolean hasArgs() {
        return true;
    }
}
