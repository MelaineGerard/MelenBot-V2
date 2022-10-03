package fr.melaine_gerard.melenbot.commands.music;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.lavaplayer.GuildMusicManager;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class RepeatCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de répeter la musique en cours";
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
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans un salon vocal !").build()).queue();
            return;
        }

        final Member member = event.getMember();

        if (member == null) {
            channel.sendMessage("Je n'arrive pas à récupérer ton profil !").queue();
            return;
        }
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Tu dois être dans un salon vocal !").build()).queue();
            return;
        }

        if (memberVoiceState.getChannel() == null || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans le même salon que toi !").build()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final boolean newRepeating = !musicManager.scheduler.repeating;
        musicManager.scheduler.repeating = newRepeating;
        final String mode = newRepeating ? "répétition de la musique en cours" : "je continue la playlist";
        channel.sendMessageEmbeds(EmbedUtils.createSuccessEmbed(event.getJDA(), "Le bot passe en mode **" + mode + "** !").build()).queue();
    }
}
