package fr.melaine_gerard.melenbot.commands.music;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.lavaplayer.GuildMusicManager;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de changer le volume du bot";
    }

    @Override
    public String getUsage() {
        return "<volume>";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final TextChannel channel = event.getChannel();
        Member member = event.getMember();
        if (args.isEmpty()){
            channel.sendMessage("Merci d'indiquer le volume souhaité !").queue();
            return;
        }
        int volume;
        try{
            volume = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e){
            channel.sendMessage("Merci d'indiquer le volume souhaité !").queue();
            return;
        }
        if (volume < 0 ||volume > 100) {
            channel.sendMessage("Merci d'indiquer un volume entre 0 et 100 !").queue();
            return;
        }

        if (member == null) return;
        GuildVoiceState voiceState = member.getVoiceState();
        if (voiceState == null || !voiceState.inVoiceChannel()) {
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Tu dois être dans un salon vocal").build()).queue();
            return;
        }
        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState selfMemberVoiceState = selfMember.getVoiceState();
        if (selfMemberVoiceState == null || !selfMemberVoiceState.inVoiceChannel()) {
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans un salon vocal").build()).queue();
            return;
        }
        if (selfMemberVoiceState.getChannel() != voiceState.getChannel()) {
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans le même salon vocal que toi !").build()).queue();
            return;
        }
        musicManager.audioPlayer.setVolume(volume);
        channel.sendMessage("Passage du volume à " + volume + "%...").queue();

    }
}
