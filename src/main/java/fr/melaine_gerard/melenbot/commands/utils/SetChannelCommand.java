package fr.melaine_gerard.melenbot.commands.utils;

import fr.melaine_gerard.melenbot.enumerations.ChanType;
import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SetChannelCommand implements ICommand {

    @Override
    public String getDescription() {
        return "Permet de définir des salons qui sont utilisé pour certaines commandes";
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        if (args.size() < 2) {
            event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer le type de channel à modifier ainsi que mentionner le salon en question !").build()).queue();
            return;
        }
        boolean hasType = false;
        ChanType chanType = null;
        for (ChanType channelType : ChanType.values()) {
            if (channelType.getName().equalsIgnoreCase(args.get(0))) {
                hasType = true;
                chanType = channelType;
            }
        }

        if (!hasType) {
            event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer un type de channel valide !").build()).queue();
            return;
        }

        GuildChannel guildChannel = event.getMessage().getMentions().getChannels().get(0);
        if (event.getMessage().getMentions().getChannels().isEmpty() || guildChannel.getType() != ChannelType.TEXT) {
            event.getChannel().sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer un type de channel valide !").build()).queue();
            return;
        }

        if (chanType == ChanType.LOGS) {
            DatabaseUtils.updateLogsChannel(event.getGuild().getId(), guildChannel.getId());
            event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau salon des logs est " + guildChannel.getAsMention()).build()).queue();
        } else if (chanType == ChanType.SUGGESTIONS) {
            DatabaseUtils.updateSuggestionsChannel(event.getGuild().getId(), guildChannel.getId());
            event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau salon des suggestions est " + guildChannel.getAsMention()).build()).queue();

        } else {
            DatabaseUtils.updateWelcomeChannel(event.getGuild().getId(), guildChannel.getId());
            event.getChannel().sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le nouveau salon de bienvenue est " + guildChannel.getAsMention()).build()).queue();
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
