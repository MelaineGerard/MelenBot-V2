package fr.melaine_gerard.melenbot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.lavaplayer.GuildMusicManager;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class NowPlayingCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de voir la musique en cours de lecture";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MUSIC;
    }

    @Override
    public void handle(MessageReceivedEvent event, List<String> args) {
        final MessageChannelUnion channel = event.getChannel();
        final Member selfMember = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (selfVoiceState == null || !selfVoiceState.inAudioChannel()) {
            channel.sendMessage("Je dois être dans un salon vocal !").queue();
            return;
        }

        final Member member = event.getMember();

        if (member == null) {
            channel.sendMessage("Je n'arrive pas à récupérer ton profil !").queue();
            return;
        }

        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            channel.sendMessage("Tu dois être dans un salon vocal !").queue();
            return;
        }

        if (memberVoiceState.getChannel() == null || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("Je dois être dans le même salon que toi !").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        if (playingTrack == null) {
            channel.sendMessage("Aucune musique en cours de lecture !").queue();
            return;
        }

        final AudioTrackInfo info = playingTrack.getInfo();

        channel.sendMessageFormat("Musique en cours de lecture : `%s` par `%s` (lien : <%s>)", info.title, info.author, info.uri).queue();

    }
}
