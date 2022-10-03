package fr.melaine_gerard.melenbot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.lavaplayer.GuildMusicManager;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Affiche la file d'attente";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MUSIC;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        final MessageChannelUnion channel = event.getChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty()) {
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "La vie d'attente est vide !").build()).queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 10);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final EmbedBuilder eb = EmbedUtils.createEmbed(event.getJDA()).setTitle("File d'attente actuelle");
        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();
            eb.addField(" ", "**" + (i + 1) + ".** `" + info.title + "` - *" + info.author + "*", false);
        }

        if (trackList.size() > trackCount) {
            eb.setFooter("et " + (trackList.size() - trackCount) + " autres...");
        }
        channel.sendMessageEmbeds(eb.build()).queue();

    }

}
