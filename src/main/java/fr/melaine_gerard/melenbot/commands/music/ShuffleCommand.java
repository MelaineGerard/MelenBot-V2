package fr.melaine_gerard.melenbot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.lavaplayer.GuildMusicManager;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class ShuffleCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de mélanger les musiques dans la playlist";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        final TextChannel channel = event.getChannel();
        final Member selfMember = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans un salon vocal !").build()).queue();
            return;
        }

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Tu dois être dans un salon vocal !").build()).queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans le même salon que toi !").build()).queue();
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        if (musicManager.scheduler.queue.isEmpty()){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "La playlist est vide !").build()).queue();
            return;
        }
        List<AudioTrack> audioTracks = new ArrayList<>(musicManager.scheduler.queue);
        Collections.shuffle(audioTracks);
        musicManager.scheduler.queue.clear();
        musicManager.scheduler.queue.addAll(audioTracks);
        channel.sendMessage(EmbedUtils.createSuccessEmbed(event.getJDA(), "La playlist a été mélangé !").build()).queue();
    }
}
