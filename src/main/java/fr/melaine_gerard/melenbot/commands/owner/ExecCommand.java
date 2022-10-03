package fr.melaine_gerard.melenbot.commands.owner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExecCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet d'éxécuter une commande";
    }

    @Override
    public String getUsage() {
        return "commande";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.OWNER;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer une commande").build()).queue();
            return;
        }
        StringBuilder cmd = new StringBuilder();
        for (String arg : args) cmd.append(arg).append(" ");
        try {
            Process process = Runtime.getRuntime().exec(cmd.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty())
                    result.append(line).append("\n");
            }
            String resultat = result.toString();
            if (resultat.length() <= 2048)
                event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), result.toString()).build()).queue();
            else {
                HttpURLConnection connection;
                URL url = new URL("https://paste.melaine-gerard.fr/documents");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Melen Discord Bot");
                connection.setRequestProperty("Content-Length", Integer.toString(resultat.length()));
                connection.setUseCaches(false);
                String response = null;
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(resultat.getBytes(StandardCharsets.UTF_8));
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    response = reader1.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonObject object = new Gson().fromJson(response, JsonObject.class);
                String hasteUrl = "";
                if (object != null)
                    hasteUrl = "https://paste.melaine-gerard.fr/" + object.get("key").getAsString();
                event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Résultat trop long " + hasteUrl).build()).queue();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isOwnerCommand() {
        return true;
    }
}
