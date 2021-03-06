package fr.melaine_gerard.melenbot.commands.music;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet au bot de se déconnecter du salon";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MUSIC;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        final TextChannel channel = event.getChannel();
        final Member selfMember = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("Je dois être dans un salon vocal !").queue();
            return;
        }

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("Tu dois être dans un salon vocal !").queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage("Je dois être dans le même salon que toi !").queue();
            return;
        }

        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.repeating = false;
        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
        PlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.player.stopTrack();

        final AudioManager audioManager = event.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
        channel.sendMessage("Deconnexion du salon vocal en cours...").queue();
    }
}
