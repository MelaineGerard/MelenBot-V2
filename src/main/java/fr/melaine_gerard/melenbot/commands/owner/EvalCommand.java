package fr.melaine_gerard.melenbot.commands.owner;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class EvalCommand implements ICommand {
    private final GroovyShell engine;
    private final String imports;

    public EvalCommand(){
        this.engine = new GroovyShell();
        this.imports = """
                import java.io.*
                import java.lang.*
                import java.util.*
                import java.util.concurrent.*
                import net.dv8tion.jda.api.*
                import net.dv8tion.jda.api.entities.*
                import net.dv8tion.jda.api.entities.impl.*
                import net.dv8tion.jda.api.managers.*
                import net.dv8tion.jda.api.managers.impl.*
                import net.dv8tion.jda.api.utils.*
                import fr.melaine_gerard.melenbot.*
                import fr.melaine_gerard.melenbot.utils.*
                import fr.melaine_gerard.melenbot.utils.db.*
                """;
    }

    @Override
    public String getDescription() {
        return "Permet de tester l'exécution d'un bout de code";
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        if(args.isEmpty()){
            event.getChannel().sendMessage("Merci d'indiquer le code à exécuter").queue();
            return;
        }

        try {
            engine.setProperty("args", args);
            engine.setProperty("event", event);
            engine.setProperty("message", event.getMessage());
            engine.setProperty("channel", event.getChannel());
            engine.setProperty("jda", event.getJDA());
            engine.setProperty("guild",event.getGuild());
            engine.setProperty("member", event.getMember());

            String script = imports + event.getMessage().getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            if (out == null)
                event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Exécuté avec succès !").build()).queue();
            else if (out instanceof MessageEmbed resultEmbed)
                event.getChannel().sendMessageEmbeds(resultEmbed).queue();
            else
                event.getChannel().sendMessageEmbeds(EmbedUtils.createEmbed(event.getJDA()).setTitle("Code Exécuté : ").setDescription(String.format("```\n%s\n```", out)).build()).queue();
        }catch (Exception e){
            event.getChannel().sendMessage("Une erreur est survenu lors de l'éxécution de la commande : \n" + e.getMessage()).queue();
        }
    }


    @Override
    public boolean isOwnerCommand() {
        return true;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.OWNER;
    }
}
