package fr.melaine_gerard.melenbot.listeners;

import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.db.DatabaseUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class GuildMemberJoinListeners extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String welcomeMessage = DatabaseUtils.getWelcomeMessage(event.getGuild().getId());
        if (welcomeMessage == null) return;
        System.out.println("1st attempt");
        welcomeMessage = welcomeMessage.replaceAll("%users%", String.valueOf(event.getGuild().getMembers().size()))
                .replaceAll("%member%", event.getMember().getAsMention())
                .replaceAll("%tag%", event.getUser().getAsTag())
                .replaceAll("%guild%", event.getGuild().getName())
                .replaceAll("\\|", System.lineSeparator());
        if(DatabaseUtils.getWelcomeChannel(event.getGuild().getId()) == null) return;
        TextChannel channel = event.getJDA().getTextChannelById(Objects.requireNonNull(DatabaseUtils.getWelcomeChannel(event.getGuild().getId())));
        System.out.println("nice");
        if (channel == null) return;
        System.out.println("3st attempt");
        channel.sendMessage(EmbedUtils.createEmbed(event.getJDA()).setTitle("Nouveau membre").setDescription(welcomeMessage).build()).queue();
    }
}
