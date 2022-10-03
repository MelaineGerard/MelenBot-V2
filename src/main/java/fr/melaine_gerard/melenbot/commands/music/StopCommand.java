package fr.melaine_gerard.melenbot.commands.music;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.lavaplayer.GuildMusicManager;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class StopCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de stopper la musique";
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
            channel.sendMessage("Je n'arrive pas à récupérer ton profil ! ").queue();
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

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        final AudioManager audioManager = event.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
        channel.sendMessage("Musique stoppé et file d'attente vidée !").queue();


    }

}
