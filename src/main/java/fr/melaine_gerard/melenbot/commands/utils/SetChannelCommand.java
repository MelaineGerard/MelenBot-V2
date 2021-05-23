package fr.melaine_gerard.melenbot.commands.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.enumerations.ChanType;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SetChannelCommand implements ICommand {

    @Override
    public String getDescription() {
        return "Permet de définir des salons qui sont utilisé pour certaines commandes";
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        if(args.size() < 2) {
            event.getChannel().sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer le type de channel à modifier ainsi que mentionner le salon en question !").build()).queue();
            return;
        }
        boolean hasType = false;
        ChanType chanType = null;
        for (ChanType channelType : ChanType.values()) {
            if(channelType.getName().equalsIgnoreCase(args.get(0))){
                hasType = true;
                chanType = channelType;
            }
        }

        if(!hasType){
            event.getChannel().sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer un type de channel valide !").build()).queue();
            return;
        }

        if(event.getMessage().getMentionedChannels().isEmpty() || event.getMessage().getMentionedChannels().get(0).getType() != ChannelType.TEXT) {
            event.getChannel().sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer un type de channel valide !").build()).queue();
            return;
        }
        TextChannel channel = event.getMessage().getMentionedChannels().get(0);
        if(chanType == ChanType.LOGS){
            DatabaseUtils.updateLogsChannel(event.getGuild().getId(), channel.getId());
            event.getChannel().sendMessage(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau salon des logs est " + channel.getAsMention()).build()).queue();
        }else if (chanType == ChanType.SUGGESTIONS){
            DatabaseUtils.updateSuggestionsChannel(event.getGuild().getId(), channel.getId());
            event.getChannel().sendMessage(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau salon des suggestions est " + channel.getAsMention()).build()).queue();
        
        } else {
            DatabaseUtils.updateWelcomeChannel(event.getGuild().getId(), channel.getId());
            event.getChannel().sendMessage(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau salon de bienvenue est " + channel.getAsMention()).build()).queue();
        }
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public Collection<Permission> permissionsNeeded() {
        
        return Collections.singletonList(Permission.MANAGE_CHANNEL);
    }

    @Override
    public boolean hasArgs() {
        return true;
    }

    @Override
    public String getUsage() {
        return "<type> <#channel>";
    }
    
}
