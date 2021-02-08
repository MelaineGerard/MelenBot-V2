package fr.melaine_gerard.melenbot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.lavaplayer.GuildMusicManager;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Affiche la file d'attente";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        final TextChannel channel = event.getChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if(queue.isEmpty()){
            channel.sendMessage("La vie d'attente est vide !").queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 10);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final MessageAction messageAction = channel.sendMessage("** File d'attente actuelle:**\n");

        for (int i = 0; i < trackCount; i++){
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();
            messageAction.append("#")
		    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(info.title)
                    .append("` - `")
                    .append(info.author)
                    .append("`\n");                  
	}

        if(trackList.size() > trackCount){
            messageAction.append("et `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` autres...");
        }

        messageAction.queue();

    }

}
