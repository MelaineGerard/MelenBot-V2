package fr.melaine_gerard.melenbot.utils;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class FunctionsUtils {

    public static String getDurationBreakdown(long millis) {
        if(millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if(days != 0) sb.append(days).append(" jours ");
        if(hours != 0) sb.append(hours).append(" heures ");
        if(minutes != 0) sb.append(minutes).append(" minutes ");
        sb.append(seconds).append(" secondes");
        return(sb.toString());
    }

    public static String getImageFromApi(String link, TextChannel channel) {
        HttpResponse<JsonNode> response = Unirest.get("https://melenbot-api.melaine-gerard.fr/pikachu")
                .header("accept", "application/json")
                .asJson();
        if(!response.getBody().getObject().has("image")){
            channel.sendMessage("Aucun résultat trouvé !").queue();
            return "";
        }
        return response.getBody().getObject().getString("image");
    }
}
