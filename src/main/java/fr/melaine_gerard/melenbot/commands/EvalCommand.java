package fr.melaine_gerard.melenbot.commands;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class EvalCommand implements ICommand {
    private final GroovyShell engine;
    private final String imports;

    public EvalCommand(){
        this.engine = new GroovyShell();
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.api.*\n" +
                "import net.dv8tion.jda.api.entities.*\n" +
                "import net.dv8tion.jda.api.entities.impl.*\n" +
                "import net.dv8tion.jda.api.managers.*\n" +
                "import net.dv8tion.jda.api.managers.impl.*\n" +
                "import net.dv8tion.jda.api.utils.*\n" +
                "import fr.melaine_gerard.melenbot.*\n";
    }

    @Override
    public String getDescription() {
        return "Permet de tester l'éxecution d'un bout de code";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
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

            event.getChannel().sendMessage("```\n" + (out == null ? EmbedUtils.createSuccessEmbed(event.getJDA(), "Executed without error").build() : out.toString()) + "\n```").queue();
        }catch (Exception e){
            event.getChannel().sendMessage("Une erreur est survenu lors de l'éxécution de la commande : \n" + e.getMessage()).queue();
        }
    }


    @Override
    public boolean isOwnerCommand() {
        return true;
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
