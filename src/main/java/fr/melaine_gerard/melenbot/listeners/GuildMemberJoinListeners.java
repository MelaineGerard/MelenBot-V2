package fr.melaine_gerard.melenbot.listeners;

import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class GuildMemberJoinListeners extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String welcomeMessage = DatabaseUtils.getWelcomeMessage(event.getGuild().getId());
        if (welcomeMessage == null) return;
        welcomeMessage = welcomeMessage.replaceAll("%users%", String.valueOf(event.getGuild().getMembers().size()))
                .replaceAll("%member%", event.getMember().getAsMention())
                .replaceAll("%tag%", event.getUser().getAsTag())
                .replaceAll("%guild%", event.getGuild().getName())
                .replaceAll("\\|", System.lineSeparator());
        String channelId = DatabaseUtils.getWelcomeChannel(event.getGuild().getId());
        if (channelId == null ||channelId.isEmpty()) return;
        TextChannel channel = event.getJDA().getTextChannelById(channelId);
        if (channel == null) return;
        channel.sendMessageEmbeds(EmbedUtils.createEmbed(event.getJDA()).setTitle("Nouveau membre").setDescription(welcomeMessage).build()).queue();
    }
}
